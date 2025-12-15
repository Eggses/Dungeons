package me.Eggses.dungeons.listenersOLD;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntitygetHurt implements Listener {

    private final EntityManager entityManager;

    public EntitygetHurt(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @EventHandler
    public void onHurt(EntityDamageEvent event) {

        DungeonEntity dungeonEntity = entityManager.getDungeonEntity(event.getEntity().getUniqueId());
        if (dungeonEntity == null) return;

        dungeonEntity.updateName();

    }
}
