package me.Eggses.dungeons.listeners.entities;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;

public class Combustion implements Listener {

    private final DungeonRegistry dungeonRegistry;
    private final BlockRegistry blockRegistry;

    public Combustion(DungeonRegistry dungeonRegistry, BlockRegistry blockRegistry) {
        this.dungeonRegistry = dungeonRegistry;
        this.blockRegistry = blockRegistry;
    }

    /*
    entity.setFireTick(X) still is not canceled.
    This only prevents mobs burning in the sun.
     */

    @EventHandler
    public void onCombustion(EntityCombustEvent event) {

        if (!dungeonRegistry.isDungeonWorld(event.getEntity().getWorld())) return;

        if (event instanceof EntityCombustByEntityEvent) return;
        if (event instanceof EntityCombustByBlockEvent combustByBlockEvent) {
            Block block = combustByBlockEvent.getCombuster();
            if (block != null) {
                blockRegistry.handleEvent(block.getLocation(), combustByBlockEvent, EventContext.EMPTY);
            }
            return;
        }

        System.out.println("Burn Event Canclled");
        event.setCancelled(true);
    }
}
