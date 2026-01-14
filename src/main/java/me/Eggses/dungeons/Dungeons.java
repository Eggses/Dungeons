package me.Eggses.dungeons;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.commands.DungeonsBaseCommand;
import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dispatch.EventManagerRegistry;
import me.Eggses.dungeons.dungeon.files.DungeonLog;
import me.Eggses.dungeons.dungeon.files.reading.ReadingUtility;
import me.Eggses.dungeons.dungeon.items.DungeonKeyItems;
import me.Eggses.dungeons.dungeon.lifecycle.*;
import me.Eggses.dungeons.dungeon.portalroom.DungeonEntranceRoomRegistry;
import me.Eggses.dungeons.dungeon.portals.OpenPortalRegistry;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.InstanceNameManager;
import me.Eggses.dungeons.entities.mobs.mobtype.MobRegistry;
import me.Eggses.dungeons.entities.mobs.mobtype.MobUtility;
import me.Eggses.dungeons.eventhandler.EventRegistry;
import me.Eggses.dungeons.items.ItemHandler;
import me.Eggses.dungeons.items.ItemKey;
import me.Eggses.dungeons.items.ItemGive;
import me.Eggses.dungeons.tasks.TaskRunner;
import me.Eggses.dungeons.listeners.entities.Combustion;
import me.Eggses.dungeons.listeners.entities.EntityCombat;
import me.Eggses.dungeons.listeners.entities.EntityDeath;
import me.Eggses.dungeons.listeners.entities.EntityExplode;
import me.Eggses.dungeons.listeners.players.Login;
import me.Eggses.dungeons.listeners.players.DeathController;
import me.Eggses.dungeons.listeners.players.PlayerDungeonWorld;
import me.Eggses.dungeons.listeners.blocks.PlayerInteract;
import me.Eggses.dungeons.listeners.players.PlayerMovement;
import me.Eggses.dungeons.listeners.bans.*;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@SuppressWarnings("unused")
public final class Dungeons extends JavaPlugin {

    private final DungeonRegistry dungeonRegistry = new DungeonRegistry();

    @Override
    public void onEnable() {

        var messagesFile = new ConfigurationFile(this, "messages.yml");
        var menuFile = new ConfigurationFile(this, "menus.yml");
        var messageCreator = new MessageCreator(messagesFile);
        var textFormatter = new TextFormatter();

        var dungeonInstanceTemplateRegistry = new DungeonInstanceTemplateRegistry();
        var itemRegistry = new EventManagerRegistry<String>();
        var templateReservation = new TemplateReservation();

        var dungeonLog = new DungeonLog(this);
        var instanceNameManager = new InstanceNameManager(dungeonLog);
        var dungeonWorldManager = new DungeonWorldManager(this, instanceNameManager);
        var taskRunner = new TaskRunner(this);
        var blockRegistry = new BlockRegistry(taskRunner);

        var openPortalRegistry = new OpenPortalRegistry();

        var dungeonLifecycleService = new DungeonLifecycleService(
                this, dungeonRegistry, openPortalRegistry, templateReservation, dungeonWorldManager, dungeonLog
        );

        var dungeonEventRouter = new DungeonEventRouter(dungeonRegistry, openPortalRegistry);

        var soundPlayer = new SoundPlayer();
        var mobRegistry = new MobRegistry(this, new MobUtility(), textFormatter, soundPlayer);
        var dungeonPortalRoomRegistry = new DungeonEntranceRoomRegistry();

        var bannedItems = new BannedItems(messageCreator, textFormatter);

        var itemKey = new ItemKey(this);
        var itemHandler = new ItemHandler(itemKey, messageCreator);
        var dungeonKeys = new DungeonKeyItems(itemHandler);
        var itemGive = new ItemGive();

        var eventRegistry = new EventRegistry();

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
                eventRegistry,
                dungeonPortalRoomRegistry,
                blockRegistry,
                dungeonKeys,
                itemRegistry,
                dungeonInstanceTemplateRegistry
        );

        registerListeners(dungeonEventRouter, blockRegistry, itemRegistry, itemKey, dungeonRegistry);
        registerCommands(dungeonRegistry, dungeonEventRouter, dungeonLoadingManager, dungeonKeys, itemGive, messagesFile, menuFile, messageCreator, textFormatter);

        dungeonLifecycleService.destroyLeftAllInstanceWorlds();
        dungeonLoadingManager.loadAllDungeonsOnEnable();
    }

    private void registerListeners(DungeonEventRouter dungeonEventRouter,
                                   BlockRegistry blockRegistry,
                                   EventManagerRegistry<String> itemRegistry,
                                   ItemKey itemKey,
                                   DungeonRegistry dungeonRegistry) {

        var pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new Combustion(dungeonRegistry), this);
        pluginManager.registerEvents(new EntityCombat(dungeonEventRouter), this);
        pluginManager.registerEvents(new EntityDeath(dungeonEventRouter), this);
        pluginManager.registerEvents(new EntityExplode(dungeonEventRouter), this);

        pluginManager.registerEvents(new CauldronLevel(dungeonRegistry), this);
        pluginManager.registerEvents(new ChorusFruitTeleport(dungeonRegistry), this);
        pluginManager.registerEvents(new DurabilityLoss(dungeonRegistry), this);
        pluginManager.registerEvents(new ElytraGlide(dungeonRegistry), this);
        pluginManager.registerEvents(new EntityInteract(dungeonRegistry), this);
        pluginManager.registerEvents(new FrostWalkerFreeze(dungeonRegistry), this);
        pluginManager.registerEvents(new ItemUse(dungeonRegistry), this);
        pluginManager.registerEvents(new MendingProfit(dungeonRegistry), this);
        pluginManager.registerEvents(new PlayerLaunchProjectile(dungeonRegistry), this);
        pluginManager.registerEvents(new PotionEffectGain(dungeonRegistry), this);
        pluginManager.registerEvents(new ShieldCooldown(this, dungeonRegistry), this);
        pluginManager.registerEvents(new TotemUse(dungeonRegistry), this);

        pluginManager.registerEvents(new Login(), this);
        pluginManager.registerEvents(new DeathController(dungeonRegistry, dungeonEventRouter), this);
        pluginManager.registerEvents(new PlayerDungeonWorld(dungeonRegistry, dungeonEventRouter), this);
        pluginManager.registerEvents(new PlayerInteract(blockRegistry, itemRegistry, itemKey), this);
        pluginManager.registerEvents(new PlayerMovement(dungeonEventRouter), this);
    }

    private void registerCommands(DungeonRegistry dungeonRegistry,
                                  DungeonEventRouter dungeonEventRouter,
                                  DungeonLoadingManager dungeonLoadingManager,
                                  DungeonKeyItems dungeonKeyItems,
                                  ItemGive itemGive,
                                  ConfigurationFile messagesFile,
                                  ConfigurationFile menuFile,
                                  MessageCreator messageCreator,
                                  TextFormatter textFormatter) {

        var dungeonsBaseCommand = new DungeonsBaseCommand(
                dungeonRegistry,
                dungeonEventRouter,
                dungeonLoadingManager,
                dungeonKeyItems,
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
