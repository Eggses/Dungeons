package me.Eggses.dungeons;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.commands.DungeonsBaseCommand;
import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dispatch.EventManagerRegistry;
import me.Eggses.dungeons.dungeon.bosses.BossRegistry;
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
import me.Eggses.dungeons.listeners.entities.*;
import me.Eggses.dungeons.listeners.players.*;
import me.Eggses.dungeons.tasks.TaskRunner;
import me.Eggses.dungeons.listeners.bans.*;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@SuppressWarnings("unused")
public final class Dungeons extends JavaPlugin {

    private final DungeonRegistry dungeonRegistry = new DungeonRegistry();
    private BlockRegistry blockRegistry;

    @Override
    public void onEnable() {

        var messagesFile = new ConfigurationFile(this, "messages.yml");
        var menuFile = new ConfigurationFile(this, "menus.yml");
        var messageCreator = new MessageCreator(messagesFile);
        var textFormatter = new TextFormatter();

        var dungeonInstanceTemplateRegistry = new DungeonInstanceTemplateRegistry();
        var templateReservation = new TemplateReservation();
        var itemRegistry = new EventManagerRegistry<String>();

        var dungeonLog = new DungeonLog(this);
        var instanceNameManager = new InstanceNameManager(dungeonLog);
        var dungeonWorldManager = new DungeonWorldManager(this, instanceNameManager);
        var taskRunner = new TaskRunner(this);

        this.blockRegistry = new BlockRegistry(taskRunner);

        var openPortalRegistry = new OpenPortalRegistry();

        var dungeonLifecycleService = new DungeonLifecycleService(
                this,
                dungeonRegistry,
                openPortalRegistry,
                templateReservation,
                dungeonWorldManager,
                dungeonLog
        );

        var dungeonEventRouter = new DungeonEventRouter(dungeonRegistry, openPortalRegistry);

        var soundPlayer = new SoundPlayer();
        var mobUtility = new MobUtility();
        var mobRegistry = new MobRegistry(this, mobUtility, textFormatter, soundPlayer);
        var bossRegistry = new BossRegistry(mobUtility, messageCreator, soundPlayer, blockRegistry);
        var dungeonEntranceRoomRegistry = new DungeonEntranceRoomRegistry();

        var bannedItems = new BannedItems(messageCreator, textFormatter);

        var itemKey = new ItemKey(this);
        var itemHandler = new ItemHandler(itemKey, messageCreator);
        var itemGive = new ItemGive();
        var dungeonKeyItems = new DungeonKeyItems(itemHandler, textFormatter);

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
                dungeonFactory,
                new ReadingUtility(),
                messageCreator,
                menuFile,
                soundPlayer,
                mobRegistry,
                bossRegistry,
                eventRegistry,
                dungeonEntranceRoomRegistry,
                blockRegistry,
                dungeonKeyItems,
                itemRegistry,
                itemHandler,
                itemGive,
                itemKey,
                bannedItems,
                templateReservation,
                dungeonInstanceTemplateRegistry
        );

        registerListeners(dungeonEventRouter, blockRegistry, itemRegistry, itemKey, dungeonRegistry, dungeonEntranceRoomRegistry);

        registerCommands(
                dungeonRegistry,
                dungeonEventRouter,
                dungeonLoadingManager,
                dungeonKeyItems,
                itemGive,
                messagesFile,
                menuFile,
                messageCreator
        );

        dungeonLifecycleService.destroyLeftAllInstanceWorlds();
        dungeonLoadingManager.loadDungeons();
    }


    private void registerListeners(DungeonEventRouter dungeonEventRouter,
                                   BlockRegistry blockRegistry,
                                   EventManagerRegistry<String> itemRegistry,
                                   ItemKey itemKey,
                                   DungeonRegistry dungeonRegistry,
                                   DungeonEntranceRoomRegistry dungeonEntranceRoomRegistry) {

        var pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new Combustion(dungeonRegistry, blockRegistry), this);
        pluginManager.registerEvents(new EntityCombat(dungeonEventRouter), this);
        pluginManager.registerEvents(new EntityRemove(dungeonEventRouter), this);
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
        pluginManager.registerEvents(new Inventory(itemRegistry, itemKey), this);
        pluginManager.registerEvents(new PlayerDungeonWorld(dungeonRegistry, dungeonEventRouter), this);
        pluginManager.registerEvents(new PlayerBlockInteract(blockRegistry), this);
        pluginManager.registerEvents(new PlayerItemInteract(itemRegistry, itemKey), this);
        pluginManager.registerEvents(new PlayerMovement(dungeonEventRouter, blockRegistry), this);
        pluginManager.registerEvents(new ItemDrop(dungeonEntranceRoomRegistry), this);
    }

    private void registerCommands(DungeonRegistry dungeonRegistry,
                                  DungeonEventRouter dungeonEventRouter,
                                  DungeonLoadingManager dungeonLoadingManager,
                                  DungeonKeyItems dungeonKeyItems,
                                  ItemGive itemGive,
                                  ConfigurationFile messagesFile,
                                  ConfigurationFile menuFile,
                                  MessageCreator messageCreator) {

        var dungeonsBaseCommand = new DungeonsBaseCommand(
                dungeonRegistry,
                dungeonEventRouter,
                dungeonLoadingManager,
                dungeonKeyItems,
                itemGive,
                messagesFile,
                menuFile,
                messageCreator
        );

        Objects.requireNonNull(getCommand("dungeons")).setExecutor(dungeonsBaseCommand);
        Objects.requireNonNull(getCommand("dungeons")).setTabCompleter(dungeonsBaseCommand);
    }

    @Override
    public void onDisable() {
        dungeonRegistry.endAllInstances(false);
        blockRegistry.removeAllTextDisplays();
    }

    /*
    TODO: culdrons event is fine just replace them all
    TODO: move light blocks in arena 1 block up
    TODO: lightning not spawning
    TODO: boss maybe died or it was my strength? i m\punched it a few times.

     */

    /*
    TODO mob health isnt updating - maybe the new lsitener isnt registered.
    TODO: maybe return back to the old system for mob health - to avoid these errors / other errors.
    TODO: maybe ignore the advcncements when deleting seems to always cause an error..
    TODO: or maybe dleet advancement older before pasitng into the server?
    TODO: boss health bar sitll wott work jsut i think maybe <purple> dosnt exist that is probably it

    TODO New mechanic, lightning strikes the ground, spawning some fire blocks, after 30 seocnds they despsawn
    TODO: fire blocks have a entit ymove event... OR just an entity CombustByBlock dosnt really matter... then the
    TODO listener fowards into that - remember if its a movememnt lsitener you need remove yaw / pitch.
    *






    moss isnt posioning
    moss too small
    maligant marhs orignal file: has errors; achivement file + gamerules need to be destroyed.
    SMP changes plugin notifies people where they die.


    no fire in arena
    boss bar no colour
    rot has space at front
    no moss spawns
    harvest seems ineffective
    maybe too much damage is going out
    boss too slow.
    seems fire wont trigger as like it cant get se ton fire as its rianing..
    also no moss....
    player stats need to be done.

    TODO: add another boolean flag to boss to indcate if its defeated so it will not spawn another.



    fix errors in the throwing of excpetions of the malginat marsh gaving non real gamerules
    no fire in arena
    one culdron is out of fire.
    boss boss bar isnt being coloured.
    boss name tag endless formatting health like too many deicmal
    Rotation mechanics running every second not at all split
    as rot blooms has a space at the front.
    moss isnt working/
    no indication i think you have the other setting.
    harvest is rising but slowly... got 10 stacks after a while idk
    didnt reset on death
    did not run the fire thing either...

    boss spawned randomly
    no bos
    seems if boss fight started then you cannot delete blocks?
    like area controller is spanwing mbos but not killing mobs

    seems to clean up on death idk.

    giant streak of blocks cut down the world.

    got a meessage syaing dungeon with player despite me logging out...
    but didint throw a full error might be normal.




     */
}
