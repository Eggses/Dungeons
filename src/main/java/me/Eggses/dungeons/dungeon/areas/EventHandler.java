package me.Eggses.dungeons.dungeon.areas;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;
import java.util.function.BiFunction;

public class EventHandler {

    private final AreaController areaController;
    private final EntityManager entityManager;

    public EventHandler(AreaController areaController, EntityManager entityManager) {
        this.areaController = areaController;
        this.entityManager = entityManager;
    }

    public void handleMovementEventInDungeon(Location location, long chunkKey) {
        areaController.handlePlayerMoveEvent(location, chunkKey);
    }

    public void handleInteractEvent(Position positionOfBlock) {
        areaController.handleInteractEvent(positionOfBlock);
    }

    public void handleDungeonTriggerCommand(String argument) {
        areaController.handleDungeonTriggerCommand(argument);
    }

    public void handleEntityDeathEvent(UUID uuid) {
        areaController.handleEntityDeathEvent(uuid);
    }

    public void handlePlayerRespawnEvent(PlayerRespawnEvent event) {
        areaController.handlePlayerRespawnEvent(event);
    }

    public void handleEntityDamageEntityEvent(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof LivingEntity victim)) return;

        LivingEntity attacker = resolveTrueAttacker(event);
        if (attacker == null) return;

        UUID victimUUID = victim.getUniqueId();
        UUID attackerUUID = attacker.getUniqueId();

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
            dungeonAttacker.getEntityEventHandler().handleEntityDamageEntityEvent(dungeonAttacker, event);
            return;
        }

        DungeonEntity dungeonVictim = entityManager.getDungeonEntity(victimUUID);
        if (dungeonVictim != null) {
            dungeonVictim.updateHealthDisplay(event.getFinalDamage());
            dungeonVictim.getEntityEventHandler().handleEntityDamageEntityEvent(dungeonVictim, event);
        }
    }

    public LivingEntity resolveTrueAttacker(EntityDamageByEntityEvent event) {

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