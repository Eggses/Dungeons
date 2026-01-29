package me.Eggses.dungeons.listeners.entities;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.dungeon.regions.WorldPosition;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityMovement implements Listener {

    private final BlockRegistry blockRegistry;
    public EntityMovement(BlockRegistry blockRegistry) {
        this.blockRegistry = blockRegistry;
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        if (!event.hasExplicitlyChangedBlock()) return;
        blockRegistry.handleEvent(new WorldPosition(event.getTo()), event, EventContext.EMPTY);
    }
}
