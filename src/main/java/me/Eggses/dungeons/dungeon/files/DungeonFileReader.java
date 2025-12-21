package me.Eggses.dungeons.dungeon.files;

import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.areas.utility.DungeonAction;
import me.Eggses.dungeons.dungeon.areas.utility.DungeonArea;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;
import me.Eggses.dungeons.entities.equipment.ArmourCreator;
import me.Eggses.dungeons.entities.equipment.ArmourEquipment;
import me.Eggses.dungeons.entities.equipment.WeaponEquipment;
import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.mobs.MobType;
import me.Eggses.dungeons.utility.MessageCreator;
import me.Eggses.dungeons.utility.SoundPlayer;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Consumer;

public class DungeonFileReader {

    private static final Consumer<DungeonContext> EMPTY_CONSUMER = dungeonContext -> {};

    private final JavaPlugin plugin;
    private final ConfigurationFile configurationFile;
    private final MessageCreator messageCreator;
    private final SoundPlayer soundPlayer;

    public DungeonFileReader(JavaPlugin plugin,
                             String fileName,
                             MessageCreator messageCreator,
                             SoundPlayer soundPlayer) {

        this.plugin = plugin;
        this.messageCreator = messageCreator;
        this.soundPlayer = soundPlayer;

        this.configurationFile = new ConfigurationFile(plugin, fileName);
    }

    public String readTemplateFileName() {
        String templateName = configurationFile.getCustomFile().getString("dungeon_template_name");
        if (templateName == null) throw new IllegalArgumentException("Template File not Defined");
        return templateName;
    }

    public Position readDefaultGraveyard() {
        String pos = configurationFile.getCustomFile().getString("default_graveyard");
        return stringToPosition(pos);
    }

    public DungeonPortal readDungeonPortal() {

        String errorMessage = "Could Not Create Dungeon Portal in " + configurationFile.getFileName();
        ConfigurationSection portalConfig
                = configurationFile.getCustomFile().getConfigurationSection("dungeon_portal"
        );

        if (portalConfig == null) {
            plugin.getLogger().severe(errorMessage);
            return null;
        }
        String worldWithPortal = portalConfig.getString("world_with_portal");
        if (worldWithPortal == null) {
            plugin.getLogger().severe(errorMessage);
            return null;
        }
        Region entryPortalRegion = stringToRegion(portalConfig.getString("entry_portal_region"));
        if (entryPortalRegion == null) {
            plugin.getLogger().severe(errorMessage);
            return null;
        }
        Region exitPortalRegion = stringToRegion(portalConfig.getString("exit_portal_region"));
        if (exitPortalRegion == null) {
            plugin.getLogger().severe(errorMessage);
            return null;
        }
        Position exitPosition = unformattedStringToPosition(portalConfig.getString("world_exit_location"));
        if (exitPosition == null) {
            plugin.getLogger().severe(errorMessage);
            return null;
        }
        Position dungeonSpawnPosition = unformattedStringToPosition(portalConfig.getString("dungeon_spawn_position"));
        if (dungeonSpawnPosition == null) {
            plugin.getLogger().severe(errorMessage);
            return null;
        }

        int openDurationSeconds = portalConfig.getInt("open_duration_seconds");

        List<Consumer<DungeonContext>> onOpenList = resolveCommandList(portalConfig.getStringList("on_open"));
        List<Consumer<DungeonContext>> onCloseList = resolveCommandList(portalConfig.getStringList("on_close"));

        return new DungeonPortal(
                worldWithPortal,
                entryPortalRegion,
                dungeonSpawnPosition,
                exitPortalRegion,
                exitPosition,
                openDurationSeconds,
                compressList(onOpenList),
                compressList(onCloseList)
        );
    }

    public AreaControllerBuilder readDungeonAreas() {

        AreaControllerBuilder areaControllerBuilder = new AreaControllerBuilder();

        ConfigurationSection areas = configurationFile.getCustomFile().getConfigurationSection("dungeon_areas");
        if (areas == null) {
            logDungeonReadingError("Error Getting dungeon_areas in " + configurationFile.getFileName());
            return areaControllerBuilder;
        }

        for (String areaName : areas.getKeys(false)) {

            ConfigurationSection dungeonArea = areas.getConfigurationSection(areaName);
            if (dungeonArea == null) {
                logDungeonReadingError("Error Creating Area: " + areaName + ". Area Skipped");
                continue;
            }

            Region entryBounds = stringToRegion(dungeonArea.getString("entry_bounds"));
            if (entryBounds == null) {
                logDungeonReadingError("Error Creating Area: " + areaName + ". Area Skipped");
                continue;
            }

            List<Consumer<DungeonContext>> onEntryConsumers
                    = resolveCommandList(dungeonArea.getStringList("on_entry"));

            List<Consumer<DungeonContext>> onClearConsumers
                    = resolveCommandList(dungeonArea.getStringList("on_clear"));


            List<Map<?, ?>> interactionsMap = dungeonArea.getMapList("interactions");
            List<Map<?, ?>> triggersMap = dungeonArea.getMapList("triggers");

            List<DungeonAction<Position>> interactions = handleDungeonInteractions(interactionsMap);
            List<DungeonAction<String>> triggers = handleDungeonTriggers(triggersMap);

            areaControllerBuilder.addBlockInteractionList(interactions);
            areaControllerBuilder.addDungeonTriggerCommandList(triggers);

            areaControllerBuilder.addDungeonArea(new DungeonArea(
                    entryBounds,
                    compressList(onEntryConsumers),
                    compressList(onClearConsumers)
            ));
        }
        return areaControllerBuilder;
    }

    private void logDungeonReadingError(String errorMessage) {
        plugin.getLogger().severe(errorMessage);
    }

    private List<DungeonAction<Position>> handleDungeonInteractions(List<Map<?, ?>> interactionsMap) {

        List<DungeonAction<Position>> dungeonActions = new ArrayList<>();
        if (interactionsMap == null) return dungeonActions;

        for (Map<?, ?> map : interactionsMap) {

            Object pos = map.get("pos");
            if (!(pos instanceof String posString)) continue;
            Position position = stringToPosition(posString);
            if (position == null) continue;

            Object commandsObj = map.get("commands");
            if (!(commandsObj instanceof List<?> rawList)) continue;
            List<String> commands = unknownToStringList(rawList);

            List<Consumer<DungeonContext>> consumers = resolveCommandList(commands);
            Consumer<DungeonContext> compressed = compressList(consumers);

            dungeonActions.add(new DungeonAction<>(position, compressed));
        }
        return dungeonActions;
    }

    private List<DungeonAction<String>> handleDungeonTriggers(List<Map<?, ?>> triggersMap) {

        List<DungeonAction<String>> dungeonActions = new ArrayList<>();
        if (triggersMap == null) return dungeonActions;

        for (Map<?, ?> map : triggersMap) {

            Object id = map.get("id");
            if (!(id instanceof String idString)) continue;

            Object commandsObj = map.get("commands");
            if (!(commandsObj instanceof List<?> rawList)) continue;
            List<String> commands = unknownToStringList(rawList);

            List<Consumer<DungeonContext>> consumers = resolveCommandList(commands);
            Consumer<DungeonContext> compressed = compressList(consumers);

            dungeonActions.add(new DungeonAction<>(idString, compressed));
        }
        return dungeonActions;
    }

    private List<String> unknownToStringList(List<?> unkownList) {
        List<String> list = new ArrayList<>();
        for (Object object : unkownList) {
            if (object instanceof String string) {
                list.add(string);
            }
        }
        return list;
    }

    private List<Consumer<DungeonContext>> resolveCommandList(List<String> commands) {

        List<Consumer<DungeonContext>> consumersToRun = new ArrayList<>();
        List<MobBuilder> mobsToBuildInSection = new ArrayList<>();

        if (commands == null) return consumersToRun;

        for (String command : commands) {

            if (command == null) continue;
            command = command.trim();
            if (command.isBlank()) continue;

            String[] arguments = command.split("\\s+", 2);
            String commandName = arguments[0];
            command = (arguments.length == 2) ? arguments[1] : "";

            switch (commandName.toUpperCase()) {
                case "MOB" -> {
                    var mobBuilder = resolveMobCommand(command);
                    if (mobBuilder != null) mobsToBuildInSection.add(mobBuilder);
                }
                case "FILL" -> {
                    var action = resolveFillCommand(command);
                    if (action != null) consumersToRun.add(action);
                }
                case "GRAVEYARD" -> {
                    var action = resolveGraveyardCommand(command);
                    if (action != null) consumersToRun.add(action);
                }
                case "MESSAGE" -> {
                    var action = resolveMessageCommand(command);
                    if (action != null) consumersToRun.add(action);
                }
                case "SOUND" -> {
                    var action = resolvePlaySoundCommand(command);
                    if (action != null) consumersToRun.add(action);
                }
                default -> logDungeonReadingError(
                        "Unknown command " + commandName + " in " + configurationFile.getFileName()
                );
            }
        }

        if (!mobsToBuildInSection.isEmpty()) {

            List<MobBuilder> copy = List.copyOf(mobsToBuildInSection);

            consumersToRun.add(dungeonContext -> {
                var entityManager = dungeonContext.getEntityManager();
                if (entityManager != null) entityManager.spawnMobList(copy);
            });
        }

        return consumersToRun;
    }

    private MobBuilder resolveMobCommand(String mobCommand) {

        Map<String, String> valueMap = createValueMap(mobCommand);

        String type = valueMap.get("type");
        if (type == null) return null;
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }

        Position position = stringToPosition(valueMap.get("pos"));
        if (position == null) return null;

        MobType preset = MobType.getMobType(valueMap.get("preset"));

        Integer level = stringToInteger(valueMap.get("level"));
        Integer count = stringToInteger(valueMap.get("count"));
        if (level == null) level = 1;
        if (count == null) count = 1;

        WeaponEquipment weaponEquipment = WeaponEquipment.createWeaponsFromString(valueMap.get("weapon"));
        ArmourEquipment armourEquipment = ArmourCreator.createArmourFromString(valueMap.get("armour"));

        return new MobBuilder(entityType, position)
                .armourEquipment(armourEquipment)
                .weaponEquipment(weaponEquipment)
                .dungeonLevel(level)
                .count(count)
                .applyPreset(preset);
    }

    private Consumer<DungeonContext> resolveGraveyardCommand(String graveyardCommand) {

        Map<String, String> valueMap = createValueMap(graveyardCommand);

        String posString = valueMap.get("pos");
        Position position = stringToPosition(posString);
        if (position == null) return null;

        return dungeonContext -> {
            var graveyard = dungeonContext.getGraveyard();
            if (graveyard == null) return;

            graveyard.setActiveGraveyard(position);
        };
    }

    private Consumer<DungeonContext> resolveFillCommand(String fillCommand) {

        Map<String, String> valueMap = createValueMap(fillCommand);

        Material material = Material.matchMaterial(valueMap.get("block"));
        if (material == null) return null;

        Position pos1 = stringToPosition(valueMap.get("pos1"));
        Position pos2 = stringToPosition(valueMap.get("pos2"));
        if (pos1 == null || pos2 == null) return null;

        Region region = new Region(pos1, pos2);

        return dungeonContext -> {

            var world = dungeonContext.getWorld();
            if (world == null) return;

            for (int x1 = region.getMinX(); x1 <= region.getMaxX(); x1++) {
                for (int y1 = region.getMinY(); y1 <= region.getMaxY(); y1++) {
                    for (int z1 = region.getMinZ(); z1 <= region.getMaxZ(); z1++) {
                        world.getBlockAt(x1, y1, z1).setType(material, false);
                    }
                }
            }
        };
    }

    private Consumer<DungeonContext> resolveMessageCommand(String text) {

        if (text == null) return null;

        int indexOfEquals = text.indexOf('=');
        if (indexOfEquals == -1) return null;
        String msg = text.substring(indexOfEquals + 1);

        Component message = messageCreator.createMessage(msg);

        return dungeonContext -> {
            var supplier = dungeonContext.getPlayers();
            if (supplier == null) return;

            supplier.get().forEach(player -> player.sendMessage(message));
        };
    }

    private Consumer<DungeonContext> resolvePlaySoundCommand(String soundToPlay) {

        if (soundToPlay == null) return null;
        Map<String, String> valuesMap = createValueMap(soundToPlay);

        String soundValue = valuesMap.get("sound");
        if (soundValue == null) return null;

        Sound sound = soundPlayer.createSound(soundValue);

        return dungeonContext -> {
            var supplier = dungeonContext.getPlayers();
            if (supplier == null) return;

            soundPlayer.playSound(sound, supplier.get());
        };
    }

    private Integer stringToInteger(String number) {
        if (number == null) return null;
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Consumer<DungeonContext> compressList(List<Consumer<DungeonContext>> consumers) {

        if (consumers == null || consumers.isEmpty()) {
            return EMPTY_CONSUMER;
        }

        List<Consumer<DungeonContext>> copy = List.copyOf(consumers);

        return dungeonContext ->
                copy.forEach(consumer -> consumer.accept(dungeonContext));
    }

    private Map<String, String> createValueMap(String command) {

        String[] arguments = command.split("\\s+");

        Map<String, String> valuesMap = new HashMap<>();

        for (String argument : arguments) {

            int indexOfEquals = argument.indexOf('=');
            if (indexOfEquals == -1) continue;

            String key = argument.substring(0, indexOfEquals);
            String value = argument.substring(indexOfEquals + 1);

            valuesMap.put(key, value);
        }
        return valuesMap;
    }

    private Region stringToRegion(String entryBounds) {

        if (entryBounds == null) return null;

        Map<String, String> valuesMap = createValueMap(entryBounds);

        Position pos1 = stringToPosition(valuesMap.get("pos1"));
        Position pos2 = stringToPosition(valuesMap.get("pos2"));
        if (pos1 == null || pos2 == null) return null;

        return new Region(pos1, pos2);
    }

    private Position stringToPosition(String position) {
        if (position == null) return null;

        String[] coordinates = position.split(",");
        if (coordinates.length != 3) return null;

        try {
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            int z = Integer.parseInt(coordinates[2]);

            return new Position(x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Position unformattedStringToPosition(String position) {
        if (position == null) return null;
        Map<String, String> valuesMap = createValueMap(position);
        return stringToPosition(valuesMap.get("pos"));
    }
}