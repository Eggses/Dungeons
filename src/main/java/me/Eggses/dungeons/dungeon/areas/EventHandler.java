package me.Eggses.dungeons.dungeon.areas;

import me.Eggses.dungeons.dungeon.regions.Position;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

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

    public void handleDungeonTriggerCommand(Integer argumentValue) {
        areaController.handleDungeonTriggerCommand(argumentValue);
    }

    public void handleEntityDeathEvent(UUID uuid) {
        areaController.handleEntityDeathEvent(uuid);
    }

    public void handleEntityDamageEntityEvent(EntityDamageByEntityEvent event) {
        switch (event.getCause()) {
            case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK -> handleMeleeDamage(event);
            case PROJECTILE -> handleProjectileDamage(event);
            case ENTITY_EXPLOSION -> handleEntityExplosionDamage(event);
            case MAGIC -> handleMagicDamage(event);
            default -> {}
        }
    }

    private void handleMeleeDamage(EntityDamageByEntityEvent event) {
        // TODO
    }

    private void handleProjectileDamage(EntityDamageByEntityEvent event) {
        //event.getEntity().getCaster

        // TODO
    }

    private void handleEntityExplosionDamage(EntityDamageByEntityEvent event) {
        // TODO




    }

    private void handleMagicDamage(EntityDamageByEntityEvent event) {
        // TODO



    }
}
