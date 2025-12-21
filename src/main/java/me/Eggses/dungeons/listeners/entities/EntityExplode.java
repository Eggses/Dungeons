package me.Eggses.dungeons.listeners.entities;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {

    private final DungeonEventRouter dungeonEventRouter;

    public EntityExplode(DungeonEventRouter dungeonEventRouter) {
        this.dungeonEventRouter = dungeonEventRouter;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        dungeonEventRouter.handleEntityExplodeEvent(event);

        System.out.println("Code for explosion called in Listener");
    }
}