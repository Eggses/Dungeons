package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovement implements Listener {

    private final DungeonEventRouter dungeonEventRouter;

    public PlayerMovement(DungeonEventRouter dungeonEventRouter) {
        this.dungeonEventRouter = dungeonEventRouter;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasExplicitlyChangedBlock()) return;
        dungeonEventRouter.handlePlayerMovementEvent(event);
    }
}
