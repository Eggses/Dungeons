package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.blocks.task.KeystoneParticleTask;
import me.Eggses.dungeons.dungeon.bosses.BossRegistry;
import me.Eggses.dungeons.dungeon.items.ClickItem;
import me.Eggses.dungeons.dungeon.items.DungeonKeyItems;
import me.Eggses.dungeons.dungeon.regions.WorldPosition;
import me.Eggses.dungeons.dungeon.types.DungeonType;
import me.Eggses.dungeons.blocks.events.InteractOpenMenu;
import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dispatch.EventManagerRegistry;
import me.Eggses.dungeons.dungeon.files.reading.DungeonFileReader;
import me.Eggses.dungeons.dungeon.files.reading.DungeonTemplateCompiler;
import me.Eggses.dungeons.dungeon.files.reading.ReadingUtility;
import me.Eggses.dungeons.dungeon.files.templates.DungeonInstanceTemplate;
import me.Eggses.dungeons.dungeon.files.templates.DungeonTemplate;
import me.Eggses.dungeons.dungeon.files.templates.NonInstanceDungeonTemplate;
import me.Eggses.dungeons.dungeon.portalroom.DungeonEntranceRoomRegistry;
import me.Eggses.dungeons.dungeon.regions.WorldRegion;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.entities.mobs.mobtype.MobRegistry;
import me.Eggses.dungeons.dungeon.items.CancelUse;
import me.Eggses.dungeons.eventhandler.EventRegistry;
import me.Eggses.dungeons.items.ItemGive;
import me.Eggses.dungeons.items.ItemHandler;
import me.Eggses.dungeons.items.ItemKey;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Placeholder;
import me.Eggses.dungeons.utility.text.Placeholders;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class DungeonLoadingManager {

    private final Set<DungeonType> dungeons = EnumSet.allOf(DungeonType.class);

    private final JavaPlugin plugin;
    private final DungeonFactory dungeonFactory;

    private final ReadingUtility readingUtility;
    private final MessageCreator messageCreator;
    private final FileConfiguration menuFile;
    private final SoundPlayer soundPlayer;

    private final MobRegistry mobRegistry;
    private final BossRegistry bossRegistry;
    private final EventRegistry eventRegistry;
    private final DungeonEntranceRoomRegistry dungeonEntranceRoomRegistry;
    private final BlockRegistry blockRegistry;
    private final DungeonKeyItems dungeonKeyItems;
    private final EventManagerRegistry<String> itemRegistry;

    private final ItemHandler itemHandler;
    private final ItemGive itemGive;
    private final ItemKey itemKey;
    private final BannedItems bannedItems;

    private final TemplateReservation templateReservation;
    private final DungeonInstanceTemplateRegistry dungeonInstanceTemplateRegistry;

    private final Map<DungeonType, WorldPosition> keystoneLocations = new EnumMap<>(DungeonType.class);
    private final Map<DungeonType, WorldRegion> portalRooms = new EnumMap<>(DungeonType.class);

    public DungeonLoadingManager(JavaPlugin plugin,
                                 DungeonFactory dungeonFactory,
                                 ReadingUtility readingUtility,
                                 MessageCreator messageCreator,
                                 ConfigurationFile menuFile,
                                 SoundPlayer soundPlayer,
                                 MobRegistry mobRegistry,
                                 BossRegistry bossRegistry,
                                 EventRegistry eventRegistry,
                                 DungeonEntranceRoomRegistry dungeonEntranceRoomRegistry,
                                 BlockRegistry blockRegistry,
                                 DungeonKeyItems dungeonKeyItems,
                                 EventManagerRegistry<String> itemRegistry,
                                 ItemHandler itemHandler,
                                 ItemGive itemGive,
                                 ItemKey itemKey,
                                 BannedItems bannedItems,
                                 TemplateReservation templateReservation,
                                 DungeonInstanceTemplateRegistry dungeonInstanceTemplateRegistry) {

        this.plugin = plugin;
        this.dungeonFactory = dungeonFactory;

        this.readingUtility = readingUtility;
        this.messageCreator = messageCreator;
        this.menuFile = menuFile.getCustomFile();
        this.soundPlayer = soundPlayer;

        this.mobRegistry = mobRegistry;
        this.bossRegistry = bossRegistry;
        this.eventRegistry = eventRegistry;
        this.dungeonEntranceRoomRegistry = dungeonEntranceRoomRegistry;
        this.blockRegistry = blockRegistry;
        this.dungeonKeyItems = dungeonKeyItems;
        this.itemRegistry = itemRegistry;

        this.itemHandler = itemHandler;
        this.itemGive = itemGive;
        this.itemKey = itemKey;
        this.bannedItems = bannedItems;

        this.templateReservation = templateReservation;
        this.dungeonInstanceTemplateRegistry = dungeonInstanceTemplateRegistry;
    }

    public void reloadAllDungeons() {
        cleanUpDungeons();
        loadDungeons();
    }

    public void loadDungeons() {

        for (DungeonType dungeonType : dungeons) {

            ConfigurationFile fileToRead = new ConfigurationFile(plugin, dungeonType.getDungeonConfigFileName());
            DungeonFileReader dungeonFileReader = new DungeonFileReader(fileToRead, readingUtility);
            DungeonTemplate dungeonTemplate = dungeonFileReader.createTemplate();
            DungeonTemplateCompiler dungeonTemplateCompiler = new DungeonTemplateCompiler(plugin, dungeonTemplate, readingUtility, mobRegistry, bossRegistry, eventRegistry, messageCreator, soundPlayer);

            NonInstanceDungeonTemplate nonInstanceDungeonTemplate = dungeonTemplateCompiler.createNonInstanceDungeonTemplate();
            Placeholders placeholders = messageCreator.placeholders();
            placeholders.addPlaceholder(Placeholder.DUNGEON_NAME, nonInstanceDungeonTemplate.dungeonName());
            String openDurationSeconds = String.valueOf(dungeonTemplate.getPortalTemplate().getOpenDurationSeconds());
            placeholders.addPlaceholder(Placeholder.OPEN_DURATION, openDurationSeconds);

            DungeonInstanceTemplate dungeonInstanceTemplate = dungeonTemplateCompiler.createDungeonInstanceTemplate(placeholders);
            dungeonInstanceTemplateRegistry.add(dungeonType, dungeonInstanceTemplate);

            var itemTemplate = nonInstanceDungeonTemplate.itemTemplate();
            var uniqueKey = dungeonType.getUniqueKey();
            dungeonKeyItems.addKey(dungeonType, new DungeonKeyItems.KeyItem(itemTemplate, placeholders, uniqueKey));

            itemRegistry.addOrUpdate(uniqueKey, PlayerInteractEvent.class, new CancelUse());
            itemRegistry.addOrUpdate(uniqueKey, InventoryClickEvent.class, new ClickItem());

            var positionOfKeystone = nonInstanceDungeonTemplate.positionOfKeyStone();
            var worldOfKeystone = nonInstanceDungeonTemplate.dungeonPortalRoomWorld();
            var worldPositionOfKeystone = new WorldPosition(worldOfKeystone, positionOfKeystone);
            keystoneLocations.put(dungeonType, worldPositionOfKeystone);

            blockRegistry.addBlockAndEvent(worldPositionOfKeystone, PlayerInteractEvent.class, new InteractOpenMenu(
                    dungeonFactory,
                    templateReservation,
                    dungeonType,
                    itemHandler,
                    itemGive,
                    itemKey,
                    bannedItems,
                    messageCreator,
                    placeholders,
                    menuFile
            ));

            var name = messageCreator.createMessage(nonInstanceDungeonTemplate.keystoneName(), placeholders);
            blockRegistry.addBlockAndTextDisplay(worldPositionOfKeystone, name);

            blockRegistry.addBlockAndTaskBehaviour(worldPositionOfKeystone, new KeystoneParticleTask().getTask());

            var generalPortalRoomRegion = nonInstanceDungeonTemplate.generalPortalRoomRegion();
            var worldRegion = new WorldRegion(worldOfKeystone, generalPortalRoomRegion);
            portalRooms.put(dungeonType, worldRegion);
            dungeonEntranceRoomRegistry.addPortalRoom(worldRegion);
        }
    }

    private void cleanUpDungeons() {

        for (DungeonType dungeonType : dungeons) {

            dungeonKeyItems.removeKey(dungeonType);
            itemRegistry.remove(dungeonType.getUniqueKey());

            WorldPosition worldPositionOfKeystone = keystoneLocations.remove(dungeonType);
            if (worldPositionOfKeystone != null) blockRegistry.remove(worldPositionOfKeystone);

            WorldRegion generalPortalRoomRegion = portalRooms.remove(dungeonType);
            if (generalPortalRoomRegion != null) dungeonEntranceRoomRegistry.remove(generalPortalRoomRegion);
        }
    }
}
