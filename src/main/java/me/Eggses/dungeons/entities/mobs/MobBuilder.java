package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.entities.eventbehaviour.EventBehaviour;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.entities.taskbehaviour.EntityTask;
import me.Eggses.dungeons.entities.taskbehaviour.EntityTaskBehaviour;
import me.Eggses.dungeons.entities.eventbehaviour.EntityEventBehaviour;
import me.Eggses.dungeons.entities.equipment.ArmourEquipment;
import me.Eggses.dungeons.entities.equipment.WeaponEquipment;
import org.bukkit.entity.EntityType;

import java.util.function.Consumer;

public class MobBuilder {

    private EntityType entityType;
    private Position position;
    private int dungeonLevel = 1;
    private WeaponEquipment weaponEquipment = new WeaponEquipment();
    private ArmourEquipment armourEquipment = new ArmourEquipment();
    private final EntityEventBehaviour entityEventBehaviour = new EntityEventBehaviour();
    private EntityTaskBehaviour entityTaskBehaviour = new EntityTaskBehaviour();
    private Consumer<DungeonEntity> spawnFinalizer = (entity) -> {};
    private int count = 1;
    private MobName mobName = new MobName();

    public MobBuilder(EntityType entityType, Position position) {
        this.entityType = entityType;
        this.position = position;
    }

    // Builders

    public MobBuilder entityType(EntityType entityType) {
        this.entityType = entityType;
        return this;
    }

    public MobBuilder position(Position position) {
        this.position = position;
        return this;
    }

    public MobBuilder dungeonLevel(int dungeonLevel) {
        this.dungeonLevel = dungeonLevel;
        return this;
    }

    public MobBuilder weaponEquipment(WeaponEquipment weaponEquipment) {
        this.weaponEquipment = weaponEquipment;
        return this;
    }

    public MobBuilder armourEquipment(ArmourEquipment armourEquipment) {
        this.armourEquipment = armourEquipment;
        return this;
    }

    public MobBuilder eventBehaviour(EventBehaviour<?> eventBehaviour) {
        this.entityEventBehaviour.addEventBehaviour(eventBehaviour);
        return this;
    }

    public MobBuilder entityTask(EntityTask entityTask) {
        this.entityTaskBehaviour.addEntityTask(entityTask);
        return this;
    }

    public MobBuilder spawnFinalizer(Consumer<DungeonEntity> spawnFinalizer) {
        this.spawnFinalizer = spawnFinalizer;
        return this;
    }

    public MobBuilder count(int count) {
        this.count = count;
        return this;
    }

    public MobBuilder mobName(MobName mobName) {
        this.mobName = mobName;
        return this;
    }

    public MobBuilder mobNameSpawnFinalizerTaskBehaviour(MobType mobType) {
        if (mobType == null) return this;
        this.mobName = mobType.getMobName();
        this.spawnFinalizer = mobType.getSpawnFinalizer();
        this.entityTaskBehaviour = mobType.getEntityTaskBehaviour();
        return this;
    }

    // Getters

    public EntityType getEntityType() {
        return entityType;
    }

    public Position getPosition() {
        return position;
    }

    public int getDungeonLevel() {
        return dungeonLevel;
    }

    public WeaponEquipment getWeaponEquipment() {
        return weaponEquipment;
    }

    public ArmourEquipment getArmourEquipment() {
        return armourEquipment;
    }

    public EntityEventBehaviour getEntityEventBehaviour() {
        return entityEventBehaviour;
    }

    public Consumer<DungeonEntity> getSpawnFinalizer() {
        return spawnFinalizer;
    }

    public EntityTaskBehaviour getEntityTaskBehaviour() {
        return entityTaskBehaviour;
    }

    public int getCount() {
        return count;
    }

    public MobName getMobName() {
        return mobName;
    }
}