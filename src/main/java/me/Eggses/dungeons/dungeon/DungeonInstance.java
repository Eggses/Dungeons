package me.Eggses.dungeons.dungeon;

import me.Eggses.dungeons.configuration.DungeonLog;
import me.Eggses.dungeons.dungeon.players.DungeonPlayers;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.portals.PortalController;
import me.Eggses.dungeons.dungeon.progress.AreaController;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.GameRules;
import me.Eggses.dungeons.entities.taskbehaviour.TaskManager;
import me.Eggses.dungeons.utility.MessageCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.logging.Level;

public abstract class DungeonInstance {

    private static final Logger log = LoggerFactory.getLogger(DungeonInstance.class);
    private final JavaPlugin plugin;
    private final DungeonManager dungeonManager;
    private final DungeonLog dungeonLog;
    private final String templateFileName;
    private final PortalController portalController;
    private final String instanceFileName;
    private final BannedItems bannedItems;
    private final TaskManager taskManager;
    private final MessageCreator messageCreator;

    private final DungeonWorldManager dungeonWorldManager;
    private World dungeonWorld = null;
    private AreaController areaController = null;
    private final DungeonPlayers dungeonPlayers = new DungeonPlayers();

    public DungeonInstance(JavaPlugin plugin,
                           DungeonManager dungeonManager,
                           String dungeonTemplateFileName,
                           String instanceFileName,
                           DungeonPortal dungeonPortal,
                           BannedItems bannedItems,
                           DungeonLog dungeonLog,
                           TaskManager taskManager,
                           MessageCreator messageCreator) {

        this.plugin = plugin;
        this.dungeonManager = dungeonManager;
        this.templateFileName = dungeonTemplateFileName;
        this.portalController = new PortalController(plugin,this, dungeonPortal);
        this.dungeonLog = dungeonLog;
        this.bannedItems = bannedItems;
        this.taskManager = taskManager;
        this.messageCreator = messageCreator;

        this.instanceFileName = instanceFileName;
        this.dungeonWorldManager = new DungeonWorldManager(
                plugin, dungeonTemplateFileName, instanceFileName);

        this.dungeonWorldManager.attemptToCreateInstance(this::onWorldCreated, this::errorCreatingDungeon);
    }

    private void onWorldCreated(World world) {
        this.dungeonWorld = world;
        dungeonManager.addDungeonInstance(this);
        areaController = new AreaController(world, taskManager, messageCreator);
        new GameRules(world).applyRules();
        portalController.openDungeonPortal();
        dungeonManager.addToOpenPortals(this, portalController.getChunkKeysEncompassed());
    }

    private void errorCreatingDungeon(Exception exception) {
        dungeonManager.freeFolderName(instanceFileName);
        plugin.getLogger().log(Level.SEVERE, "Dungeon Failed To Generate: ", exception);
        dungeonLog.addEntry("Dungeon Generation Failure: " + templateFileName + ".");

        Component message = Component
                .text("Dungeon Failed To Generate.")
                .color(TextColor.color(255, 20, 20));

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    public void closeDungeonPortal() {
        if (!portalController.isOpen()) return;

        portalController.closeDungeonPortal();
        dungeonManager.removeFromOpenPortals(portalController.getChunkKeysEncompassed());

        if (dungeonPlayers.isEmpty()) {
            dungeonLog.addEntry("Dungeon was empty after Portal closed. " +
                    "This may be a mistake unless everyone died right at the start somehow.");
            tryEndDungeon();
        }
    }

    private void tryEndDungeon() {

        if (!dungeonPlayers.isEmpty() || portalController.isOpen()) return;

        World mainWorld = Bukkit.getWorlds().getFirst();

        List<String> errors = new ArrayList<>();
        String errorMessage = "Dungeon Set is Empty, Portal is closed, but Dungeon World contains: ";
        for (Player player : dungeonWorld.getPlayers()) {
            String error = errorMessage + player.getName();
            plugin.getLogger().severe(error);
            errors.add(error);

            player.teleport(mainWorld.getSpawnLocation());
        }
        if (!errors.isEmpty()) dungeonLog.addEntryList(errors);

        Bukkit.unloadWorld(dungeonWorld, false);

        dungeonManager.removeDungeonInstance(dungeonWorld);

        dungeonWorldManager.attemptToDeleteInstance(exception -> {
            plugin.getLogger().log(Level.SEVERE, "Dungeon Failed To be Deleted: ", exception);
            dungeonLog.addEntry("Dungeon Deletion Failure: " + templateFileName + ".");
        });

        // end any repeating tasks in here:
    }


    public void addPlayer(Player player) {
        dungeonPlayers.add(player);
        player.setGameMode(GameMode.ADVENTURE);
    }

    public void removePlayer(Player player) {
        dungeonPlayers.remove(player);
        if (dungeonPlayers.isEmpty()) tryEndDungeon();
        player.setGameMode(GameMode.SURVIVAL);
    }

    public boolean isInDungeon(Player player) {
        return dungeonPlayers.contains(player);
    }


    public void handleEntityDeathEvent(UUID uuid) {
        areaController.handleEntityDeathEvent(uuid);
    }

    public void handlePlayerInteractEvent(Position positionOfBlock) {
        areaController.handleInteractEvent(positionOfBlock);
    }

    // PRE CONDITION: Player is in the Dungeon... DO NOT CHECK WORLD
    public void handleMovementEventInDungeon(Player player, Location destination, long chunkKey) {

        areaController.handleMovementEventInDungeon(destination, chunkKey);

        if (portalController.isInPortalInDungeonWorld(destination)) {
            portalController.leaveDungeon(player);
        }
    }

    public void handleMovementEventInWorld(Player player, Location destination) {

        if (!portalController.isOpen()) return; // Should be impossible but critical guard.
        if (!portalController.isInPortalInMainWorld(destination)) return;

        if (bannedItems.hasBannedItems(player)) {
            bannedItems.createBannedItemsMessage(player);
            return;
        }
        portalController.enterDungeon(player, dungeonWorld);
    }

    public void handleDungeonTriggerCommand(int argumentValue) {
        areaController.handleDungeonTriggerCommand(argumentValue);
    }

    public @Nullable World getDungeonWorld() {
        return dungeonWorld;
    }

    public String getInstanceFileName() {
        return instanceFileName;
    }

      /*
    be super carufl with calling stuff as  nothing in dungeon has a stable world... only coords
    so could get weird behaviour if you ever dont pre condition confirm the world.
     */

    /*
         This one, and the one aobve are maybe effected... actually not this one
            more the code above that handles portals regions and the code in the DUngeonmanager for
            instances iwth open portals... esneitally... you have have 1 portal in a Chunk so probably
                have like
                        Map<Long, DUngeon...>

        and you pull by Long which is the chunk ID... its just that multiple longs can
                point to the same instance...

        for the movement code location where you have like

                map<location, key> and map<Long(chunkkey), key> then map<key, dungeonRoom>

                you still have boolean for active dungeon room...

        mutliple rooms cna be in same chunk so ensure that the map is like...

        actaully it has to be like

                Long, Set<Key>... then you can have multiple THINGS in the same map basically...

        I think this emthod is fine but the others ones have to change above... once you
                pull the correct region.. you linear check through the set to work out if a player
                is actually in the correct region... hang on might need to be


                Map<Long, Map<Region, Set<Key>>>....
        Becuase a Long can point to a group of regions... of of each has thier own Key...


        key's gotten from here and the interact map which is map<location of button /lever, KEY>

            can be used in the Map<Key, DungeonRoom


                before you do this fix the protals... note 1 portal in 1 chunk/..

        therefore you can just have

                Map<Chunk, Instance>

                        you pull the dungeon instance with a chunk... THEN do you check further...

        IN BOTH CASES: THE CHUNK IS NOT A 100% KEY, IT JUST NARROWS IT DOWN SO YOU DONT NEED A LINEAR CHECK FOR
                EVERY SINGLE MOVEMENT EVENT.


     */



}