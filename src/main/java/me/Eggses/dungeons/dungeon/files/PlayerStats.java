package me.Eggses.dungeons.dungeon.files;

import me.Eggses.dungeons.configuration.ConfigurationFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerStats {

    private static final String FILE_NAME = "stats.yml";
    private static final String PLAYER_STATS_PATH = "player_stats";

    private final JavaPlugin plugin;
    private final ConfigurationFile configurationFile;

    private final Map<UUID, List<DungeonStat>> dungeonStats = new HashMap<>();

    public PlayerStats(JavaPlugin plugin) {
        this.plugin = plugin;
        configurationFile = new ConfigurationFile(plugin, FILE_NAME);
    }

    private void readFile() {
    }




    private record DungeonStat(String dungeonName, int bestTimeSeconds, int completions) {}

    /*

    store player stats in a config somewhere that would be cool!
     */
}
