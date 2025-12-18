package me.Eggses.dungeons.dungeon.files.reading;

import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dungeon.areas.EntityManager;
import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.areas.utility.DungeonAction;
import me.Eggses.dungeons.dungeon.areas.utility.DungeonArea;
import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.entities.equipment.ArmourCreator;
import me.Eggses.dungeons.entities.equipment.ArmourEquipment;
import me.Eggses.dungeons.entities.equipment.WeaponEquipment;
import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.mobs.MobType;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DungeonAreaReader {

    private final JavaPlugin plugin;
    private final ConfigurationFile configurationFile;
    private final DungeonFileReader dungeonFileReader;

    public DungeonAreaReader(JavaPlugin plugin,
                             ConfigurationFile configurationFile,
                             DungeonFileReader dungeonFileReader) {

        this.plugin = plugin;
        this.configurationFile = configurationFile;
        this.dungeonFileReader = dungeonFileReader;
    }

    public AreaControllerBuilder readDungeonAreas() {

        AreaControllerBuilder areaControllerBuilder = new AreaControllerBuilder();

        ConfigurationSection areas = configurationFile.getCustomFile().getConfigurationSection("dungeon_areas");
        if (areas == null) {
            logDungeonAreaReadingError(null);
            return areaControllerBuilder;
        }

        for (String areaName : areas.getKeys(false)) {

            ConfigurationSection dungeonArea = areas.getConfigurationSection(areaName);
            if (dungeonArea == null) {
                logDungeonAreaReadingError(areaName);
                continue;
            }

            Region entryBounds = dungeonFileReader.stringToRegion(dungeonArea.getString("entry_bounds"));
            if (entryBounds == null) {
                logDungeonAreaReadingError(areaName);
                continue;
            }

            List<TriConsumer<World, EntityManager, Graveyard>> onEntryConsumers
                    = resolveCommandList(dungeonArea.getStringList("on_entry"));

            List<TriConsumer<World, EntityManager, Graveyard>> onClearConsumers
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

    private void logDungeonAreaReadingError(String areaName) {
        String errorMessage = "Error in Reading File: " + configurationFile.getFileName() + ".";
        if (areaName != null) errorMessage = errorMessage + "Dungeon Area With Error: " + areaName + ".";

        plugin.getLogger().severe(errorMessage);
    }

    private List<DungeonAction<Position>> handleDungeonInteractions(List<Map<?, ?>> interactionsMap) {

        List<DungeonAction<Position>> dungeonActions = new ArrayList<>();
        if (interactionsMap == null) return dungeonActions;

        for (Map<?, ?> map : interactionsMap) {

            Object pos = map.get("pos");
            if (!(pos instanceof String posString)) continue;
            Position position = dungeonFileReader.stringToPosition(posString);
            if (position == null) continue;

            Object commandsObj = map.get("commands");
            if (!(commandsObj instanceof List<?> rawList)) continue;
            List<String> commands = unknownToStringList(rawList);

            List<TriConsumer<World, EntityManager, Graveyard>> triConsumersToRun = resolveCommandList(commands);
            TriConsumer<World, EntityManager, Graveyard> triConsumer = compressList(triConsumersToRun);

            dungeonActions.add(new DungeonAction<>(position, triConsumer));
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

            List<TriConsumer<World, EntityManager, Graveyard>> triConsumersToRun = resolveCommandList(commands);
            TriConsumer<World, EntityManager, Graveyard> triConsumer = compressList(triConsumersToRun);

            dungeonActions.add(new DungeonAction<>(idString, triConsumer));
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

    private List<TriConsumer<World, EntityManager, Graveyard>> resolveCommandList(List<String> commands) {

        List<TriConsumer<World, EntityManager, Graveyard>> triConsumersToRun = new ArrayList<>();
        List<MobBuilder> mobsToBuildInSection = new ArrayList<>();

        if (commands == null) return triConsumersToRun;

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
                    if (action != null) {
                        triConsumersToRun.add(
                                (world, entityManager, graveyard) -> action.accept(world)
                        );
                    }
                }
                case "GRAVEYARD" -> {
                    var action = resolveGraveyardCommand(command);
                    if (action != null) {
                        triConsumersToRun.add(
                                (world, entityManager, graveyard) -> action.accept(graveyard)
                        );
                    }
                }
            }
        }

        if (!mobsToBuildInSection.isEmpty()) {
            triConsumersToRun.add((world, entityManager, graveyard)
                    -> entityManager.spawnMobList(List.copyOf(mobsToBuildInSection)));
        }

        return triConsumersToRun;
    }

    private MobBuilder resolveMobCommand(String mobCommand) {

        Map<String, String> valueMap = dungeonFileReader.createValueMap(mobCommand);

        String type = valueMap.get("type");
        if (type == null) return null;
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }

        Position position = dungeonFileReader.stringToPosition(valueMap.get("pos"));
        if (position == null) return null;

        MobType preset = MobType.getMobType(valueMap.get("preset"));

        Integer level = stringToInteger(valueMap.get("level"));
        Integer count = stringToInteger(valueMap.get("count"));
        if (level == null || count == null) return null;

        WeaponEquipment weaponEquipment = WeaponEquipment.createWeaponsFromString(valueMap.get("weapon"));
        ArmourEquipment armourEquipment = ArmourCreator.createArmourFromString(valueMap.get("armour"));

        return new MobBuilder(entityType, position)
                .armourEquipment(armourEquipment)
                .weaponEquipment(weaponEquipment)
                .dungeonLevel(level)
                .count(count)
                .mobNameSpawnFinalizerTaskBehaviour(preset);
    }

    private Consumer<Graveyard> resolveGraveyardCommand(String graveyardCommand) {

        Map<String, String> valueMap = dungeonFileReader.createValueMap(graveyardCommand);

        String posString = valueMap.get("pos");
        Position position = dungeonFileReader.stringToPosition(posString);
        if (position == null) return null;

        return graveyard -> graveyard.setActiveGraveyard(position);
    }

    private Consumer<World> resolveFillCommand(String fillCommand) {

        Map<String, String> valueMap = dungeonFileReader.createValueMap(fillCommand);

        Material material = Material.matchMaterial(valueMap.get("block"));
        if (material == null) return null;

        Position pos1 = dungeonFileReader.stringToPosition(valueMap.get("pos1"));
        Position pos2 = dungeonFileReader.stringToPosition(valueMap.get("pos2"));
        if (pos1 == null || pos2 == null) return null;

        Region region = new Region(pos1, pos2);

        return world -> {
            for (int x1 = region.getMinX(); x1 <= region.getMaxX(); x1++) {
                for (int y1 = region.getMinY(); y1 <= region.getMaxY(); y1++) {
                    for (int z1 = region.getMinZ(); z1 <= region.getMaxZ(); z1++) {
                        world.getBlockAt(x1, y1, z1).setType(material, false);
                    }
                }
            }
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

    private TriConsumer<World, EntityManager, Graveyard> compressList(
            List<TriConsumer<World, EntityManager, Graveyard>> consumers) {

        if (consumers == null || consumers.isEmpty()) {
            return (w, em, g) -> {};
        }

        List<TriConsumer<World, EntityManager, Graveyard>> copy = List.copyOf(consumers);

        return (w, em, g) -> copy.forEach(c -> c.accept(w, em, g));
    }
}