package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.blocks.task.KeystoneParticleTask;
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
import me.Eggses.dungeons.dungeon.portalroom.DungeonPortalRoomRegistry;
import me.Eggses.dungeons.dungeon.regions.WorldRegion;
import me.Eggses.dungeons.entities.mobs.mobtype.MobRegistry;
import me.Eggses.dungeons.items.DungeonItemKeys;
import me.Eggses.dungeons.items.events.CancelUse;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Placeholder;
import me.Eggses.dungeons.utility.text.Placeholders;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class DungeonLoadingManager {

    private final Set<DungeonType> dungeons = EnumSet.allOf(DungeonType.class);

    private final JavaPlugin plugin;
    private final ReadingUtility readingUtility;
    private final MessageCreator messageCreator;
    private final SoundPlayer soundPlayer;
    private final MobRegistry mobRegistry;

    private final DungeonPortalRoomRegistry dungeonPortalRoomRegistry;
    private final BlockRegistry blockRegistry;
    private final DungeonItemKeys dungeonItemKeys;
    private final EventManagerRegistry<String> itemRegistry;

    private final DungeonInstanceTemplateRegistry dungeonInstanceTemplateRegistry;

    private final Map<DungeonType, Location> keystoneLocations = new EnumMap<>(DungeonType.class);
    private final Map<DungeonType, WorldRegion> portalRooms = new EnumMap<>(DungeonType.class);

    private boolean loadScheduled = false;

    public DungeonLoadingManager(JavaPlugin plugin,
                                 ReadingUtility readingUtility,
                                 MessageCreator messageCreator,
                                 SoundPlayer soundPlayer,
                                 MobRegistry mobRegistry,
                                 DungeonPortalRoomRegistry dungeonPortalRoomRegistry,
                                 BlockRegistry blockRegistry,
                                 DungeonItemKeys dungeonItemKeys,
                                 EventManagerRegistry<String> itemRegistry,
                                 DungeonInstanceTemplateRegistry dungeonInstanceTemplateRegistry) {

        this.plugin = plugin;
        this.readingUtility = readingUtility;
        this.messageCreator = messageCreator;
        this.soundPlayer = soundPlayer;
        this.mobRegistry = mobRegistry;
        this.dungeonPortalRoomRegistry = dungeonPortalRoomRegistry;
        this.blockRegistry = blockRegistry;
        this.dungeonItemKeys = dungeonItemKeys;
        this.itemRegistry = itemRegistry;
        this.dungeonInstanceTemplateRegistry = dungeonInstanceTemplateRegistry;
    }

    public void loadAllDungeonsOnEnable() {
        loadScheduled = true;

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            loadScheduled = false;
            loadDungeons();
        }, 20 * 30);
    }

    public void reloadAllDungeons() {
        if (loadScheduled) return;

        cleanUpDungeons();
        loadDungeons();
    }

    private void loadDungeons() {
        for (DungeonType dungeonType : dungeons) {

            ConfigurationFile fileToRead = new ConfigurationFile(plugin, dungeonType.getDungeonConfigFileName());
            DungeonFileReader dungeonFileReader = new DungeonFileReader(fileToRead, readingUtility);
            DungeonTemplate dungeonTemplate = dungeonFileReader.createTemplate();
            DungeonTemplateCompiler dungeonTemplateCompiler = new DungeonTemplateCompiler(plugin, dungeonTemplate, readingUtility, mobRegistry, messageCreator, soundPlayer);

            DungeonInstanceTemplate dungeonInstanceTemplate = dungeonTemplateCompiler.createDungeonInstanceTemplate();
            dungeonInstanceTemplateRegistry.add(dungeonType, dungeonInstanceTemplate);

            NonInstanceDungeonTemplate nonInstanceDungeonTemplate = dungeonTemplateCompiler.createNonInstanceDungeonTemplate();

            Placeholders placeholders = messageCreator.Placeholders();
            placeholders.addPlaceholder(Placeholder.DUNGEON_NAME, nonInstanceDungeonTemplate.dungeonName());
            String openDurationSeconds = String.valueOf(dungeonTemplate.getPortalTemplate().getOpenDurationSeconds());
            placeholders.addPlaceholder(Placeholder.OPEN_DURATION, openDurationSeconds);

            var itemStackTemplate = nonInstanceDungeonTemplate.itemStackTemplate();
            var uniqueKey = dungeonType.getUniqueKey();
            dungeonItemKeys.add(dungeonType, itemStackTemplate, uniqueKey, placeholders);
            itemRegistry.addOrUpdate(uniqueKey, PlayerInteractEvent.class, new CancelUse());

            var positionOfKeystone = nonInstanceDungeonTemplate.positionOfKeyStone();
            var worldOfKeystone = nonInstanceDungeonTemplate.dungeonPortalRoomWorld();
            var locationOfKeystone = positionOfKeystone.toLocation(worldOfKeystone);
            keystoneLocations.put(dungeonType, locationOfKeystone);

            blockRegistry.addBlockAndEvent(locationOfKeystone, PlayerInteractEvent.class, new InteractOpenMenu());
            blockRegistry.addBlockAndName(locationOfKeystone, textDisplay -> {
                Component keystoneName = messageCreator.createMessage(nonInstanceDungeonTemplate.keystoneName(), placeholders);
                textDisplay.text(keystoneName);
                textDisplay.setAlignment(TextDisplay.TextAlignment.CENTER);
            });
            blockRegistry.addBlockAndTaskBehaviour(locationOfKeystone, new KeystoneParticleTask().getTaskContext());

            var generalPortalRoomRegion = nonInstanceDungeonTemplate.generalPortalRoomRegion();
            var worldRegion = new WorldRegion(worldOfKeystone, generalPortalRoomRegion);
            portalRooms.put(dungeonType, worldRegion);
            dungeonPortalRoomRegistry.addPortalRoom(worldRegion);
        }
    }

    private void cleanUpDungeons() {

        for (DungeonType dungeonType : dungeons) {

            dungeonItemKeys.remove(dungeonType);
            itemRegistry.remove(dungeonType.getUniqueKey());

            Location locationOfKeystone = keystoneLocations.remove(dungeonType);
            if (locationOfKeystone != null) blockRegistry.remove(locationOfKeystone);

            WorldRegion generalPortalRoomRegion = portalRooms.remove(dungeonType);
            if (generalPortalRoomRegion != null) dungeonPortalRoomRegistry.remove(generalPortalRoomRegion);
        }
    }
}