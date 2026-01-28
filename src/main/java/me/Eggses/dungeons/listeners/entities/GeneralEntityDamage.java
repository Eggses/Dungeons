package me.Eggses.dungeons.listeners.entities;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class GeneralEntityDamage implements Listener {

    private final DungeonEventRouter dungeonEventRouter;

    public GeneralEntityDamage(DungeonEventRouter dungeonEventRouter) {
        this.dungeonEventRouter = dungeonEventRouter;
    }

    // Runs last, and ignores if the event is cancelled.
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        dungeonEventRouter.handleEvent(event, event.getEntity().getWorld());
    }
}
