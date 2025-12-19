package me.Eggses.dungeons.dungeon.instance;

import me.Eggses.dungeons.dungeon.areas.AreaController;
import me.Eggses.dungeons.dungeon.areas.EntityManager;
import me.Eggses.dungeons.dungeon.areas.EventHandler;
import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import me.Eggses.dungeons.dungeon.instance.configurations.DungeonTemplate;
import me.Eggses.dungeons.dungeon.players.DungeonPlayers;
import me.Eggses.dungeons.dungeon.portals.PortalController;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.GameRules;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonInstanceCoordinator;
import me.Eggses.dungeons.entities.taskbehaviour.TaskManager;
import me.Eggses.dungeons.utility.MessageCreator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DungeonInstance {

    private final JavaPlugin plugin;
    private final DungeonInstanceCoordinator dungeonInstanceCoordinator;
    private final World dungeonWorld;
    private final String instanceFileName;

    private final AreaController areaController;
    private final EventHandler eventHandler;
    private final PortalController portalController;
    private final DungeonPlayers dungeonPlayers;

    public DungeonInstance(JavaPlugin plugin,
                           DungeonInstanceCoordinator dungeonInstanceCoordinator,
                           World dungeonWorld,
                           DungeonTemplate dungeonTemplate,
                           String instanceFileName,
                           MessageCreator messageCreator,
                           TaskManager taskManager,
                           BannedItems bannedItems) {

        this.plugin = plugin;
        this.dungeonInstanceCoordinator = dungeonInstanceCoordinator;
        this.dungeonWorld = dungeonWorld;
        this.instanceFileName = instanceFileName;

        var entityManager = new EntityManager(dungeonWorld, taskManager, messageCreator);
        this.areaController = new AreaController(entityManager, new Graveyard(), dungeonWorld, dungeonTemplate.getAreaControllerBuilder());
        this.eventHandler = new EventHandler(areaController, entityManager);
        this.portalController = new PortalController(plugin, this, dungeonTemplate.getDungeonPortal(), bannedItems);
        this.dungeonPlayers = new DungeonPlayers();

        new GameRules(dungeonWorld).applyRules();
        dungeonTemplate.getDungeonRules().accept(dungeonWorld);

        portalController.openDungeonPortal();
        dungeonInstanceCoordinator.openPortal(this, portalController.getChunkKeysEncompassed());
    }

    public void closeDungeonPortal() {
        portalController.closeDungeonPortal();
        dungeonInstanceCoordinator.closePortal(portalController.getChunkKeysEncompassed());
        tryEndDungeon();
    }

    private void tryEndDungeon() {
        if (!dungeonPlayers.isEmpty() || portalController.isOpen()) return;
        endInstanceRuntime();
        dungeonInstanceCoordinator.destroyWorld(instanceFileName);
    }

    public void forceEndDungeonInstance(boolean destroyWorldFolder) {
        portalController.closeDungeonPortal();
        dungeonInstanceCoordinator.closePortal(portalController.getChunkKeysEncompassed());
        endInstanceRuntime();
        if (destroyWorldFolder) dungeonInstanceCoordinator.destroyWorld(instanceFileName);
    }

    private void endInstanceRuntime() {
        World mainWorld = Bukkit.getWorld("world");
        if (mainWorld == null) mainWorld = Bukkit.getWorlds().getFirst();

        List<String> players = new ArrayList<>();

        for (Player player : dungeonWorld.getPlayers()) {
            players.add(player.getName());
            player.teleport(mainWorld.getSpawnLocation());
        }

        if (!players.isEmpty()) {
            String error = "Dungeon Ended with the following Players: ";
            String playerList = String.join(", ", players);

            plugin.getLogger().severe(error + playerList);
        }

        areaController.endAllTasks();
        dungeonInstanceCoordinator.destroyInstanceRuntime(this);
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

        eventHandler.handleMovementEventInDungeon(destination, chunkKey);

        if (portalController.isInPortalInDungeonWorld(destination)) {
            portalController.leaveDungeon(player);
        }
    }

    public void handlePlayerInteractEvent(Position positionOfBlock) {
        eventHandler.handleInteractEvent(positionOfBlock);
    }

    public void handleDungeonTriggerCommand(String argument) {
        eventHandler.handleDungeonTriggerCommand(argument);
    }

    public void handleEntityDeathEvent(UUID uuid) {
        eventHandler.handleEntityDeathEvent(uuid);
    }

    public void handleEntityDamageEntityEvent(EntityDamageByEntityEvent event) {
        eventHandler.handleEntityDamageEntityEvent(event);
    }

    public void handlePlayerRespawnEvent(PlayerRespawnEvent playerRespawnEvent) {
        eventHandler.handlePlayerRespawnEvent(playerRespawnEvent);
    }

    public World getDungeonWorld() {
        return dungeonWorld;
    }

    public Set<Long> getPortalChunkKeys() {
        return portalController.getChunkKeysEncompassed();
    }
}