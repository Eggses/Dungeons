package me.Eggses.dungeons.dungeon.bosses;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.players.Players;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.RotationPosition;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;
import me.Eggses.dungeons.entities.mobs.EntityManager;
import me.Eggses.dungeons.tasks.TaskRunner;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BossArenaController {

    private final JavaPlugin plugin;
    private final DungeonInstance dungeonInstance;
    private final DungeonContext dungeonContext;
    private final World world;
    private final EntityManager entityManager;
    private final TaskRunner taskRunner;
    private final MessageCreator messageCreator;
    private final TextFormatter textFormatter;
    private final Players playersInFight = new Players();
    private final Region entryRegion;
    private final RotationPosition spawnPosition;
    private final Supplier<DungeonBossBuilder> bossBuilderSupplier;
    private final Consumer<DungeonContext> onBossDefeat;
    private Boss boss;
    private boolean allowEntry = true;
    private BukkitTask spawningBoss;

    public BossArenaController(JavaPlugin plugin,
                               DungeonInstance dungeonInstance,
                               DungeonContext dungeonContext,
                               World world,
                               EntityManager entityManager,
                               TaskRunner taskRunner,
                               MessageCreator messageCreator,
                               TextFormatter textFormatter,
                               Region entryRegion,
                               RotationPosition spawnPosition,
                               Supplier<DungeonBossBuilder> bossBuilderSupplier,
                               Consumer<DungeonContext> onBossDefeat) {
        this.plugin = plugin;
        this.dungeonInstance = dungeonInstance;
        this.dungeonContext = dungeonContext;
        this.world = world;
        this.entityManager = entityManager;
        this.taskRunner = taskRunner;
        this.messageCreator = messageCreator;
        this.textFormatter = textFormatter;
        this.entryRegion = entryRegion;
        this.spawnPosition = spawnPosition;
        this.bossBuilderSupplier = bossBuilderSupplier;
        this.onBossDefeat = onBossDefeat;
    }


    public void handlePlayerMovement(Player player, Location to) {
        if (!allowEntry) return;

        if (entryRegion.within(to)) {
            enterBossFight(player);
        }
    }

    public void enterBossFight(Player player) {
        player.teleport(spawnPosition.toLocation(world));
        playersInFight.add(player);

        if (spawningBoss != null) return;
        spawningBoss = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try {
                startFight();
            } finally {
                spawningBoss = null;
            }
        }, 20 * 30);
    }

    public void exitBossFight(Player player) {
        playersInFight.remove(player);

        if (playersInFight.isEmpty()) {
            if (spawningBoss != null) {
                spawningBoss.cancel();
                spawningBoss = null;
            } else {
                if (boss != null) boss.failBossFight();
                allowEntry = true;
            }
        } else {
            if (boss != null) boss.removeBossBarViewer(player);
        }
    }

    private void startFight() {
        for (Player player : world.getPlayers()) {
            if (!playersInFight.contains(player)) enterBossFight(player);
        }
        allowEntry = false;

        boss = new Boss(bossBuilderSupplier.get(), this, world, taskRunner, messageCreator, textFormatter);
        entityManager.addMob(boss);

        for (Player player : playersInFight.getPlayers()) {
            boss.addBossBarViewer(player);
        }
    }

    public void defeatBoss() {
        onBossDefeat.accept(dungeonContext);
    }

    public boolean isInFight(Player player) {
        return playersInFight.contains(player);
    }

    public Set<Player> getPlayers() {
        return playersInFight.getPlayers();
    }

    public World getWorld() {
        return dungeonInstance.getDungeonWorld();
    }
}
