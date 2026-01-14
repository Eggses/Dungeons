package me.Eggses.dungeons.dungeon.events.core;

import me.Eggses.dungeons.entities.mobs.EntityManager;
import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;
import java.util.function.BiFunction;

public class EntityDamageEntity implements EventBehaviour<EntityDamageByEntityEvent> {

    private final EntityManager entityManager;

    public EntityDamageEntity(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /*
    This does have an EventContext of its own, and it can use it - but it does not.
    It instead creates its own event context and passes that to ANOTHER EventManager
    they are different EventContexts.
     */
    @Override
    public void handleEvent(EntityDamageByEntityEvent event, EventContext eventContext) {
        if (!(event.getEntity() instanceof LivingEntity victim)) return;

        LivingEntity trueAttacker = resolveTrueAttacker(event);
        if (trueAttacker == null) return;

        UUID victimUUID = victim.getUniqueId();
        UUID attackerUUID = trueAttacker.getUniqueId();

        if (entityManager.contains(victimUUID) && entityManager.contains(attackerUUID)) {
            event.setCancelled(true);
            return;
        }

        DungeonEntity dungeonAttacker = entityManager.getDungeonEntity(attackerUUID);
        if (dungeonAttacker != null && victim instanceof Player) {
            BiFunction<DungeonEntity, Double, Double> damageFormula = switch (event.getCause()) {
                case PROJECTILE -> AttributeController.getRangedDamageFormula();
                case ENTITY_EXPLOSION -> AttributeController.getExplosionDamageFormula();
                case MAGIC -> AttributeController.getMagicDamageFormula();
                default -> AttributeController.getIdentityDamageFormula();
            };
            event.setDamage(damageFormula.apply(dungeonAttacker, event.getDamage()));

            EventContext innerEventContext = EventContext
                    .builder()
                    .ownerOfBehaviour(dungeonAttacker)
                    .trueAttacker(trueAttacker)
                    .build();

            dungeonAttacker.handleEvent(event, innerEventContext);
            return;
        }

        DungeonEntity dungeonVictim = entityManager.getDungeonEntity(victimUUID);
        if (dungeonVictim != null) {
            dungeonVictim.updateHealthDisplay(event.getFinalDamage());

            EventContext innerEventContext = EventContext
                    .builder()
                    .ownerOfBehaviour(dungeonVictim)
                    .trueAttacker(trueAttacker)
                    .build();

            dungeonVictim.handleEvent(event, innerEventContext);
        }
    }

    private LivingEntity resolveTrueAttacker(EntityDamageByEntityEvent event) {

        Entity attacker = event.getDamager();

        switch (attacker) {
            case LivingEntity livingEntity -> {
                return livingEntity;
            }
            case Projectile projectile -> {
                ProjectileSource projectileSource = projectile.getShooter();
                return (projectileSource instanceof LivingEntity livingEntity) ? livingEntity : null;
            }
            case AreaEffectCloud areaEffectCloud -> {
                ProjectileSource projectileSource = areaEffectCloud.getSource();
                return (projectileSource instanceof LivingEntity livingEntity) ? livingEntity : null;
            }
            case EvokerFangs evokerFangs -> {
                return evokerFangs.getOwner();
            }
            default -> {
                return null;
            }
        }
    }
}
