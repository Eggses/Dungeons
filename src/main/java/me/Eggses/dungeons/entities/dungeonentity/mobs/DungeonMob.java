package me.Eggses.dungeons.entities.dungeonentity.mobs;

import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.entities.dungeonentity.TaskManager;
import me.Eggses.dungeons.entities.taskbehaviour.ActiveEntityTasks;
import me.Eggses.dungeons.entities.taskbehaviour.EntityTaskBehaviour;
import me.Eggses.dungeons.entities.eventbehaviour.EntityEventBehaviour;
import me.Eggses.dungeons.entities.equipment.EquipmentManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DungeonMob implements DungeonEntity {

    private final LivingEntity entity;
    private final EntityEventBehaviour entityEventBehaviour;
    private final EntityTaskBehaviour entityTaskBehaviour;
    private final ActiveEntityTasks activeEntityTasks = new ActiveEntityTasks();
    private final AttributeController attributeController = new AttributeController();

    private final int dungeonLevel;
    private final MobName mobName;

    public DungeonMob(MobBuilder mobBuilder) {

        Location location = mobBuilder.getLocation();
        World world = location.getWorld();
        entity = world.spawn(location, mobBuilder.getEntityType());

        this.dungeonLevel = mobBuilder.getDungeonLevel();
        this.entityEventBehaviour = mobBuilder.getEntityEventBehaviour();
        this.entityTaskBehaviour = mobBuilder.getEntityTaskBehaviour();
        this.mobName = mobBuilder.getMobName();

        EquipmentManager equipmentManager = new EquipmentManager(entity);
        equipmentManager.setEquipment(mobBuilder.getWeaponEquipment(), mobBuilder.getArmourEquipment());

        attributeController.applyAttributes(this);

        mobBuilder.getOnSpawn().accept(entity);
    }

    public void startTasks(TaskManager taskManager) {
        activeEntityTasks.addAndRunTasks(entityTaskBehaviour, entity, taskManager);
    }

    public int getDungeonLevel() {
        return dungeonLevel;
    }


    @Override
    public LivingEntity getEntity() {
        return entity;
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
