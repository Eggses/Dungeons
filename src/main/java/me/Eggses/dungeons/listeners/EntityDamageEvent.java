package me.Eggses.dungeons.listeners;

import me.Eggses.dungeons.entities.EntityManager;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

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

        // Mob hits Player:
        // attacker is the mob:
        // defender is the player...

        // If they are both mobs, cancel the damage.
        if (entityManager.contains(uuidOfAttacker) && entityManager.contains(uuidOfDefender)) {
            event.setCancelled(true);
            return;
        }

        if ((!(attacker instanceof Player)) && defender instanceof Player) {
            DungeonEntity mob = entityManager.getDungeonEntity(uuidOfAttacker);
            mob.getEntityEventBehaviour().handleEntityDamageEntityEvent(mob, event);
        }


        // apply specific attribute damage
        if (event.getCause() == DamageCause.PROJECTILE) {

        }


        if (event.getCause() == DamageCause.ENTITY_EXPLOSION) {

        }
    }

    @EventHandler
    public void death(EntityDeathEvent deathEvent) {
        DungeonEntity dungeonEntity = entityManager.getDungeonEntity(deathEvent.getEntity().getUniqueId());
        if (dungeonEntity == null) return;
        dungeonEntity.endTasks();
    }
}
