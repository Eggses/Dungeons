package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.eventhandler.*;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.tasks.ActiveTasks;
import me.Eggses.dungeons.tasks.Task;
import me.Eggses.dungeons.tasks.TaskContext;
import me.Eggses.dungeons.tasks.TaskRunner;
import me.Eggses.dungeons.entities.nameutility.NameFormatter;
import me.Eggses.dungeons.entities.equipment.EquipmentManager;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.UUID;

public class DungeonMob implements DungeonEntity {

    private static final String TEAM_NAME = "DungeonEntities";
    private static final Scoreboard SCOREBOARD = Bukkit.getScoreboardManager().getMainScoreboard();
    private static Team TEAM = SCOREBOARD.getTeam(TEAM_NAME);

    private static final PotionEffect DOLPHINS_GRACE = new PotionEffect(
            PotionEffectType.DOLPHINS_GRACE,
            Integer.MAX_VALUE,
            49,
            false,
            false,
            false
    );

    private final LivingEntity entity;

    private final int dungeonLevel;
    private final MobName mobName;
    private final NameFormatter nameFormatter;
    private final EventManager eventManager;

    private final AttributeController attributeController = new AttributeController(this);
    private final ActiveTasks activeTasks = new ActiveTasks();

    public DungeonMob(MobBuilder mobBuilder,
                      World world,
                      TaskRunner taskRunner,
                      MessageCreator messageCreator,
                      TextFormatter textFormatter) {

        // Spawn Entity
        Location location = mobBuilder.getRotationPosition().toLocation(world);

        Entity entitySpawned = world.spawnEntity(location, mobBuilder.getEntityType());
        if (!(entitySpawned instanceof LivingEntity livingEntity)) throw new IllegalArgumentException("Tried to Spawn a Non-Living Mob!");
        this.entity = livingEntity;

        // Set Instance Fields
        this.dungeonLevel = mobBuilder.getDungeonLevel();
        this.mobName = mobBuilder.getMobName();
        this.nameFormatter = new NameFormatter(this, messageCreator, textFormatter);
        this.eventManager = mobBuilder.getEntityEventBehaviour();

        // Apply Armour
        EquipmentManager equipmentManager = new EquipmentManager(this);
        equipmentManager.setEquipment(mobBuilder.getWeaponEquipment(), mobBuilder.getArmourEquipment());

        // Spawn Changes
        mobBuilder.getSpawnChanges().accept(this);

        // Apply Attributes
        attributeController.applyAttributes();

        // Update Mob
        entity.setPersistent(true);
        entity.setRemoveWhenFarAway(false);

        AttributeInstance attributeInstance = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attributeInstance != null) entity.setHealth(attributeInstance.getValue());

        if (TEAM == null) {
            TEAM = SCOREBOARD.registerNewTeam(TEAM_NAME);
            TEAM.setAllowFriendlyFire(false);
        }
        TEAM.addEntity(entity);

        // Set Name
        takeDamage(0.0);
        entity.setCustomNameVisible(true);

        // Start Tasks
        List<Task<DungeonEntity>> tasks = mobBuilder.getEntityTaskBehaviour();
        TaskContext<DungeonEntity> taskContext = new TaskContext<>(this, activeTasks, taskRunner);
        for (Task<DungeonEntity> task : tasks) {
            task.runTask(taskContext);
        }

        // Mount
        EntityType typeOfMount = mobBuilder.getMountType();
        if (typeOfMount != null) {

            Entity mountSpawned = world.spawnEntity(location, typeOfMount);
            if (!(mountSpawned instanceof LivingEntity livingMount)) throw new IllegalArgumentException("Tried to Spawn a Non-Living Mob for a Mount!");
            livingMount.setPersistent(true);
            livingMount.addPotionEffect(DOLPHINS_GRACE);

            livingMount.addPassenger(entity);
        }

        // Potion
        if (!(livingEntity instanceof Creeper)) entity.addPotionEffect(DOLPHINS_GRACE);
    }

    public ActiveTasks getActiveTasks() {
        return activeTasks;
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
        activeTasks.endAllTasks();
    }

    @Override
    public AttributeController getAttributeController() {
        return attributeController;
    }

    @Override
    public <E extends Event> void addEvent(Class<E> eventClass, EventBehaviour<E> eventBehaviour) {
        eventManager.addEventBehaviour(eventClass, eventBehaviour);
    }

    @Override
    public <E extends Event> void handleEvent(E event, EventContext eventContext) {
        eventManager.handleEvent(event, eventContext);
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
    public void takeDamage(double damage) {
        int health = (int) (Math.max(0, entity.getHealth() - damage));
        entity.customName(nameFormatter.updateHealth(health));
    }
}
