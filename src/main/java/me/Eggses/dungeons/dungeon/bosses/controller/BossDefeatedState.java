package me.Eggses.dungeons.dungeon.bosses.controller;

import me.Eggses.dungeons.dungeon.regions.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BossDefeatedState implements ArenaControllerState {

    private final BossArenaController bossArenaController;
    private final Region entryRegion;

    public BossDefeatedState(BossArenaController bossArenaController, Region entryRegion) {
        this.bossArenaController = bossArenaController;
        this.entryRegion = entryRegion;
    }

    @Override
    public void onStateStart() {
        bossArenaController.clearPlayers();
    }

    @Override
    public void handlePlayerMovement(Player player, Location movementLocation) {
        if (entryRegion.within(movementLocation)) {
            bossArenaController.enterArena(player);
        }
    }

    @Override
    public void leaveBossArena(Player player) {
    }
}
