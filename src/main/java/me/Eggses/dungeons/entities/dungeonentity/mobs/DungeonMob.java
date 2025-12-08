package me.Eggses.dungeons.entities.dungeonentity.mobs;

import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.entities.dungeonentity.TaskManager;
import me.Eggses.dungeons.entities.nameutility.NameFormatter;
import me.Eggses.dungeons.entities.taskbehaviour.ActiveEntityTasks;
import me.Eggses.dungeons.entities.taskbehaviour.EntityTaskBehaviour;
import me.Eggses.dungeons.entities.eventbehaviour.EntityEventBehaviour;
import me.Eggses.dungeons.entities.equipment.EquipmentManager;
import me.Eggses.dungeons.utility.MessageCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class DungeonMob implements DungeonEntity {

    private final LivingEntity entity;
    private final EntityEventBehaviour entityEventBehaviour;
    private final EntityTaskBehaviour entityTaskBehaviour;
    private final ActiveEntityTasks activeEntityTasks = new ActiveEntityTasks();
    private final AttributeController attributeController = new AttributeController(this);
    private final NameFormatter nameFormatter;

    private final MessageCreator messageCreator;
    private final int dungeonLevel;
    private final MobName mobName;

    private Component name;

    public DungeonMob(MobBuilder mobBuilder, MessageCreator messageCreator) {

        Location location = mobBuilder.getLocation();
        World world = location.getWorld();
        entity = world.spawn(location, mobBuilder.getEntityType());
        entity.setPersistent(true);

        this.messageCreator = messageCreator;
        this.dungeonLevel = mobBuilder.getDungeonLevel();
        this.entityEventBehaviour = mobBuilder.getEntityEventBehaviour();
        this.entityTaskBehaviour = mobBuilder.getEntityTaskBehaviour();
        this.mobName = mobBuilder.getMobName();
        this.nameFormatter = new NameFormatter(this, messageCreator);

        EquipmentManager equipmentManager = new EquipmentManager(entity);
        equipmentManager.setEquipment(mobBuilder.getWeaponEquipment(), mobBuilder.getArmourEquipment());

        attributeController.applyAttributes();

        AttributeInstance attributeInstance = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attributeInstance != null) {
            entity.setHealth(attributeInstance.getValue());
        }

        updateName();

        mobBuilder.getOnSpawn().accept(entity);
    }

    public void startTasks(TaskManager taskManager) {
        activeEntityTasks.addAndRunTasks(entityTaskBehaviour, entity, taskManager);
    }

    public int getDungeonLevel() {
        return dungeonLevel;
    }

    public EntityEventBehaviour getEntityEventBehaviour() {
        return entityEventBehaviour;
    }

    public AttributeController getAttributeController() {
        return attributeController;
    }

    @Override
    public LivingEntity getEntity() {
        return entity;
    }

    @Override
    public MobName getMobName() {
        return null;
    }


    @Override
    public void updateName() {
        AttributeInstance attributeInstance = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attributeInstance != null) {
            int health = (int) attributeInstance.getValue();
            entity.customName(nameFormatter.createName(health));
        }
    }

    @Override
    public UUID getUUID() {
        return entity.getUniqueId();
    }

    @Override
    public void endTasks() {
        activeEntityTasks.clearAllTasks();
    }
}
