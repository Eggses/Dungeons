package me.Eggses.dungeons.listeners.entities;

import me.Eggses.dungeons.entities.EntityManager;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class EntityCombat implements Listener {

    private final EntityManager entityManager;

    public EntityCombat(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {

        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();

        UUID uuidOfAttacker = attacker.getUniqueId();
        UUID uuidOfVictim = victim.getUniqueId();

        boolean attackerIsDungeonEntity = entityManager.contains(uuidOfAttacker);
        boolean victimIsDungeonEntity = entityManager.contains(uuidOfVictim);

        // Both Dungeon Entities
        // that dosnt work.. what if someone fired an arrow... then they are that...
        // like
        if (attackerIsDungeonEntity && victimIsDungeonEntity) {
            event.setCancelled(true);
            return;

        }
        SplashPotion
        Projectile

        if
        // Dungeon Entity Hits Player
        // dosnt work for ranged holy shit...
        if (attackerIsDungeonEntity && victim instanceof Player) {
            handleDungeonEntityAttackingPlayer(event);
            return;
        }
    }





    private void handleDungeonEntityAttackingPlayer(EntityDamageByEntityEvent event) {

    }


    private enum AttackCause {

        RANGED(),
        MAGIC(),
    }

}
