package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerEntityInteract implements Listener {

    private final DungeonEventRouter dungeonEventRouter;

    public PlayerEntityInteract(DungeonEventRouter dungeonEventRouter) {
        this.dungeonEventRouter = dungeonEventRouter;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        dungeonEventRouter.handleEvent(event, event.getPlayer().getWorld());
    }
}
