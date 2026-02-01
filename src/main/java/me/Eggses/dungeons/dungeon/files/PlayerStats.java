package me.Eggses.dungeons.dungeon.files;


import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dungeon.types.DungeonType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;
import java.util.UUID;

public class PlayerStats {

    private static final String FILE_NAME = "stats.yml";
    private static final String PLAYER_STATS_PATH = "player_stats";
    private static final String DUNGEONS = "dungeons";

    private static final String BEST_TIME = "best_time_ms";
    private static final String COMPLETIONS = "completions";

    private final Object lock = new Object();
    private BukkitTask saveTask;

    private final JavaPlugin plugin;
    private final ConfigurationFile configurationFile;

    public PlayerStats(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configurationFile = new ConfigurationFile(plugin, FILE_NAME);
    }

    public void addOrUpdateEntry(UUID uuid, DungeonType dungeonType, long timeTakenMs) {
        synchronized (lock) {
            FileConfiguration config = configurationFile.getCustomFile();

            String path = PLAYER_STATS_PATH + "." + uuid + "." + DUNGEONS + "." + dungeonType.name();

            ConfigurationSection section = config.getConfigurationSection(path);
            if (section == null) section = config.createSection(path);

            long bestTime = section.getLong(BEST_TIME, Long.MAX_VALUE);
            if (timeTakenMs < bestTime) section.set(BEST_TIME, timeTakenMs);

            section.set(COMPLETIONS, section.getInt(COMPLETIONS, 0) + 1);

            scheduleSaveLocked();
        }
    }

    public Optional<DungeonStat> getStatsFor(UUID uuid, DungeonType dungeonType) {
        synchronized (lock) {
            FileConfiguration config = configurationFile.getCustomFile();

            String path = PLAYER_STATS_PATH + "." + uuid + "." + DUNGEONS + "." + dungeonType.name();
            ConfigurationSection section = config.getConfigurationSection(path);
            if (section == null) return Optional.empty();

            return Optional.of(new DungeonStat(
                    dungeonType,
                    section.getLong(BEST_TIME, -1L),
                    section.getInt(COMPLETIONS, 0)
            ));
        }
    }

    public void reload() {
        synchronized (lock) {
            if (saveTask != null) {
                saveTask.cancel();
                saveTask = null;
            }
            configurationFile.reloadCustomFile();
        }
    }

    public void flushSave() {
        synchronized (lock) {
            if (saveTask != null) {
                saveTask.cancel();
                saveTask = null;
            }
            configurationFile.saveCustomFile();
        }
    }

    private void scheduleSaveLocked() {
        if (saveTask != null) saveTask.cancel();

        saveTask = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            synchronized (lock) {
                configurationFile.saveCustomFile();
                saveTask = null;
            }
        }, 20L);
    }

    public record DungeonStat(DungeonType dungeonType, long bestTimeMs, int completions) {}
}
