package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.entities.taskbehaviour.TaskManager;
import me.Eggses.dungeons.entities.nameutility.NameFormatter;
import me.Eggses.dungeons.entities.eventbehaviour.EntityEventBehaviour;
import me.Eggses.dungeons.entities.equipment.EquipmentManager;
import me.Eggses.dungeons.entities.taskbehaviour.ActiveEntityTasks;
import me.Eggses.dungeons.utility.MessageCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
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
    private final EntityEventBehaviour entityEventBehaviour;

    private final AttributeController attributeController = new AttributeController(this);
    private final ActiveEntityTasks activeEntityTasks = new ActiveEntityTasks();

    public DungeonMob(MobBuilder mobBuilder, TaskManager taskManager, MessageCreator messageCreator) {

        // Spawn Entity
        Location location = mobBuilder.getLocation();
        World world = location.getWorld();
        entity = world.spawn(location, mobBuilder.getEntityType());

        // Set Instance Fields
        this.dungeonLevel = mobBuilder.getDungeonLevel();
        this.nameFormatter = new NameFormatter(this, messageCreator);
        this.mobName = mobBuilder.getMobName();
        this.entityEventBehaviour = mobBuilder.getEntityEventBehaviour();

        // Apply Armour
        EquipmentManager equipmentManager = new EquipmentManager(this);
        equipmentManager.setEquipment(mobBuilder.getWeaponEquipment(), mobBuilder.getArmourEquipment());

        // Apply Attributes
        attributeController.applyAttributes();

        // Update Mob
        entity.setPersistent(true);
        AttributeInstance attributeInstance = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attributeInstance != null) {
            entity.setHealth(attributeInstance.getValue());
        }
        if (TEAM == null) {
            TEAM = SCOREBOARD.registerNewTeam(TEAM_NAME);
            TEAM.setAllowFriendlyFire(false);
        }
        TEAM.addEntity(entity);

        // Set Name
        updateName();

        // Start Tasks
        activeEntityTasks.addAndRunTasks(mobBuilder.getEntityTaskBehaviour(), this, taskManager);

        // Finally
        mobBuilder.getSpawnFinalizer().accept(this);
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
        activeEntityTasks.clearAllTasks();
    }

    @Override
    public EntityEventBehaviour getEntityEventBehaviour() {
        return entityEventBehaviour;
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

    @Override @Deprecated
    public void updateName() {
        // this is the wrong health!!! helath event doesnt apply damage
        int health = (int) entity.getHealth();
        entity.customName(nameFormatter.createName(health));
    }
}