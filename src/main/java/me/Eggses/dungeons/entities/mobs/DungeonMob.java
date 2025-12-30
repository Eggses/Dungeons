package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.tasks.running.ActiveTasks;
import me.Eggses.dungeons.tasks.running.TaskManager;
import me.Eggses.dungeons.entities.nameutility.NameFormatter;
import me.Eggses.dungeons.eventhandler.EventManager;
import me.Eggses.dungeons.entities.equipment.EquipmentManager;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class DungeonMob implements DungeonEntity {

    private static final String TEAM_NAME = "DungeonEntities";
    private static final Scoreboard SCOREBOARD = Bukkit.getScoreboardManager().getMainScoreboard();
    private static Team TEAM = SCOREBOARD.getTeam(TEAM_NAME);

    private final LivingEntity entity;

    private final int dungeonLevel;
    private final MobName mobName;
    private final NameFormatter nameFormatter;
    private final EventManager eventManager;

    private final AttributeController attributeController = new AttributeController(this);
    private final ActiveTasks<DungeonEntity> activeEntityTasks = new ActiveTasks<>();

    public DungeonMob(MobBuilder mobBuilder,
                      World world,
                      TaskManager taskManager,
                      MessageCreator messageCreator,
                      TextFormatter textFormatter) {

        // Spawn Entity
        Position position = mobBuilder.getPosition();

        Entity entitySpawned = world.spawnEntity(position.toLocation(world), mobBuilder.getEntityType());
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
        AttributeInstance attributeInstance = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attributeInstance != null) entity.setHealth(attributeInstance.getValue());

        if (TEAM == null) {
            TEAM = SCOREBOARD.registerNewTeam(TEAM_NAME);
            TEAM.setAllowFriendlyFire(false);
        }
        TEAM.addEntity(entity);

        // Set Name
        updateHealthDisplay(0.0);
        entity.setCustomNameVisible(true);

        // Start Tasks
        activeEntityTasks.addAndRunTasks(this, mobBuilder.getEntityTaskBehaviour(), taskManager);
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
        activeEntityTasks.endAllTasks();
    }

    @Override
    public <E extends Event> void handleEvent(E event, EventContext eventContext) {
        eventManager.handleEvent(event, eventContext);
    }

    @Override
    public AttributeController getAttributeController() {
        return attributeController;
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
    public void updateHealthDisplay(double damageToBeTaken) {
        int health = (int) (Math.max(0, entity.getHealth() - damageToBeTaken));
        entity.customName(nameFormatter.updateHealth(health));
    }
}