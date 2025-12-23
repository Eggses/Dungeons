package me.Eggses.dungeons.dungeon.instance;

import me.Eggses.dungeons.blocks.BlockRegistry;
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
    private final BlockRegistry blockRegistry;

    private final AreaController areaController;
    private final InstanceEventHandler instanceEventHandler;
    private final PortalController portalController;
    private final DungeonPlayers dungeonPlayers;

    public DungeonInstance(JavaPlugin plugin,
                           DungeonInstanceCoordinator dungeonInstanceCoordinator,
                           World dungeonWorld,
                           DungeonTemplate dungeonTemplate,
                           BlockRegistry blockRegistry,
                           String instanceFileName,
                           MessageCreator messageCreator,
                           TaskManager taskManager,
                           BannedItems bannedItems) {

        this.plugin = plugin;
        this.dungeonInstanceCoordinator = dungeonInstanceCoordinator;
        this.dungeonWorld = dungeonWorld;
        this.instanceFileName = instanceFileName;
        this.blockRegistry = blockRegistry;

        var entityManager = new EntityManager(dungeonWorld, taskManager, messageCreator);
        this.areaController = new AreaController(entityManager, new Graveyard(dungeonTemplate.getDefaultGraveyardPosition()), dungeonWorld, blockRegistry, dungeonTemplate.getAreaControllerBuilder());
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
        if (shouldEndDungeon()) endDungeon(true);
    }

    public boolean shouldEndDungeon() {
        return dungeonPlayers.isEmpty() && !portalController.isOpen();
    }

    public void endDungeon(boolean destroyWorldFolder) {
        // Dungeon may be force ended... if so destroy portal.
        if (portalController.isOpen()) {
            portalController.closeDungeonPortal();
            dungeonInstanceCoordinator.closePortal(portalController.getChunkKeysEncompassed());
        }

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
        blockRegistry.removeAllByWorld(dungeonWorld);
        dungeonInstanceCoordinator.destroyInstanceRuntime(this);

        if (destroyWorldFolder) {
            Bukkit.getScheduler().runTaskLater(
                    plugin, () -> dungeonInstanceCoordinator.destroyWorld(instanceFileName), 30 * 20);
        }
    }

    public void addPlayer(Player player) {
        dungeonPlayers.add(player);
        player.setGameMode(GameMode.ADVENTURE);
    }

    public void removePlayer(Player player) {
        dungeonPlayers.remove(player);
        player.setGameMode(GameMode.SURVIVAL);
        if (shouldEndDungeon()) endDungeon(true);
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