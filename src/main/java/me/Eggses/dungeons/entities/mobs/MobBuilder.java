package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.entities.tasks.EntityTask;
import me.Eggses.dungeons.entities.tasks.EntityTaskBehaviour;
import me.Eggses.dungeons.eventhandler.EventManager;
import me.Eggses.dungeons.entities.equipment.ArmourEquipment;
import me.Eggses.dungeons.entities.equipment.WeaponEquipment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;

import java.util.function.Consumer;

public class MobBuilder {

    private EntityType entityType;
    private Position position;
    private int dungeonLevel = 1;
    private WeaponEquipment weaponEquipment = new WeaponEquipment();
    private ArmourEquipment armourEquipment = new ArmourEquipment();
    private final EventManager eventManager = new EventManager();
    private final EntityTaskBehaviour entityTaskBehaviour = new EntityTaskBehaviour();
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

    public <E extends Event> MobBuilder eventBehaviour(Class<E> eventClass, EventBehaviour<E> eventBehaviour) {
        this.eventManager.addEventBehaviour(eventClass, eventBehaviour);
        return this;
    }

    public MobBuilder entityTask(EntityTask entityTask) {
        this.entityTaskBehaviour.addEntityTask(entityTask);
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

    public MobBuilder applyPreset(MobType mobType) {
        if (mobType == null) return this;
        Consumer<MobBuilder> mobBuilderConsumer = mobType.getMobBuilder();
        if (mobBuilderConsumer == null) return this;
        mobBuilderConsumer.accept(this);
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

    public EventManager getEntityEventBehaviour() {
        return eventManager;
    }

    public Consumer<DungeonEntity> getSpawnChanges() {
        return spawnChanges;
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