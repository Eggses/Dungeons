package me.Eggses.dungeons.entities.dungeonentity.mobs;

import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.entities.dungeonentity.TaskManager;
import me.Eggses.dungeons.entities.nameutility.NameFormatter;
import me.Eggses.dungeons.entities.eventbehaviour.EntityEventBehaviour;
import me.Eggses.dungeons.entities.equipment.EquipmentManager;
import me.Eggses.dungeons.entities.taskbehaviour.ActiveEntityTasks;
import me.Eggses.dungeons.utility.MessageCreator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class DungeonMob implements DungeonEntity {

    private final LivingEntity entity;

    private final int dungeonLevel;
    private final MobName mobName;
    private final NameFormatter nameFormatter;
    private final EntityEventBehaviour entityEventBehaviour;

    private final AttributeController attributeController = new AttributeController(this);
    private final ActiveEntityTasks activeEntityTasks = new ActiveEntityTasks();

    public DungeonMob(MobBuilder mobBuilder, TaskManager taskManager, MessageCreator messageCreator) {

        // Spawn Entity
        Location location = mobBuilder.getLocation();
        World world = location.getWorld();
        entity = world.spawn(location, mobBuilder.getEntityType());

        // Set Instance Fields
        this.dungeonLevel = mobBuilder.getDungeonLevel();
        this.nameFormatter = new NameFormatter(this, messageCreator);
        this.mobName = mobBuilder.getMobName();
        this.entityEventBehaviour = mobBuilder.getEntityEventBehaviour();

        // Apply Armour
        EquipmentManager equipmentManager = new EquipmentManager(this);
        equipmentManager.setEquipment(mobBuilder.getWeaponEquipment(), mobBuilder.getArmourEquipment());

        // Apply Attributes
        attributeController.applyAttributes();

        // Update Mob
        entity.setPersistent(true);
        AttributeInstance attributeInstance = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attributeInstance != null) {
            entity.setHealth(attributeInstance.getValue());
        }

        // Set Name
        updateName();

        // Start Tasks
        activeEntityTasks.addAndRunTasks(mobBuilder.getEntityTaskBehaviour(), this, taskManager);

        // Finally
        mobBuilder.getSpawnFinalizer().accept(entity);
    }

    public AttributeController getAttributeController() {
        return attributeController;
    }

    @Override
    public UUID getUUID() {
        return entity.getUniqueId();
    }

    @Override
    public LivingEntity getEntity() {
        return entity;
    }

    @Override
    public void endTasks() {
        activeEntityTasks.clearAllTasks();
    }

    @Override
    public EntityEventBehaviour getEntityEventBehaviour() {
        return entityEventBehaviour;
    }

    @Override
    public int getDungeonLevel() {
        return dungeonLevel;
    }

    @Override
    public MobName getMobName() {
        return mobName;
    }

    @Override
    public void updateName() {
        AttributeInstance attributeInstance = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attributeInstance != null) {
            int health = (int) attributeInstance.getValue();
            entity.customName(nameFormatter.createName(health));
        }
    }
}