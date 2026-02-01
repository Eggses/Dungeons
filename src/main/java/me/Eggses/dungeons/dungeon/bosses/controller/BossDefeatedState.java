package me.Eggses.dungeons.dungeon.bosses.controller;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.regions.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BossDefeatedState implements ArenaControllerState {

    private final BossArenaController bossArenaController;
    private final Region entryRegion;
    private final DungeonInstance dungeonInstance;

    public BossDefeatedState(BossArenaController bossArenaController,
                             Region entryRegion,
                             DungeonInstance dungeonInstance) {
        this.bossArenaController = bossArenaController;
        this.entryRegion = entryRegion;
        this.dungeonInstance = dungeonInstance;
    }

    @Override
    public void onStateStart() {
        bossArenaController.clearPlayers();
        dungeonInstance.updatePlayerStats();
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
