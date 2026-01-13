package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
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
        dungeonEventRouter.handleEvent(event, event.getPlayer().getWorld());
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        dungeonEventRouter.handleEvent(event, event.getPlayer().getWorld());
    }
}