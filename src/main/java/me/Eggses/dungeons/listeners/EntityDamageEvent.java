package me.Eggses.dungeons.listeners;

import me.Eggses.dungeons.entities.EntityManager;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Optional;
import java.util.UUID;

public class EntityDamageEvent implements Listener {

    private final EntityManager entityManager;

    public EntityDamageEvent(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @EventHandler
    public void onDamageEvent(EntityDamageByEntityEvent event) {

        Entity attacker = event.getDamager();
        Entity defender = event.getEntity();

        UUID uuidOfAttacker = attacker.getUniqueId();
        UUID uuidOfDefender = defender.getUniqueId();

        // If they are both mobs, cancel the damage.
        if (entityManager.contains(uuidOfAttacker) && entityManager.contains(uuidOfDefender)) {
            event.setCancelled(true);
            return;
        }

        Optional<DungeonEntity> maybeMob = entityManager.getDungeonEntity(uuidOfDefender);

        maybeMob.ifPresent(dungeonEntity ->
                        dungeonEntity.getEntityEventBehaviour().handleEntityDamageEntityEvent(dungeonEntity, event));


        Optional<DungeonEntity> maybeMob2 = entityManager.getDungeonEntity(uuidOfDefender);
        maybeMob2.ifPresent(DungeonEntity::updateName);

        // apply specific attribute damage
        if (event.getCause() == DamageCause.PROJECTILE) {

        }


        if (event.getCause() == DamageCause.ENTITY_EXPLOSION) {

        }





    }
}
