package me.Eggses.dungeons.dungeon.bosses.controller;

import me.Eggses.dungeons.dungeon.players.Players;
import me.Eggses.dungeons.dungeon.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class SpawningBossState implements ArenaControllerState {

    private static final long SPAWN_BOSS_AFTER_SECONDS = 10 * 20;

    private final JavaPlugin plugin;
    private final BossArenaController bossArenaController;
    private final Region entryRegion;

    private BukkitTask bossSpawningTask;

    public SpawningBossState(JavaPlugin plugin,
                             BossArenaController bossArenaController,
                             Region entryRegion) {

        this.plugin = plugin;
        this.bossArenaController = bossArenaController;
        this.entryRegion = entryRegion;
    }

    @Override
    public void onStateStart() {
        bossSpawningTask = Bukkit.getScheduler().runTaskLater(
                plugin,
                bossArenaController::changeStateToPulledBossState,
                SPAWN_BOSS_AFTER_SECONDS
        );
    }

    @Override
    public void handlePlayerMovement(Player player, Location movementLocation) {
        if (entryRegion.within(movementLocation)) {
            bossArenaController.enterArena(player);
        }
    }

    @Override
    public void leaveBossArena(Player player) {
        Players players = bossArenaController.getPlayersInArena();
        players.remove(player);

        if (players.isEmpty()) {
            bossSpawningTask.cancel();
            bossSpawningTask = null;
            bossArenaController.changeStateToReadyToCommenceState();
        }
    }
}
