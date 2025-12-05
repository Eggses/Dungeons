package me.Eggses.dungeons.dungeonentity.mobs;

import me.Eggses.dungeons.dungeonentity.TaskManager;
import me.Eggses.dungeons.taskbehaviour.ActiveEntityTasks;
import me.Eggses.dungeons.taskbehaviour.EntityTaskBehaviour;
import me.Eggses.dungeons.eventbehaviour.EntityEventBehaviour;
import me.Eggses.dungeons.equipment.EquipmentManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class DungeonMob implements DungeonEntity {

    private final LivingEntity entity;
    private final EntityEventBehaviour entityEventBehaviour;
    private final EntityTaskBehaviour entityTaskBehaviour;
    private final ActiveEntityTasks activeEntityTasks = new ActiveEntityTasks();

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

        // set level attributes then do this probably...
        //
        mobBuilder.getOnSpawn().accept(entity);
    }

    public void configureAttributes() {
        // take the dungoen level
    }

    public void startTasks(TaskManager taskManager) {
        activeEntityTasks.addAndRunTasks(entityTaskBehaviour, entity, taskManager);
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
