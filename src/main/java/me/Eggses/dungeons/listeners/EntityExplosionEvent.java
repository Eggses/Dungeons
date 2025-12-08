package me.Eggses.dungeons.listeners;

import me.Eggses.dungeons.Dungeons;
import me.Eggses.dungeons.entities.EntityManager;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Optional;

public class EntityExplosionEvent implements Listener {

    private final EntityManager entityManager;

    public EntityExplosionEvent(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @EventHandler
    public void onExplosionDamage(EntityExplodeEvent event) {

        /*
        optional is probably pointless with this if check!!!
         */


        if (entityManager.contains(event.getEntity().getUniqueId())) {
           Optional<DungeonEntity> maybeMob = entityManager.getDungeonEntity(event.getEntity().getUniqueId());
           maybeMob.ifPresent(mob -> mob.getEntityEventBehaviour().handleExplosionEvent(mob, event));
        }


    }
}
