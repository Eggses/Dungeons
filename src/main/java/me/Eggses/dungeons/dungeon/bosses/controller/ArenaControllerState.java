package me.Eggses.dungeons.dungeon.bosses.controller;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ArenaControllerState {
    void onStateStart();
    void handlePlayerMovement(Player player, Location movementLocation);
    void leaveBossArena(Player player);
}
