package me.Eggses.dungeons;

import me.Eggses.dungeons.commands.DungeonTrigger;
import me.Eggses.dungeons.dungeon.files.DungeonLog;
import me.Eggses.dungeons.dungeon.lifecycle.*;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.InstanceNameManager;
import me.Eggses.dungeons.entities.taskbehaviour.TaskManager;
import me.Eggses.dungeons.listeners.entities.Combustion;
import me.Eggses.dungeons.listeners.entities.EntityCombat;
import me.Eggses.dungeons.listeners.entities.EntityDeath;
import me.Eggses.dungeons.listeners.players.Login;
import me.Eggses.dungeons.listeners.players.dungeonchanges.DeathController;
import me.Eggses.dungeons.listeners.players.dungeonchanges.PlayerDungeonWorld;
import me.Eggses.dungeons.listeners.players.dungeonchanges.PlayerInteract;
import me.Eggses.dungeons.listeners.players.dungeonchanges.PlayerMovement;
import me.Eggses.dungeons.listeners.players.itemban.*;
import me.Eggses.dungeons.utility.MessageCreator;
import me.Eggses.dungeons.utility.SoundPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Dungeons extends JavaPlugin {


    DungeonRegistry dungeonRegistry = new DungeonRegistry();
    DungeonOpenPortalRegistry dungeonOpenPortalRegistry = new DungeonOpenPortalRegistry();
    DungeonEventRouter dungeonEventRouter = new DungeonEventRouter(dungeonRegistry, dungeonOpenPortalRegistry);

    DungeonLog dungeonLog = new DungeonLog(this);
    InstanceNameManager instanceNameManager = new InstanceNameManager(dungeonLog);
    DungeonWorldManager dungeonWorldManager = new DungeonWorldManager(this, instanceNameManager);
    DungeonInstanceCoordinator dungeonInstanceCoordinator = new DungeonLifecycleService(this, dungeonRegistry, dungeonOpenPortalRegistry, dungeonWorldManager, dungeonLog);

    TaskManager taskManager = new TaskManager(this);
    MessageCreator messageCreator = new MessageCreator();
    BannedItems bannedItems = new BannedItems(messageCreator);
    SoundPlayer soundPlayer = new SoundPlayer();

    DungeonFactory dungeonFactory = new DungeonFactory(this, dungeonRegistry, dungeonInstanceCoordinator, dungeonWorldManager, instanceNameManager,taskManager, messageCreator, soundPlayer, dungeonLog, bannedItems);

    @Override
    public void onEnable() {

        var pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new Combustion(dungeonRegistry), this);
        pluginManager.registerEvents(new EntityCombat(dungeonEventRouter), this);
        pluginManager.registerEvents(new EntityDeath(dungeonEventRouter), this);

        pluginManager.registerEvents(new Login(), this);
        pluginManager.registerEvents(new ChorusFruitTeleport(dungeonRegistry), this);
        pluginManager.registerEvents(new DurabilityLoss(dungeonRegistry), this);
        pluginManager.registerEvents(new ElytraGlide(dungeonRegistry), this);
        pluginManager.registerEvents(new FrostWalkerFreeze(dungeonRegistry), this);
        pluginManager.registerEvents(new ItemUse(dungeonRegistry), this);
        pluginManager.registerEvents(new MendingProfit(dungeonRegistry), this);
        pluginManager.registerEvents(new PlayerLaunchProjectile(dungeonRegistry), this);
        pluginManager.registerEvents(new PotionEffectGain(dungeonRegistry), this);
        pluginManager.registerEvents(new ShieldCooldown(this, dungeonRegistry), this);
        pluginManager.registerEvents(new TotemUse(dungeonRegistry), this);

        pluginManager.registerEvents(new DeathController(dungeonRegistry, dungeonEventRouter), this);
        pluginManager.registerEvents(new PlayerDungeonWorld(dungeonEventRouter), this);
        pluginManager.registerEvents(new PlayerInteract(dungeonEventRouter), this);
        pluginManager.registerEvents(new PlayerMovement(dungeonEventRouter), this);

        Objects.requireNonNull(getCommand("dungeontrigger")).setExecutor(new DungeonTrigger(dungeonEventRouter));


        dungeonInstanceCoordinator.destroyLeftAllInstanceWorlds();

        dungeonFactory.createDungeon(DungeonType.FLAT_TEST);
    }

    @Override
    public void onDisable() {
        dungeonInstanceCoordinator.endAllInstances(false);
    }
}