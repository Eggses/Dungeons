package me.Eggses.dungeons.listeners.entities;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityCombat implements Listener {

    private final DungeonEventRouter dungeonEventRouter;

    public EntityCombat(DungeonEventRouter dungeonEventRouter) {
        this.dungeonEventRouter = dungeonEventRouter;
    }

    @EventHandler
    public void onEntityHitEntity(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent entityEvent) {
            dungeonEventRouter.handleEvent(entityEvent, event.getEntity().getWorld());
        }
        dungeonEventRouter.handleEvent(event, event.getEntity().getWorld());
    }
}