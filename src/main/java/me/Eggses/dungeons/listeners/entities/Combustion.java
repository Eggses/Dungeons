package me.Eggses.dungeons.listeners.entities;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;

public class Combustion implements Listener {

    private final DungeonManager dungeonManager;

    public Combustion(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    /*
    entity.setFireTick(X) still is not canceled.
    This only prevents mobs burning in the sun.
     */

    @EventHandler
    public void onCombustion(EntityCombustEvent event) {

        if (!dungeonManager.isDungeonWorld(event.getEntity().getWorld())) return;

        if (event instanceof EntityCombustByEntityEvent) return;
        if (event instanceof EntityCombustByBlockEvent) return;

        event.setCancelled(true);
    }
}
