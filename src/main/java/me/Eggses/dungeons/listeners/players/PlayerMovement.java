package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovement implements Listener {

    private final DungeonEventRouter dungeonEventRouter;
    private final BlockRegistry blockRegistry;
    private final EventContext eventContext = EventContext.EMPTY;

    public PlayerMovement(DungeonEventRouter dungeonEventRouter, BlockRegistry blockRegistry) {
        this.dungeonEventRouter = dungeonEventRouter;
        this.blockRegistry = blockRegistry;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasExplicitlyChangedBlock()) return;
        dungeonEventRouter.handlePlayerMovementEvent(event);
        blockRegistry.handleEvent(event.getTo(), event, eventContext);
    }
}
