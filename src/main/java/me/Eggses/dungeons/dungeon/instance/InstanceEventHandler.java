package me.Eggses.dungeons.dungeon.instance;

import me.Eggses.dungeons.dungeon.areas.AreaController;
import me.Eggses.dungeons.dungeon.areas.EntityManager;
import me.Eggses.dungeons.dungeon.portals.PortalController;
import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;
import java.util.function.BiFunction;

public class InstanceEventHandler {

    private final DungeonInstance dungeonInstance;
    private final AreaController areaController;
    private final EntityManager entityManager;

    public InstanceEventHandler(DungeonInstance dungeonInstance,
                                AreaController areaController,
                                EntityManager entityManager) {

        this.dungeonInstance = dungeonInstance;
        this.areaController = areaController;
        this.entityManager = entityManager;
    }

    /* =========================================================
     * Core Dungeon Area Control Events
     * ========================================================= */

    public void handleMovementEventOutsideDungeon(Player player, Location destination) {

        PortalController portalController = dungeonInstance.getPortalController();

        if (!portalController.isOpen()) return;

        if (portalController.isInPortalOutsideDungeon(destination)) {
            portalController.enterDungeon(player, dungeonInstance.getDungeonWorld());
        }
    }

    public void handleMovementEventInDungeon(Player player, Location destination, long chunkKey) {

        PortalController portalController = dungeonInstance.getPortalController();

        areaController.handlePlayerMovement(destination, chunkKey);

        if (portalController.isInPortalInDungeonWorld(destination)) {
            portalController.leaveDungeon(player);
        }
    }

    public void handleDungeonTriggerCommand(String argument) {
        areaController.handleDungeonTriggerCommand(argument);
    }

    /* =========================================================
     * Other Player Events
     * ========================================================= */

    public void handlePlayerRespawnEvent(PlayerRespawnEvent event) {
        event.setRespawnLocation(areaController.getGraveyardRespawnLocation());
    }

    public void handlePlayerQuitEvent(PlayerQuitEvent event) {
        dungeonInstance.removePlayer(event.getPlayer());
    }

    /* =========================================================
     * Entity Events
     * ========================================================= */

    public void handleEntityDeathEvent(EntityDeathEvent event) {
        areaController.handleEntityDeath(event.getEntity().getUniqueId());
    }

    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {

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

            EventContext eventContext = EventContext
                    .builder()
                    .ownerOfBehaviour(dungeonAttacker)
                    .trueAttacker(trueAttacker)
                    .build();

            dungeonAttacker.handleEvent(event, eventContext);
            return;
        }

        DungeonEntity dungeonVictim = entityManager.getDungeonEntity(victimUUID);
        if (dungeonVictim != null) {
            dungeonVictim.updateHealthDisplay(event.getFinalDamage());

            EventContext eventContext = EventContext
                    .builder()
                    .ownerOfBehaviour(dungeonVictim)
                    .trueAttacker(trueAttacker)
                    .build();

            dungeonVictim.handleEvent(event, eventContext);
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

    public void handleExplosionPrimeEvent(ExplosionPrimeEvent event) {
        passEventToMobIfExists(event.getEntity(), event, EventContext.EMPTY);
    }

    public <E extends Event> void passEventToMobIfExists(Entity entity, E event, EventContext eventContext) {
        DungeonEntity dungeonEntity = entityManager.getDungeonEntity(entity.getUniqueId());
        if (dungeonEntity == null) return;

        dungeonEntity.handleEvent(event, eventContext);
    }
}