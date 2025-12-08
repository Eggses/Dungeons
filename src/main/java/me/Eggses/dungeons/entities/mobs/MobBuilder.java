package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.entities.taskbehaviour.EntityTaskBehaviour;
import me.Eggses.dungeons.entities.eventbehaviour.EntityEventBehaviour;
import me.Eggses.dungeons.entities.equipment.ArmourEquipment;
import me.Eggses.dungeons.entities.equipment.WeaponEquipment;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.function.Consumer;

public class MobBuilder {

    private Class<? extends LivingEntity> entityType;
    private Location location;
    private int dungeonLevel = 1;
    private WeaponEquipment weaponEquipment = new WeaponEquipment();
    private ArmourEquipment armourEquipment = new ArmourEquipment();
    private EntityEventBehaviour entityEventBehaviour = new EntityEventBehaviour();
    private EntityTaskBehaviour entityTaskBehaviour = new EntityTaskBehaviour();
    private Consumer<LivingEntity> spawnFinalizer = (entity) -> {};
    private int count = 1;
    private MobName mobName = new MobName();

    public MobBuilder(Class<? extends LivingEntity> entityType, Location location) {
        this.entityType = entityType;
        this.location = location;
    }

    // Builders

    public MobBuilder entityType(Class<? extends LivingEntity> entityType) {
        this.entityType = entityType;
        return this;
    }

    public MobBuilder location(Location location) {
        this.location = location;
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

    public MobBuilder entityEventBehaviour(EntityEventBehaviour entityEventBehaviour) {
        this.entityEventBehaviour = entityEventBehaviour;
        return this;
    }

    public MobBuilder entityTaskBehaviour(EntityTaskBehaviour entityTaskBehaviour) {
        this.entityTaskBehaviour = entityTaskBehaviour;
        return this;
    }

    public MobBuilder spawnFinalizer(Consumer<LivingEntity> spawnFinalizer) {
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

    // Getters

    public Class<? extends LivingEntity> getEntityType() {
        return entityType;
    }

    public Location getLocation() {
        return location;
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

    public Consumer<LivingEntity> getSpawnFinalizer() {
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