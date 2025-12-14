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

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

public abstract class DungeonInstance {

    private final JavaPlugin plugin;
    private final DungeonManager dungeonManager;
    private final DungeonLog dungeonLog;

    private final String templateFileName;

    private final PortalController portalController;

    private final String instanceFileName;
    private final BannedItems bannedItems;
    private final TaskManager taskManager;
    private final MessageCreator messageCreator;
    private final Consumer<World> dungeonRules;

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
                           MessageCreator messageCreator,
                           Consumer<World> dungeonRules) {

        this.plugin = plugin;
        this.dungeonManager = dungeonManager;
        this.templateFileName = dungeonTemplateFileName;
        this.portalController = new PortalController(plugin,this, dungeonPortal);
        this.dungeonLog = dungeonLog;
        this.bannedItems = bannedItems;
        this.taskManager = taskManager;
        this.messageCreator = messageCreator;
        this.dungeonRules = dungeonRules;

        this.instanceFileName = instanceFileName;
        this.dungeonWorldManager = new DungeonWorldManager(
                plugin, dungeonTemplateFileName, instanceFileName);

        this.dungeonWorldManager.attemptToCreateInstance(this::onWorldCreated, this::errorCreatingDungeon);
    }

    private void onWorldCreated(World world) {
        this.dungeonWorld = world;

        dungeonManager.addDungeonInstance(this);

        areaController = new AreaController(world, taskManager, messageCreator);

        GameRules gameRules = new GameRules(world);
        gameRules.applyRules();
        gameRules.applyRules(dungeonRules);

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


    public void handleMovementEventInWorld(Player player, Location destination) {

        if (!portalController.isOpen()) return; // Should be impossible.
        if (!portalController.isInPortalInMainWorld(destination)) return;



        if (bannedItems.hasBannedItems(player)) {
            bannedItems.createBannedItemsMessage(player);
            return;
        }
        portalController.enterDungeon(player, dungeonWorld);
    }

    public void handleMovementEventInDungeon(Player player, Location destination, long chunkKey) {

        areaController.handleMovementEventInDungeon(destination, chunkKey);

        if (portalController.isInPortalInDungeonWorld(destination)) {
            portalController.leaveDungeon(player);
        }
    }

    public void handlePlayerInteractEvent(Position positionOfBlock) {
        areaController.handleInteractEvent(positionOfBlock);
    }

    public void handleDungeonTriggerCommand(int argumentValue) {
        areaController.handleDungeonTriggerCommand(argumentValue);
    }

    public void handleEntityDeathEvent(UUID uuid) {
        areaController.handleEntityDeathEvent(uuid);
    }

    public @Nullable World getDungeonWorld() {
        return dungeonWorld;
    }

    public String getInstanceFileName() {
        return instanceFileName;
    }

    /*
    imagine when world is created... you go to add the dungoen.. you just pass the world + maybe a key...
    and then it makes the instance... sets all these methods etc... split it.... you could also have a sperate
            class for portals porbably.

     */
}

/*
maybe dungeon instance becomes a real class... and subclasses instead are just constants
        that pass in behaviour as it never changes right.. can preconfigure everything?


 */