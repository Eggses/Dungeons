package me.Eggses.dungeons.listeners.players.dungeonchanges;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    private final DungeonEventRouter dungeonEventRouter;

    public PlayerInteract(DungeonEventRouter dungeonEventRouter) {
        this.dungeonEventRouter = dungeonEventRouter;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        dungeonEventRouter.handlePlayerInteractEvent(event);
    }
}