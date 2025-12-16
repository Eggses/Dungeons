package me.Eggses.dungeons.dungeon.instance;

import me.Eggses.dungeons.dungeon.areas.AreaController;
import me.Eggses.dungeons.dungeon.areas.EntityManager;
import me.Eggses.dungeons.dungeon.areas.EventHandler;
import me.Eggses.dungeons.dungeon.instance.configurations.DungeonConfiguration;
import me.Eggses.dungeons.dungeon.players.DungeonPlayers;
import me.Eggses.dungeons.dungeon.portals.PortalController;
import me.Eggses.dungeons.dungeon.regions.Position;
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
                           DungeonConfiguration dungeonConfiguration,
                           String instanceFileName,
                           MessageCreator messageCreator,
                           TaskManager taskManager) {

        this.plugin = plugin;
        this.dungeonInstanceCoordinator = dungeonInstanceCoordinator;
        this.dungeonWorld = dungeonWorld;
        this.instanceFileName = instanceFileName;

        var entityManager = new EntityManager(taskManager, messageCreator);
        this.areaController = new AreaController(entityManager, dungeonWorld, dungeonConfiguration.getAreaControllerBuilder(), dungeonConfiguration.getGraveyardDefinitionList());
        this.eventHandler = new EventHandler(areaController, entityManager);
        this.portalController = new PortalController(plugin, this, dungeonConfiguration.getDungeonPortal(), dungeonConfiguration.getBannedItems());
        this.dungeonPlayers = new DungeonPlayers();

        new GameRules(dungeonWorld).applyRules();
        dungeonConfiguration.getDungeonRules().accept(dungeonWorld);

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

        World mainWorld = Bukkit.getWorlds().getFirst();

        StringBuilder errorMessage = new StringBuilder(
                "Dungeon Set is Empty, Portal is closed, but Dungeon World contains: "
        );

        for (Player player : dungeonWorld.getPlayers()) {
            errorMessage.append(player.getName()).append(" ");
            player.teleport(mainWorld.getSpawnLocation());
        }

        plugin.getLogger().severe(errorMessage.toString());

        areaController.endAllTasks();
        dungeonInstanceCoordinator.destroyInstance(this);
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

    public void handleDungeonTriggerCommand(int argumentValue) {
        eventHandler.handleDungeonTriggerCommand(argumentValue);
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

    public String getInstanceFileName() {
        return instanceFileName;
    }

    public Set<Long> getPortalChunkKeys() {
        return portalController.getChunkKeysEncompassed();
    }
}