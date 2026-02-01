package me.Eggses.dungeons.dungeon.bosses.controller;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.dungeon.bosses.DungeonBossBuilder;
import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.players.Players;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.RotationPosition;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;
import me.Eggses.dungeons.entities.mobs.EntityManager;
import me.Eggses.dungeons.tasks.TaskRunner;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BossArenaController {

    private final DungeonContext dungeonContext;
    private final World world;
    private final EntityManager entityManager;
    private final TaskRunner taskRunner;
    private final MessageCreator messageCreator;
    private final TextFormatter textFormatter;
    private final Players playersInArena = new Players();
    private final Location spawnLocation;
    private final Supplier<DungeonBossBuilder> bossBuilderSupplier;
    private final Consumer<DungeonContext> onBossDefeat;

    private ArenaControllerState activeState;
    private final ArenaControllerState readyToCommenceState;
    private final ArenaControllerState spawningBossState;
    private final ArenaControllerState pulledBossState;
    private final BossDefeatedState bossDefeatedState;

    private Boss boss;

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

        this.dungeonContext = dungeonContext;
        this.world = world;
        this.entityManager = entityManager;
        this.taskRunner = taskRunner;
        this.messageCreator = messageCreator;
        this.textFormatter = textFormatter;
        this.spawnLocation = spawnPosition.toLocation(world);
        this.bossBuilderSupplier = bossBuilderSupplier;
        this.onBossDefeat = onBossDefeat;

        this.readyToCommenceState = new ReadyToCommenceState(this, entryRegion);
        this.spawningBossState = new SpawningBossState(plugin, this, entryRegion);
        this.pulledBossState = new PulledBossState(this, entryRegion, messageCreator);
        this.bossDefeatedState = new BossDefeatedState(this, entryRegion, dungeonInstance);
        this.activeState = this.readyToCommenceState;
    }

    public void handlePlayerMovement(Player player, Location destination) {
        activeState.handlePlayerMovement(player, destination);
    }

    public void enterArena(Player player) {
        player.teleport(spawnLocation);
        playersInArena.add(player);
    }

    public void leaveArena(Player player) {
        activeState.leaveBossArena(player);
        if (boss != null) boss.removeBossBarViewer(player);
    }

    public Players getPlayersInArena() {
        return playersInArena;
    }

    public void startFight() {

        for (Player player : world.getPlayers()) {
            if (!playersInArena.contains(player)) {
                enterArena(player);
            }
        }

        this.boss = new Boss(
                bossBuilderSupplier.get(),
                entityManager,
                world,
                playersInArena,
                taskRunner,
                messageCreator,
                textFormatter,
                () -> {
                    this.changeStateToBossDefeatedState();
                    onBossDefeat.accept(dungeonContext);
                }
        );
        for (Player player : playersInArena.getPlayers()) {
            boss.addBossBarViewer(player);
        }
    }

    public void failBossFight() {
        if (boss != null) boss.failBossFight();
        boss = null;
    }

    public void clearPlayers() {
        playersInArena.clear();
    }

    public void changeStateToReadyToCommenceState() {
        changeStateToUnlessAlreadyActive(readyToCommenceState);
    }

    public void changeStateToBossSpawningState() {
        changeStateToUnlessAlreadyActive(spawningBossState);
    }

    public void changeStateToPulledBossState() {
        changeStateToUnlessAlreadyActive(pulledBossState);
    }

    public void changeStateToBossDefeatedState() {
        changeStateToUnlessAlreadyActive(bossDefeatedState);
    }

    private void changeStateToUnlessAlreadyActive(ArenaControllerState newState) {
        if (activeState == newState) return;
        activeState = newState;
        activeState.onStateStart();
    }
}
