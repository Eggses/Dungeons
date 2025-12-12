package me.Eggses.dungeons.listeners.entities;

import me.Eggses.dungeons.dungeon.DungeonInstance;
import me.Eggses.dungeons.dungeon.DungeonManager;
import me.Eggses.dungeons.entities.EntityManager;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Optional;

public class EntityCombat implements Listener {

    private final DungeonManager dungeonManager;
    private EntityManager entityManager;

    public EntityCombat(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onEntityCombat(EntityDamageByEntityEvent event) {

        Optional<DungeonInstance> maybeInstance = dungeonManager.getDungeonInstance(event.getEntity().getWorld());
        if (maybeInstance.isEmpty()) return;



        event.getEntity().getWorld();


    }


}
