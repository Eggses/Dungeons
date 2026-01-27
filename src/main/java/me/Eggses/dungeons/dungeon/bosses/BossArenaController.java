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
    private final Players playersInArena = new Players();
    private final Region entryRegion;
    private final Location spawnLocation;
    private final Supplier<DungeonBossBuilder> bossBuilderSupplier;
    private final Consumer<DungeonContext> onBossDefeat;
    private Boss boss;

    private boolean allowEntry = true;
    private boolean bossIsSpawning = false;
    private BukkitTask bossSpawningTask;

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
        this.spawnLocation = spawnPosition.toLocation(world);
        this.bossBuilderSupplier = bossBuilderSupplier;
        this.onBossDefeat = onBossDefeat;
    }

    public void handlePlayerMovement(Player player, Location movementLocation) {
        if (!allowEntry) return;

        if (entryRegion.within(movementLocation)) {
            enterBossArena(player);

            if (!bossIsSpawning) {
                bossIsSpawning = true;
                bossSpawningTask = Bukkit.getScheduler().runTaskLater(plugin, this::startFight, 30 * 10);
            }
        }
    }

    private void enterBossArena(Player player) {
        player.teleport(spawnLocation);
        playersInArena.add(player);
    }

    public void leaveBossArena(Player player) {
        playersInArena.remove(player);

        if (boss != null) {
            boss.removeBossBarViewer(player);
        }

        if (playersInArena.isEmpty()) {
            if (!bossIsSpawning && boss != null) {
                boss.failBossFight();
            } else if (bossSpawningTask != null) {
                bossSpawningTask.cancel();
                bossIsSpawning = false;
            }
        }
    }

    public void startFight() {

        for (Player player : world.getPlayers()) {
            if (!playersInArena.contains(player)) {
                enterBossArena(player);
            }
        }
        allowEntry = false;

        boss = new Boss(
                bossBuilderSupplier.get(),
                entityManager,
                world,
                playersInArena,
                taskRunner,
                messageCreator,
                textFormatter,
                () -> onBossDefeat.accept(dungeonContext)
        );

        bossIsSpawning = false;

        for (Player player : playersInArena.getPlayers()) {
            boss.addBossBarViewer(player);
        }
    }
}
