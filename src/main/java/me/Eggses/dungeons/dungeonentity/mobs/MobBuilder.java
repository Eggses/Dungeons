package me.Eggses.dungeons.dungeonentity.mobs;

import me.Eggses.dungeons.tasks.EntityTaskBehaviour;
import me.Eggses.dungeons.eventbehaviour.EntityEventBehaviour;
import me.Eggses.dungeons.equipment.ArmourEquipment;
import me.Eggses.dungeons.equipment.WeaponEquipment;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.function.Consumer;

public class MobBuilder {

    private Class<? extends LivingEntity> entityType;
    private Location location;
    private int dungeonLevel = 0;
    private WeaponEquipment weaponEquipment;
    private ArmourEquipment armourEquipment;
    private EntityEventBehaviour entityEventBehaviour;
    private EntityTaskBehaviour entityTaskBehaviour;
    private Consumer<LivingEntity> onSpawn;
    private int count = 1;
    private Component name;
    private String suffix;

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

    public MobBuilder onSpawn(Consumer<LivingEntity> onSpawn) {
        this.onSpawn = onSpawn;
        return this;
    }

    public MobBuilder count(int count) {
        this.count = count;
        return this;
    }

    public MobBuilder name(Component name) {
        this.name = name;
        return this;
    }

    public MobBuilder suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

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

    public Consumer<LivingEntity> getOnSpawn() {
        return onSpawn;
    }

    public EntityTaskBehaviour getEntityTaskBehaviour() {
        return entityTaskBehaviour;
    }

    public int getCount() {
        return count;
    }

    public Component getName() {
        return name;
    }

    public String getSuffix() {
        return suffix;
    }
}