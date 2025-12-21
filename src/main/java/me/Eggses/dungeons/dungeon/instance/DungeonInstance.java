package me.Eggses.dungeons.dungeon.instance;

import me.Eggses.dungeons.dungeon.areas.AreaController;
import me.Eggses.dungeons.dungeon.areas.EntityManager;
import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import me.Eggses.dungeons.dungeon.instance.templates.DungeonTemplate;
import me.Eggses.dungeons.dungeon.players.DungeonPlayers;
import me.Eggses.dungeons.dungeon.portals.PortalController;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.GameRules;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonInstanceCoordinator;
import me.Eggses.dungeons.entities.tasks.TaskManager;
import me.Eggses.dungeons.utility.MessageCreator;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DungeonInstance {

    private final JavaPlugin plugin;
    private final DungeonInstanceCoordinator dungeonInstanceCoordinator;
    private final World dungeonWorld;
    private final String instanceFileName;

    private final AreaController areaController;
    private final InstanceEventHandler instanceEventHandler;
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
        this.areaController = new AreaController(entityManager, new Graveyard(dungeonTemplate.getDefaultGraveyardPosition()), dungeonWorld, dungeonTemplate.getAreaControllerBuilder());
        this.instanceEventHandler = new InstanceEventHandler(this, areaController, entityManager);
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
        player.setGameMode(GameMode.SURVIVAL);
        if (dungeonPlayers.isEmpty()) {
            Bukkit.getScheduler().runTaskLater(plugin, this::tryEndDungeon, 20 * 10);
        }
    }

    public boolean isInDungeon(Player player) {
        return dungeonPlayers.contains(player);
    }

    public PortalController getPortalController() {
        return portalController;
    }

    public InstanceEventHandler getInstanceEventHandler() {
        return instanceEventHandler;
    }

    public World getDungeonWorld() {
        return dungeonWorld;
    }

    public Set<Long> getPortalChunkKeys() {
        return portalController.getChunkKeysEncompassed();
    }
}