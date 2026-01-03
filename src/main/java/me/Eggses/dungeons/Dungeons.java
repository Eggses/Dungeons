package me.Eggses.dungeons;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.commands.DungeonsBaseCommand;
import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dispatch.EventManagerRegistry;
import me.Eggses.dungeons.dungeon.files.DungeonLog;
import me.Eggses.dungeons.dungeon.files.reading.ReadingUtility;
import me.Eggses.dungeons.dungeon.items.DungeonKeys;
import me.Eggses.dungeons.dungeon.lifecycle.*;
import me.Eggses.dungeons.dungeon.portalroom.DungeonPortalRoomRegistry;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.InstanceNameManager;
import me.Eggses.dungeons.entities.mobs.mobtype.MobRegistry;
import me.Eggses.dungeons.entities.mobs.mobtype.MobUtility;
import me.Eggses.dungeons.items.ItemCreator;
import me.Eggses.dungeons.items.ItemGive;
import me.Eggses.dungeons.tasks.TaskRunner;
import me.Eggses.dungeons.items.ItemKeyManager;
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
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Dungeons extends JavaPlugin {

    private final DungeonRegistry dungeonRegistry = new DungeonRegistry();

    @Override
    public void onEnable() {

        var messagesFile = new ConfigurationFile(this, "messages.yml");
        var menuFile = new ConfigurationFile(this, "menus.yml");
        var messageCreator = new MessageCreator(messagesFile);
        var textFormatter = new TextFormatter();

        var dungeonInstanceTemplateRegistry = new DungeonInstanceTemplateRegistry();
        var dungeonOpenPortalRegistry = new DungeonOpenPortalRegistry();
        var itemRegistry = new EventManagerRegistry<String>();
        var templateReservation = new TemplateReservation();

        var dungeonLog = new DungeonLog(this);
        var instanceNameManager = new InstanceNameManager(dungeonLog);
        var dungeonWorldManager = new DungeonWorldManager(this, instanceNameManager);
        var taskRunner = new TaskRunner(this);
        var blockRegistry = new BlockRegistry(taskRunner);

        var dungeonLifecycleService = new DungeonLifecycleService(
                this, dungeonRegistry, dungeonOpenPortalRegistry, templateReservation, dungeonWorldManager, dungeonLog
        );

        var dungeonEventRouter = new DungeonEventRouter(dungeonRegistry, dungeonOpenPortalRegistry);

        var soundPlayer = new SoundPlayer();
        var mobRegistry = new MobRegistry(this, new MobUtility(), textFormatter, soundPlayer);
        var dungeonPortalRoomRegistry = new DungeonPortalRoomRegistry();

        var bannedItems = new BannedItems(messageCreator, textFormatter);

        var itemKeyManager = new ItemKeyManager(this);
        var itemCreator = new ItemCreator(itemKeyManager, messageCreator);
        var dungeonKeys = new DungeonKeys();
        var itemGive = new ItemGive();

        var dungeonFactory = new DungeonFactory(
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
                bannedItems
        );

        var dungeonLoadingManager = new DungeonLoadingManager(
                this,
                new ReadingUtility(),
                messageCreator,
                soundPlayer,
                mobRegistry,
                dungeonPortalRoomRegistry,
                blockRegistry,
                dungeonKeys,
                itemRegistry,
                dungeonInstanceTemplateRegistry
        );

        registerListeners(dungeonEventRouter, blockRegistry, itemRegistry, itemKeyManager, dungeonRegistry);
        registerCommands(dungeonRegistry, dungeonEventRouter, dungeonLoadingManager, dungeonKeys, itemCreator, itemGive, messagesFile, menuFile, messageCreator, textFormatter);

        dungeonLifecycleService.destroyLeftAllInstanceWorlds();
        dungeonLoadingManager.loadAllDungeonsOnEnable();
    }

    private void registerListeners(DungeonEventRouter dungeonEventRouter,
                                   BlockRegistry blockRegistry,
                                   EventManagerRegistry<String> itemRegistry,
                                   ItemKeyManager itemKeyManager,
                                   DungeonRegistry dungeonRegistry) {

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
        pluginManager.registerEvents(new PlayerInteract(blockRegistry, itemRegistry, itemKeyManager), this);
        pluginManager.registerEvents(new PlayerMovement(dungeonEventRouter), this);
    }

    private void registerCommands(DungeonRegistry dungeonRegistry,
                                  DungeonEventRouter dungeonEventRouter,
                                  DungeonLoadingManager dungeonLoadingManager,
                                  DungeonKeys dungeonKeys,
                                  ItemCreator itemCreator,
                                  ItemGive itemGive,
                                  ConfigurationFile messagesFile,
                                  ConfigurationFile menuFile,
                                  MessageCreator messageCreator,
                                  TextFormatter textFormatter) {

        var dungeonsBaseCommand = new DungeonsBaseCommand(
                dungeonRegistry,
                dungeonEventRouter,
                dungeonLoadingManager,
                dungeonKeys,
                itemCreator,
                itemGive,
                messagesFile,
                menuFile,
                messageCreator,
                textFormatter
        );

        Objects.requireNonNull(getCommand("dungeons")).setExecutor(dungeonsBaseCommand);
        Objects.requireNonNull(getCommand("dungeons")).setTabCompleter(dungeonsBaseCommand);
    }


    @Override
    public void onDisable() {
        dungeonRegistry.endAllInstances(false);
    }
}
