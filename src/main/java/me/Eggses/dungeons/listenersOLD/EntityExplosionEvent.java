package me.Eggses.dungeons.listenersOLD;

import me.Eggses.dungeons.entities.EntityManager;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

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
            DungeonEntity dungeonEntity = entityManager.getDungeonEntity(event.getEntity().getUniqueId());
            dungeonEntity.getEntityEventBehaviour().handleExplosionEvent(dungeonEntity, event);
        }


    }
}
