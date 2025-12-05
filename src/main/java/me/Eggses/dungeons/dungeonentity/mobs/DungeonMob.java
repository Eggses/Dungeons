package me.Eggses.dungeons.dungeonentity.mobs;

import me.Eggses.dungeons.dungeonentity.TaskManager;
import me.Eggses.dungeons.tasks.ActiveEntityTasks;
import me.Eggses.dungeons.tasks.EntityTaskBehaviour;
import me.Eggses.dungeons.eventbehaviour.EntityEventBehaviour;
import me.Eggses.dungeons.equipment.EquipmentManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.Optional;
import java.util.UUID;

public class DungeonMob implements DungeonEntity {

    private final LivingEntity entity;
    private final EntityEventBehaviour entityEventBehaviour;
    private final EntityTaskBehaviour entityTaskBehaviour;

    private final ActiveEntityTasks activeEntityTasks = new ActiveEntityTasks();

    private final int dungeonLevel;

    public DungeonMob(MobBuilder mobBuilder) {

        Location location = mobBuilder.getLocation();
        World world = location.getWorld();
        entity = world.spawn(location, mobBuilder.getEntityType());

        this.dungeonLevel = mobBuilder.getDungeonLevel();

        EquipmentManager equipmentManager = new EquipmentManager(entity);
        equipmentManager.setEquipment(mobBuilder.getWeaponEquipment(), mobBuilder.getArmourEquipment());

        this.entityEventBehaviour = mobBuilder.getEntityEventBehaviour();
        this.entityTaskBehaviour = mobBuilder.getEntityTaskBehaviour();

        mobBuilder.getOnSpawn().accept(entity);

        // private Component name; need maybe
        //private String suffix; need maybe
    }

    public void configureAttributes() {
        // take the dungoen level
    }

    public void startTasks(TaskManager taskManager) {
        if (entityTaskBehaviour == null) return;
        activeEntityTasks.addAndRunTasks(entityTaskBehaviour, taskManager);
    }

    public void endTasks() {
        if (entityTaskBehaviour == null) return;
        activeEntityTasks.clearAllTasks();
    }

    public Optional<EntityEventBehaviour> getEntityEventBehaviour() {
        if (entityTaskBehaviour == null) return Optional.empty();
        return Optional.of(entityEventBehaviour);
    }




    @Override
    public UUID getUUID() {
        return entity.getUniqueId();
    }
}
