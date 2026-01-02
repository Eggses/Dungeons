package me.Eggses.dungeons;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dispatch.EventManagerRegistry;
import me.Eggses.dungeons.commandsOLD.DungeonsBaseCommand;
import me.Eggses.dungeons.dungeon.files.DungeonLog;
import me.Eggses.dungeons.dungeon.lifecycle.*;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.InstanceNameManager;
import me.Eggses.dungeons.tasks.TaskRunner;
import me.Eggses.dungeons.items.ItemKey;
import me.Eggses.dungeons.listeners.entities.Combustion;
import me.Eggses.dungeons.listeners.entities.EntityCombat;
import me.Eggses.dungeons.listeners.entities.EntityDeath;
import me.Eggses.dungeons.listeners.entities.EntityExplode;
import me.Eggses.dungeons.listeners.players.Login;
import me.Eggses.dungeons.listeners.players.dungeonchanges.DeathController;
import me.Eggses.dungeons.listeners.players.dungeonchanges.PlayerDungeonWorld;
import me.Eggses.dungeons.listeners.blocks.PlayerInteract;
import me.Eggses.dungeons.listeners.players.dungeonchanges.PlayerMovement;
import me.Eggses.dungeons.listeners.players.bans.*;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Dungeons extends JavaPlugin {

    DungeonInstanceTemplateRegistry dungeonInstanceTemplateRegistry = new DungeonInstanceTemplateRegistry();
    DungeonRegistry dungeonRegistry = new DungeonRegistry();
    DungeonOpenPortalRegistry dungeonOpenPortalRegistry = new DungeonOpenPortalRegistry();

    DungeonLog dungeonLog = new DungeonLog(this);
    InstanceNameManager instanceNameManager = new InstanceNameManager(dungeonLog);

    DungeonWorldManager dungeonWorldManager = new DungeonWorldManager(this, instanceNameManager);
    TemplateReservation templateReservation = new TemplateReservation();
    DungeonLifecycleService dungeonLifecycleService = new DungeonLifecycleService(
            this, dungeonRegistry, dungeonOpenPortalRegistry, templateReservation, dungeonWorldManager, dungeonLog
    );

    TaskRunner taskRunner = new TaskRunner(this);

    DungeonEventRouter dungeonEventRouter = new DungeonEventRouter(dungeonRegistry, dungeonOpenPortalRegistry);
    BlockRegistry blockRegistry = new BlockRegistry(taskRunner);
    EventManagerRegistry<String> itemRegistry = new EventManagerRegistry<>();
    ItemKey itemKey = new ItemKey(this);

    ConfigurationFile messagesFile = new ConfigurationFile(this, "messages.yml");
    ConfigurationFile menuFile = new ConfigurationFile(this, "menus.yml");

    MessageCreator messageCreator = new MessageCreator(messagesFile);
    TextFormatter textFormatter = new TextFormatter();

    DungeonFactory dungeonFactory = new DungeonFactory(
            this,
            dungeonRegistry,
            dungeonInstanceTemplateRegistry,
            templateReservation,
            dungeonLifecycleService,
            dungeonWorldManager,
            blockRegistry,
            instanceNameManager,
            taskRunner,
            messageCreator,
            textFormatter,
            dungeonLog,
            new BannedItems(messageCreator, textFormatter)
    );

    @Override
    public void onEnable() {

        var pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new Combustion(dungeonRegistry), this);
        pluginManager.registerEvents(new EntityCombat(dungeonEventRouter), this);
        pluginManager.registerEvents(new EntityDeath(dungeonEventRouter), this);
        pluginManager.registerEvents(new EntityExplode(dungeonEventRouter), this);

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
        pluginManager.registerEvents(new PlayerInteract(blockRegistry, itemRegistry, itemKey), this);
        pluginManager.registerEvents(new PlayerMovement(dungeonEventRouter), this);

        var dungeonsBaseCommand = new DungeonsBaseCommand(dungeonRegistry, dungeonEventRouter, messageCreator);
        Objects.requireNonNull(getCommand("dungeons")).setExecutor(dungeonsBaseCommand);
        Objects.requireNonNull(getCommand("dungeons")).setTabCompleter(dungeonsBaseCommand);

        dungeonLifecycleService.destroyLeftAllInstanceWorlds();
    }

    @Override
    public void onDisable() {
        dungeonRegistry.endAllInstances(false);
    }
}