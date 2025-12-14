package me.Eggses.dungeons.dungeon;

import me.Eggses.dungeons.configuration.DungeonLog;
import me.Eggses.dungeons.dungeon.baseinstance.DungeonConfiguration;
import me.Eggses.dungeons.dungeon.players.DungeonPlayers;
import me.Eggses.dungeons.dungeon.portals.PortalController;
import me.Eggses.dungeons.dungeon.progress.AreaController;
import me.Eggses.dungeons.dungeon.progress.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.regions.Position;
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

public class DungeonInstance {

    private final JavaPlugin plugin;
    private final DungeonManager dungeonManager;

    private final AreaControllerBuilder areaControllerBuilder;
    private final String templateFileName;
    private final Consumer<World> dungeonRules;

    private final String instanceFileName;
    private final MessageCreator messageCreator;
    private final TaskManager taskManager;
    private final DungeonLog dungeonLog;

    private final DungeonWorldManager dungeonWorldManager;
    private World dungeonWorld;
    private final PortalController portalController;
    private AreaController areaController;
    private final DungeonPlayers dungeonPlayers = new DungeonPlayers();

    public DungeonInstance(JavaPlugin plugin,
                           DungeonManager dungeonManager,
                           DungeonConfiguration dungeonConfiguration,
                           String instanceFileName,
                           MessageCreator messageCreator,
                           TaskManager taskManager,
                           DungeonLog dungeonLog) {

        this.plugin = plugin;
        this.dungeonManager = dungeonManager;
        this.areaControllerBuilder = dungeonConfiguration.getAreaControllerBuilder();
        this.templateFileName = dungeonConfiguration.getTemplateName();
        this.dungeonRules = dungeonConfiguration.getDungeonRules();
        this.instanceFileName = instanceFileName;
        this.messageCreator = messageCreator;
        this.taskManager = taskManager;
        this.dungeonLog = dungeonLog;

        this.portalController = new PortalController(
                plugin,
                this,
                dungeonConfiguration.getDungeonPortal(),
                dungeonConfiguration.getBannedItems());

        this.dungeonWorldManager = new DungeonWorldManager(plugin, templateFileName, instanceFileName);
        this.dungeonWorldManager.attemptToCreateInstance(this::onWorldCreated, this::errorCreatingDungeon);
    }

    private void onWorldCreated(World world) {
        this.dungeonWorld = world;

        dungeonManager.addDungeonInstance(this);

        areaController = new AreaController(areaControllerBuilder, world, taskManager, messageCreator);

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
}