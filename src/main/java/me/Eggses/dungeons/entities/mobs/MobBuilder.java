package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.entities.equipment.ArmourEquipment;
import me.Eggses.dungeons.entities.equipment.WeaponEquipment;
import me.Eggses.dungeons.eventinvoker.Invoker;
import me.Eggses.dungeons.tasks.TaskContext;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MobBuilder {

    private EntityType entityType;
    private Position position;
    private int dungeonLevel = 1;
    private WeaponEquipment weaponEquipment = new WeaponEquipment();
    private ArmourEquipment armourEquipment = new ArmourEquipment();
    private final List<Invoker> invokers = new ArrayList<>();
    private final List<Consumer<TaskContext<DungeonEntity>>> taskContextConsumers = new ArrayList<>();
    private Consumer<DungeonEntity> spawnChanges = (entity) -> {};
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

    public MobBuilder invoker(Invoker invoker) {
        this.invokers.add(invoker);
        return this;
    }

    public MobBuilder entityTask(Consumer<TaskContext<DungeonEntity>> entityTask) {
        this.taskContextConsumers.add(entityTask);
        return this;
    }

    public MobBuilder spawnChanges(Consumer<DungeonEntity> spawnChanges) {
        this.spawnChanges = spawnChanges;
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

    public MobBuilder applyPreset(Consumer<MobBuilder> builderPreset) {
        if (builderPreset == null) return this;
        builderPreset.accept(this);
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

    public List<Invoker> getInvokers() {
        return invokers;
    }

    public Consumer<DungeonEntity> getSpawnChanges() {
        return spawnChanges;
    }

    public List<Consumer<TaskContext<DungeonEntity>>> getEntityTaskBehaviour() {
        return taskContextConsumers;
    }

    public int getCount() {
        return count;
    }

    public MobName getMobName() {
        return mobName;
    }
}