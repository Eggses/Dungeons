package me.Eggses.dungeons.dungeon.files.reading;

import me.Eggses.dungeons.dungeon.bosses.BossArenaTemplate;
import me.Eggses.dungeons.dungeon.bosses.BossRegistry;
import me.Eggses.dungeons.dungeon.bosses.DungeonBossBuilder;
import me.Eggses.dungeons.dungeon.files.templates.DungeonTemplate;
import me.Eggses.dungeons.dungeon.files.templates.DungeonInstanceTemplate;
import me.Eggses.dungeons.dungeon.files.templates.NonInstanceDungeonTemplate;
import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.areas.utility.DungeonAction;
import me.Eggses.dungeons.dungeon.areas.utility.DungeonArea;
import me.Eggses.dungeons.dungeon.files.templates.builders.*;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.RotationPosition;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;
import me.Eggses.dungeons.entities.equipment.ArmourCreator;
import me.Eggses.dungeons.entities.equipment.ArmourEquipment;
import me.Eggses.dungeons.entities.equipment.WeaponEquipment;
import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.mobs.mobtype.MobRegistry;
import me.Eggses.dungeons.eventhandler.EventDefinition;
import me.Eggses.dungeons.eventhandler.EventRegistry;
import me.Eggses.dungeons.items.ItemTemplate;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import me.Eggses.dungeons.utility.text.Placeholders;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DungeonTemplateCompiler {

    private static final String COMMAND_MOB = "MOB";
    private static final String COMMAND_FILL = "FILL";
    private static final String COMMAND_GRAVEYARD = "GRAVEYARD";
    private static final String COMMAND_TIME = "TIME";
    private static final String COMMAND_WEATHER = "WEATHER";
    private static final String COMMAND_MESSAGE = "MESSAGE";
    private static final String COMMAND_SOUND = "SOUND";
    private static final String COMMAND_EVENT = "EVENT";
    private static final String COMMAND_PORTAL = "PORTAL";

    private static final String ARG_TYPE = "type";
    private static final String ARG_PRESET = "preset";
    private static final String ARG_LEVEL = "level";
    private static final String ARG_COUNT = "count";
    private static final String ARG_WEAPON = "weapon";
    private static final String ARG_ARMOUR = "armour";
    private static final String ARG_RIDING = "riding";

    private static final String ARG_BLOCK = "block";

    private static final String ARG_TIME = "time";
    private static final String TIME_AM = "am";
    private static final String TIME_PM = "pm";

    private static final String ARG_WEATHER = "weather";
    private static final String WEATHER_CLEAR = "clear";
    private static final String WEATHER_RAIN = "rain";
    private static final String WEATHER_THUNDERSTORM = "thunderstorm";

    private static final String ARG_SOUND = "sound";

    private static final String ARG_EVENTS = "events";

    private final JavaPlugin plugin;
    private final DungeonTemplate dungeonTemplate;
    private final ReadingUtility readingUtility;
    private final MobRegistry mobRegistry;
    private final BossRegistry bossRegistry;
    private final EventRegistry eventRegistry;
    private final MessageCreator messageCreator;
    private final SoundPlayer soundPlayer;

    private World portalRoomWorld;
    private String dungeonName;

    public DungeonTemplateCompiler(JavaPlugin plugin,
                                   DungeonTemplate dungeonTemplate,
                                   ReadingUtility readingUtility,
                                   MobRegistry mobRegistry,
                                   BossRegistry bossRegistry,
                                   EventRegistry eventRegistry,
                                   MessageCreator messageCreator,
                                   SoundPlayer soundPlayer) {

        this.plugin = plugin;
        this.dungeonTemplate = dungeonTemplate;
        this.readingUtility = readingUtility;
        this.mobRegistry = mobRegistry;
        this.bossRegistry = bossRegistry;
        this.eventRegistry = eventRegistry;
        this.messageCreator = messageCreator;
        this.soundPlayer = soundPlayer;
    }

    public NonInstanceDungeonTemplate createNonInstanceDungeonTemplate() {
        String dungeonName = createDungeonName();
        World dungeonPortalRoomWorld = createPortalRoomWorld();

        PortalRoomTemplate portalRoomTemplate = createPortalRoomTemplate();
        Position positionOfKeyStone = portalRoomTemplate.getPositionOfKeyStone();
        Region generalPortalRoomRegion = portalRoomTemplate.getGeneralRoomRegion();
        String keystoneName = portalRoomTemplate.getKeystoneName();

        ItemTemplate itemTemplate = createItemStackTemplate();

        return new NonInstanceDungeonTemplate(
                dungeonName,
                dungeonPortalRoomWorld,
                positionOfKeyStone,
                generalPortalRoomRegion,
                keystoneName,
                itemTemplate
        );
    }

    public DungeonInstanceTemplate createDungeonInstanceTemplate(Placeholders placeholders) {

        String templateFolderName = createTemplateFileName();
        String dungeonName = createDungeonName();
        DungeonPortal dungeonPortal = createDungeonPortal(placeholders);
        AreaControllerBuilder areaControllerBuilder = createAreaControllerBuilder();
        Consumer<DungeonContext> onDungeonStart = createOnDungeonStart();
        BossArenaTemplate bossArenaTemplate = createBossArenaTemplate();

        return new DungeonInstanceTemplate(
                templateFolderName,
                dungeonName,
                dungeonPortal,
                areaControllerBuilder,
                onDungeonStart,
                bossArenaTemplate
        );
    }

    private String createDungeonName() {
        if (dungeonName == null) {
            dungeonName = dungeonTemplate.getDungeonName();
        }
        return dungeonName;
    }

    private String createTemplateFileName() {
        return dungeonTemplate.getTemplateFileName();
    }

    private World createPortalRoomWorld() {
        if (portalRoomWorld == null) {
            World world = Bukkit.getWorld(dungeonTemplate.getPortalRoomWorld());
            if (world == null) throw new IllegalArgumentException("World does not exist!");
            portalRoomWorld = world;
        }
        return portalRoomWorld;
    }

    private PortalRoomTemplate createPortalRoomTemplate() {
        return dungeonTemplate.getPortalRoomTemplate();
    }

    private ItemTemplate createItemStackTemplate() {
        return dungeonTemplate.getItemTemplate();
    }

    private Consumer<DungeonContext> createOnDungeonStart() {
        return resolveCommandList(dungeonTemplate.getOnStart(), messageCreator.placeholders());
    }

    private BossArenaTemplate createBossArenaTemplate() {

        BossTemplate template = dungeonTemplate.getBoss();

        Consumer<DungeonContext> onDefeat = resolveCommandList(template.onBossDefeat(), messageCreator.placeholders());
        if (onDefeat == null) throw new IllegalArgumentException("Error reading Boss Template");

        Supplier<DungeonBossBuilder> bossCreator = bossRegistry.getDungeonBossBuilder(template.bossPreset());
        if (bossCreator == null) throw new IllegalArgumentException("Error reading Boss Template");

        return new BossArenaTemplate(
                bossCreator,
                template.entryRegion(),
                template.playerSpawningRotationPosition(),
                onDefeat
        );
    }

    private DungeonPortal createDungeonPortal(Placeholders placeholders) {

        PortalTemplate portalTemplate = dungeonTemplate.getPortalTemplate();

        World world = createPortalRoomWorld();
        if (world == null) throw new IllegalArgumentException("Null World used to create a Portal");

        Consumer<DungeonContext> onOpen = resolveCommandList(portalTemplate.getOnOpenCommands(), placeholders);
        Consumer<DungeonContext> onClose = resolveCommandList(portalTemplate.getOnCloseCommands(), placeholders);

        return new DungeonPortal(
                world,
                portalTemplate.getEntryPortalRegion(),
                portalTemplate.getDungeonSpawnPosition(),
                portalTemplate.getExitPortalRegion(),
                portalTemplate.getWorldExitLocation(),
                portalTemplate.getOpenDurationSeconds(),
                onOpen,
                onClose
        );
    }

    private AreaControllerBuilder createAreaControllerBuilder() {

        AreaControllerBuilder builder = new AreaControllerBuilder();

        for (AreaTemplate area : dungeonTemplate.getAreas()) {

            Region entryBounds = area.getEntryBounds();

            Consumer<DungeonContext> onEntry = resolveCommandList(area.getOnEntryCommands(), messageCreator.placeholders());
            Consumer<DungeonContext> onClear = resolveCommandList(area.getOnClearCommands(), messageCreator.placeholders());

            builder.addDungeonArea(new DungeonArea(entryBounds, onEntry, onClear));
            builder.addBlockInteractionList(handleActionTemplates(area.getInteractionsTemplates()));
            builder.addDungeonTriggerCommandList(handleActionTemplates(area.getTriggerTemplates()));
        }
        return builder;
    }

    private <T> List<DungeonAction<T>> handleActionTemplates(List<ActionTemplate<T>> actionTemplates) {

        List<DungeonAction<T>> dungeonActions = new ArrayList<>();

        for (ActionTemplate<T> actionTemplate : actionTemplates) {
            T t = actionTemplate.getT();
            Consumer<DungeonContext> commands = resolveCommandList(actionTemplate.getCommands(), messageCreator.placeholders());

            dungeonActions.add(new DungeonAction<>(t, commands));
        }
        return dungeonActions;
    }

    private Consumer<DungeonContext> resolveCommandList(List<String> commands, Placeholders placeholders) {

        if (commands == null || commands.isEmpty()) return getEmptyConsumer();

        List<Consumer<DungeonContext>> consumersToRun = new ArrayList<>();
        List<MobBuilder> mobsToBuildInSection = new ArrayList<>();

        for (String command : commands) {

            if (command == null) continue;
            command = command.trim();
            if (command.isBlank()) continue;

            String[] arguments = command.split("\\s+", 2);
            // ["CMD"] ["X=1 Y=2 Z=3"] as limit split into 2 groups.
            String commandName = arguments[0];
            command = (arguments.length == 2) ? arguments[1] : "";

            switch (commandName.toUpperCase()) {
                case COMMAND_MOB -> {
                    var mobBuilder = resolveMobCommand(command);
                    if (mobBuilder != null) mobsToBuildInSection.add(mobBuilder);
                }
                case COMMAND_FILL -> {
                    var action = resolveFillCommand(command);
                    if (action != null) consumersToRun.add(action);
                }
                case COMMAND_GRAVEYARD -> {
                    var action = resolveGraveyardCommand(command);
                    if (action != null) consumersToRun.add(action);
                }
                case COMMAND_TIME -> {
                    var action = resolveTimeCommand(command);
                    if (action != null) consumersToRun.add(action);
                }
                case COMMAND_WEATHER -> {
                    var action = resolveWeatherCommand(command);
                    if (action != null) consumersToRun.add(action);
                }
                case COMMAND_MESSAGE -> {
                    var action = resolveMessageCommand(command, placeholders);
                    if (action != null) consumersToRun.add(action);
                }
                case COMMAND_SOUND -> {
                    var action = resolveSoundCommand(command);
                    if (action != null) consumersToRun.add(action);
                }
                case COMMAND_EVENT -> {
                    var action = resolveEventCommand(command);
                    if (action != null) consumersToRun.add(action);
                }
                case COMMAND_PORTAL -> {
                    var action = resolvePortalCommand(command);
                    if (action != null) consumersToRun.add(action);
                }
                default -> plugin.getLogger().warning("Unknown Command Used: " + commandName + "!");
            }
        }

        if (!mobsToBuildInSection.isEmpty()) {

            List<MobBuilder> copyOfMobs = List.copyOf(mobsToBuildInSection);

            consumersToRun.add(dungeonContext -> {
                var entityManager = dungeonContext.getEntityManager();
                if (entityManager == null) return;

                entityManager.spawnMobList(copyOfMobs);
            });
        }

        return compressConsumerList(consumersToRun);
    }

    private MobBuilder resolveMobCommand(String command) {

        Map<String, String> valueMap = readingUtility.createValueMap(command);

        EntityType entityType = getEntityType(valueMap.get(ARG_TYPE));
        if (entityType == null) return null;

        Position position = readingUtility.stringToPosition(valueMap.get(ReadingUtility.ARG_POS));
        if (position == null) return null;

        Consumer<MobBuilder> preset = mobRegistry.getPreset(valueMap.get(ARG_PRESET));

        Integer level = readingUtility.toNumber(valueMap.get(ARG_LEVEL), Integer::parseInt);
        Integer count = readingUtility.toNumber(valueMap.get(ARG_COUNT), Integer::parseInt);
        if (level == null) level = 1;
        if (count == null) count = 1;

        WeaponEquipment weaponEquipment = WeaponEquipment.createWeaponsFromString(valueMap.get(ARG_WEAPON));
        ArmourEquipment armourEquipment = ArmourCreator.createArmourFromString(valueMap.get(ARG_ARMOUR));

        EntityType typeOfMount = getEntityType(valueMap.get(ARG_RIDING));

        return new MobBuilder(entityType, position)
                .armourEquipment(armourEquipment)
                .weaponEquipment(weaponEquipment)
                .dungeonLevel(level)
                .count(count)
                .mountType(typeOfMount)
                .applyPreset(preset);
    }

    private EntityType getEntityType(String type) {

        if (type == null) return null;

        try {
            return EntityType.valueOf(type.toUpperCase(Locale.ROOT));
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Consumer<DungeonContext> resolveFillCommand(String command) {

        Map<String, String> valueMap = readingUtility.createValueMap(command);

        Material material = Material.matchMaterial(valueMap.get(ARG_BLOCK));
        if (material == null) return null;

        Position pos1 = readingUtility.stringToPosition(valueMap.get(ReadingUtility.ARG_POS_1));
        Position pos2 = readingUtility.stringToPosition(valueMap.get(ReadingUtility.ARG_POS_2));
        if (pos1 == null || pos2 == null) return null;

        Region region = new Region(pos1, pos2);

        return dungeonContext -> {

            var world = dungeonContext.getWorld();
            if (world == null) return;

            for (int x1 = region.getMinX(); x1 <= region.getMaxX(); x1++) {
                for (int y1 = region.getMinY(); y1 <= region.getMaxY(); y1++) {
                    for (int z1 = region.getMinZ(); z1 <= region.getMaxZ(); z1++) {
                        world.getBlockAt(x1, y1, z1).setType(material, true);
                    }
                }
            }
        };
    }

    private Consumer<DungeonContext> resolvePortalCommand(String command) {

        Map<String, String> valueMap = readingUtility.createValueMap(command);

        Position pos1 = readingUtility.stringToPosition(valueMap.get(ReadingUtility.ARG_POS_1));
        Position pos2 = readingUtility.stringToPosition(valueMap.get(ReadingUtility.ARG_POS_2));
        if (pos1 == null || pos2 == null) return null;

        Region portalRegion = new Region(pos1, pos2);

        return dungeonContext -> {
            var instance = dungeonContext.getDungeonInstance();
            if (instance == null) return;
            instance.getPortalController().addDungeonExitPortalRegion(portalRegion);
        };
    }

    private Consumer<DungeonContext> resolveGraveyardCommand(String command) {
        RotationPosition rotationPosition = readingUtility.stringToRotationPosition(command);
        if (rotationPosition == null) return null;

        return dungeonContext -> {
            var graveyard = dungeonContext.getGraveyard();
            if (graveyard == null) return;

            graveyard.setActiveGraveyard(rotationPosition);
        };
    }

    private Consumer<DungeonContext> resolveTimeCommand(String command) {

        Map<String, String> valueMap = readingUtility.createValueMap(command);
        String timeString = valueMap.get(ARG_TIME);
        if (timeString == null) return null;

        String hourString = timeString.replaceAll("[^0-9]", "");
        String periodString = timeString.replaceAll("[^A-Za-z]", "");

        Integer hour = readingUtility.toNumber(hourString, Integer::parseInt);
        if (hour == null) return null;

        if (periodString.equalsIgnoreCase(TIME_PM) && hour != 12) {
            hour += 12;
        } else if (periodString.equalsIgnoreCase(TIME_AM) && hour == 12) {
            hour = 0;
        }

        // 6AM = 0, 7AM = 1000, 5am = 23000...
        int ticks = (hour * 1000 + 18000) % 24000;

        return dungeonContext -> {
            var world = dungeonContext.getWorld();
            if (world == null) return;
            world.setTime(ticks);
        };
    }

    private Consumer<DungeonContext> resolveWeatherCommand(String command) {

        Map<String, String> valueMap = readingUtility.createValueMap(command);
        String weatherString = valueMap.get(ARG_WEATHER);
        if (weatherString == null) return null;

        Consumer<World> weatherSetter = switch (weatherString.toLowerCase()) {
            case WEATHER_CLEAR -> world -> {
                world.setStorm(false);
                world.setThundering(false);
            };
            case WEATHER_RAIN -> world -> {
                world.setStorm(true);
                world.setThundering(false);
            };
            case WEATHER_THUNDERSTORM -> world -> {
                world.setStorm(true);
                world.setThundering(true);
            };
            default -> null;
        };
        if (weatherSetter == null) return null;

        return dungeonContext -> {
            var world = dungeonContext.getWorld();
            weatherSetter.accept(world);
        };
    }

    private Consumer<DungeonContext> resolveMessageCommand(String text, Placeholders placeholders) {

        if (text == null) return null;

        Component message = messageCreator.createMessage(text, placeholders);

        return dungeonContext -> {
            var playersSupplier = dungeonContext.getPlayers();
            if (playersSupplier == null) return;

            var players = playersSupplier.get();
            if (players == null) return;

            players.forEach(player -> player.sendMessage(message));
        };
    }

    private Consumer<DungeonContext> resolveSoundCommand(String command) {

        Map<String, String> valuesMap = readingUtility.createValueMap(command);

        String soundToPlay = valuesMap.get(ARG_SOUND);
        if (soundToPlay == null) return null;

        Sound sound = soundPlayer.createSound(soundToPlay);

        return dungeonContext -> {
            var playersSupplier = dungeonContext.getPlayers();
            if (playersSupplier == null) return;

            var players = playersSupplier.get();
            if (players == null) return;

            soundPlayer.playSound(sound, players);
        };
    }

    private Consumer<DungeonContext> resolveEventCommand(String event) {

        Map<String, String> valuesMap = readingUtility.createValueMap(event);
        String eventsString = valuesMap.get(ARG_EVENTS);

        if (eventsString == null) return null;
        List<EventDefinition<?>> eventDefinitions = new ArrayList<>();

        String[] eventKeys = eventsString.split(",");
        for (String eventKey : eventKeys) {

            if (eventKey == null) continue;
            eventKey = eventKey.trim();

            EventDefinition<?> eventDefinition = eventRegistry.getEventDefinition(eventKey);
            if (eventDefinition == null) continue;

            eventDefinitions.add(eventDefinition);
        }

        return dungeonContext -> {
            var instance = dungeonContext.getDungeonInstance();
            if (instance == null) return;

            List<EventDefinition<?>> copy = List.copyOf(eventDefinitions);
            copy.forEach(instance::addEventBehaviour);
        };
    }

    private <T> Consumer<T> compressConsumerList(List<Consumer<T>> consumers) {

        if (consumers == null || consumers.isEmpty()) {
            return getEmptyConsumer();
        }

        List<Consumer<T>> copiedList = List.copyOf(consumers);

        return (t) -> copiedList.forEach(consumer -> consumer.accept(t));
    }

    private <T> Consumer<T> getEmptyConsumer() {
        return t -> {};
    }
}