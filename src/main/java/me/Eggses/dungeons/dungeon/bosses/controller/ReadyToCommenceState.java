package me.Eggses.dungeons.dungeon.bosses.controller;

import me.Eggses.dungeons.dungeon.regions.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ReadyToCommenceState implements ArenaControllerState {

    private final BossArenaController bossArenaController;
    private final Region entryRegion;

    public ReadyToCommenceState(BossArenaController bossArenaController, Region entryRegion) {
        this.bossArenaController = bossArenaController;
        this.entryRegion = entryRegion;
    }

    @Override
    public void onStateStart() {
    }

    @Override
    public void handlePlayerMovement(Player player, Location movementLocation) {
        if (entryRegion.within(movementLocation)) {
            bossArenaController.enterArena(player);
            bossArenaController.changeStateToBossSpawningState();
        }
    }

    @Override
    public void leaveBossArena(Player player) {
    }
}
