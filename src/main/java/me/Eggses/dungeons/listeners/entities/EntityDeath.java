package me.Eggses.dungeons.listeners.entities;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener {

    private final DungeonManager dungeonManager;

    public EntityDeath(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        Entity entity = event.getEntity();
        World world = entity.getWorld();

        // This handles checking if it is in the World.
        dungeonManager.handleEntityDeathEventInDungeon(world, entity.getUniqueId());
    }
}
