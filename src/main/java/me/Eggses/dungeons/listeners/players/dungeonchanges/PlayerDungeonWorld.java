package me.Eggses.dungeons.listeners.players.dungeonchanges;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDungeonWorld implements Listener {

    private final DungeonEventRouter dungeonEventRouter;

    public PlayerDungeonWorld(DungeonEventRouter dungeonEventRouter) {
        this.dungeonEventRouter = dungeonEventRouter;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {

        Player player = event.getPlayer();

        World originalWorld = event.getFrom();
        World currentWorld = player.getWorld();

        dungeonEventRouter.handlePlayerChangeWorldEvent(player, originalWorld, currentWorld);
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        dungeonEventRouter.handlePlayerQuitEvent(event);
    }
}