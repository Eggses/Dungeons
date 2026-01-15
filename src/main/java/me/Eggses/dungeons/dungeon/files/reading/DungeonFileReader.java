package me.Eggses.dungeons.dungeon.files.reading;

import me.Eggses.dungeons.dungeon.files.templates.DungeonTemplate;
import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dungeon.files.templates.builders.*;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.RotationPosition;
import me.Eggses.dungeons.items.ItemTemplate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DungeonFileReader {

    private static final String PATH_DUNGEON_NAME = "dungeon_name";
    private static final String PATH_PORTAL_WORLD = "dungeon_portal_world";

    private static final String PORTAL_ROOM_SECTION = "dungeon_portal_room";
    private static final String PORTAL_ROOM_REGION = "general_portal_room_region";
    private static final String PORTAL_ROOM_KEYSTONE_POSITION = "keystone_position";
    private static final String PORTAL_ROOM_KEYSTONE_NAME = "keystone_name";

    private static final String KEY_ITEM_SECTION = "dungeon_key_item";
    private static final String KEY_ITEM_MATERIAL = "material";
    private static final String KEY_ITEM_NAME = "name";
    private static final String KEY_ITEM_LORE = "lore";

    private static final String TEMPLATE_NAME = "dungeon_template_name";
    private static final String DEFAULT_GRAVEYARD = "default_graveyard";
    private static final String ON_START = "on_start";

    private static final String PORTAL_SECTION = "dungeon_portal";
    private static final String PORTAL_ENTRY_REGION = "entry_portal_region";
    private static final String PORTAL_DUNGEON_SPAWN = "dungeon_spawn_position";
    private static final String PORTAL_EXIT_REGION = "exit_portal_region";
    private static final String PORTAL_WORLD_EXIT = "world_exit_position";
    private static final String PORTAL_OPEN_DURATION = "open_duration_seconds";
    private static final String PORTAL_ON_OPEN = "on_open";
    private static final String PORTAL_ON_CLOSE = "on_close";

    private static final String AREA_DUNGEON_AREAS = "dungeon_areas";
    private static final String AREA_ENTRY_BOUNDS = "entry_bounds";
    private static final String AREA_ON_ENTRY = "on_entry";
    private static final String AREA_ON_CLEAR = "on_clear";
    private static final String AREA_INTERACTIONS = "interactions";
    private static final String AREA_INTERACTIONS_POS = "pos";
    private static final String AREA_INTERACTIONS_COMMANDS = "commands";
    private static final String AREA_TRIGGERS = "triggers";
    private static final String AREA_TRIGGERS_POS =  "pos";
    private static final String AREA_TRIGGERS_COMMANDS = "commands";

    private final ConfigurationFile configurationFile;
    private final FileConfiguration file;
    private final ReadingUtility readingUtility;

    public DungeonFileReader(ConfigurationFile configurationFile, ReadingUtility readingUtility) {
        this.file = configurationFile.getCustomFile();
        this.configurationFile = configurationFile;
        this.readingUtility = readingUtility;
    }

    public DungeonTemplate createTemplate() {

        String dungeonName = readDungeonName();
        exceptionIfNotExist(dungeonName, "Dungeon Name not Found");

        String templateFileName = readTemplateName();
        exceptionIfNotExist(templateFileName, "Template name not found (dungeon_template_name)");

        String portalWorldName = readPortalWorld();
        exceptionIfNotExist(portalWorldName, "Portal World not Found (dungeon_portal_world)");

        PortalRoomTemplate portalRoomTemplate = readPortalRoomTemplate();
        exceptionIfNotExist(portalRoomTemplate, "Portal Room settings read error (dungeon_portal_room)");

        ItemTemplate itemTemplate = readKeyItem();
        exceptionIfNotExist(itemTemplate, "Key item settings read error (dungeon_key_item)");

        RotationPosition defaultGraveyard = readDefaultGraveyard();
        exceptionIfNotExist(defaultGraveyard, "Default graveyard read error (default_graveyard)");

        List<String> onStart = readOnStart();

        PortalTemplate portalTemplate = readPortalTemplate();
        exceptionIfNotExist(portalTemplate, "Portal settings read error (dungeon_portal)");

        List<AreaTemplate> areas = readAllDungeonAreas();
        exceptionIfNotExist(areas, "Dungeon areas read error (dungeon_areas)");

        return new DungeonTemplate(
                dungeonName,
                templateFileName,
                portalWorldName,
                portalRoomTemplate,
                itemTemplate,
                defaultGraveyard,
                onStart,
                portalTemplate,
                areas
        );
    }

    private String readDungeonName() {
        return file.getString(PATH_DUNGEON_NAME);
    }

    private String readPortalWorld() {
        return file.getString(PATH_PORTAL_WORLD);
    }

    private PortalRoomTemplate readPortalRoomTemplate() {

        ConfigurationSection portalRoom = file.getConfigurationSection(PORTAL_ROOM_SECTION);
        if (portalRoom == null) return null;

        Region generalRegion = readingUtility.stringToRegion(portalRoom.getString(PORTAL_ROOM_REGION));

        Position keystonePosition = readingUtility.unforamttedPosStringToPosition(portalRoom.getString(PORTAL_ROOM_KEYSTONE_POSITION));

        String keystoneName = portalRoom.getString(PORTAL_ROOM_KEYSTONE_NAME);

        if (generalRegion == null || keystonePosition == null || keystoneName == null) return null;

        return new PortalRoomTemplate(generalRegion, keystonePosition, keystoneName);
    }

    private ItemTemplate readKeyItem() {

        ConfigurationSection keySection = file.getConfigurationSection(KEY_ITEM_SECTION);
        if (keySection == null) return null;

        String material = keySection.getString(KEY_ITEM_MATERIAL);
        String name = keySection.getString(KEY_ITEM_NAME);
        List<String> lore = keySection.getStringList(KEY_ITEM_LORE);

        if (material == null || name == null) return null;

        return new ItemTemplate(material, name, lore);
    }

    private String readTemplateName() {
        return file.getString(TEMPLATE_NAME);
    }

    private RotationPosition readDefaultGraveyard() {
        return readingUtility.stringToRotationPosition(file.getString(DEFAULT_GRAVEYARD));
    }

    private List<String> readOnStart() {
        return file.getStringList(ON_START);
    }

    private PortalTemplate readPortalTemplate() {

        ConfigurationSection portalSection = file.getConfigurationSection(PORTAL_SECTION);
        if (portalSection == null) return null;

        Region entryPortalRegion =
                readingUtility.stringToRegion(portalSection.getString(PORTAL_ENTRY_REGION));

        RotationPosition dungeonSpawnPosition =
                readingUtility.stringToRotationPosition(portalSection.getString(PORTAL_DUNGEON_SPAWN));

        Region exitPortalRegion =
                readingUtility.stringToRegion(portalSection.getString(PORTAL_EXIT_REGION));

        RotationPosition worldExitLocation =
                readingUtility.stringToRotationPosition(portalSection.getString(PORTAL_WORLD_EXIT));

        Integer openDurationSeconds = readingUtility.toNumber(portalSection.getString(
                PORTAL_OPEN_DURATION), Integer::parseInt
        );

        List<String> onOpenCommands = portalSection.getStringList(PORTAL_ON_OPEN);
        List<String> onCloseCommands = portalSection.getStringList(PORTAL_ON_CLOSE);

        if (entryPortalRegion == null
                || dungeonSpawnPosition == null
                || exitPortalRegion == null
                || worldExitLocation == null
                || openDurationSeconds == null) {
            return null;
        }

        return new PortalTemplate(
                entryPortalRegion,
                dungeonSpawnPosition,
                exitPortalRegion,
                worldExitLocation,
                openDurationSeconds,
                onOpenCommands,
                onCloseCommands
        );
    }

    private List<AreaTemplate> readAllDungeonAreas() {

        List<AreaTemplate> dungeonsAreas = new ArrayList<>();

        ConfigurationSection areas = file.getConfigurationSection(AREA_DUNGEON_AREAS);
        if (areas == null) return dungeonsAreas;

        for (String nameOfArea : areas.getKeys(false)) {

            ConfigurationSection areaSection = areas.getConfigurationSection(nameOfArea);
            if (areaSection == null) continue;

            Region entryBounds = readingUtility.stringToRegion(areaSection.getString(AREA_ENTRY_BOUNDS));
            if (entryBounds == null) continue;

            List<String> onEntry = areaSection.getStringList(AREA_ON_ENTRY);
            List<String> onClear = areaSection.getStringList(AREA_ON_CLEAR);

            /*
            Interactions is a list of maps, where each map is:
            String(pos) -> "x,y,z"
            String(commands) -> List<String>
             */
            List<Map<?, ?>> interactionsMapList = areaSection.getMapList(AREA_INTERACTIONS);
            List<Map<?, ?>> triggerMapList = areaSection.getMapList(AREA_TRIGGERS);

            List<ActionTemplate<Position>> interactionTemplates = readingUtility.unknownTwoKeyMapToListR(
                    interactionsMapList,
                    AREA_INTERACTIONS_POS,
                    AREA_INTERACTIONS_COMMANDS,
                    maybeString -> (maybeString instanceof String string) ? readingUtility.stringToPosition(string) : null,
                    maybeList ->  (maybeList instanceof List<?> rawList) ? readingUtility.unkownListToStringList(rawList) : null,
                    ActionTemplate::new
            );
            List<ActionTemplate<Position>> triggerTemplates = readingUtility.unknownTwoKeyMapToListR(
                    triggerMapList,
                    AREA_TRIGGERS_POS,
                    AREA_TRIGGERS_COMMANDS,
                    maybeString -> (maybeString instanceof String string) ? readingUtility.stringToPosition(string) : null,
                    maybeList -> (maybeList instanceof List<?> rawList) ? readingUtility.unkownListToStringList(rawList) : null,
                    ActionTemplate::new
            );

            dungeonsAreas.add(new AreaTemplate(entryBounds, onEntry, onClear, interactionTemplates, triggerTemplates));
        }
        return dungeonsAreas;
    }

    private void exceptionIfNotExist(Object object, String errorMessage) {
        if (object == null) {
            throw new IllegalArgumentException("Error in: " + configurationFile.getFileName() + " - " + errorMessage);
        }
    }
}