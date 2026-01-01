package me.Eggses.dungeons.dungeon.instance;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.dungeon.types.DungeonType;
import me.Eggses.dungeons.dungeon.files.templates.DungeonInstanceTemplate;
import me.Eggses.dungeons.dungeon.areas.AreaController;
import me.Eggses.dungeons.dungeon.areas.EntityManager;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonLifecycleService;
import me.Eggses.dungeons.dungeon.players.DungeonPlayers;
import me.Eggses.dungeons.dungeon.portals.PortalController;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;
import me.Eggses.dungeons.dungeon.utility.GameRules;
import me.Eggses.dungeons.tasks.TaskRunner;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DungeonInstance {

    private final JavaPlugin plugin;
    private final DungeonLifecycleService dungeonLifecycleService;
    private final World dungeonWorld;
    private final String instanceFileName;
    private final BlockRegistry blockRegistry;

    private final AreaController areaController;
    private final InstanceEventHandler instanceEventHandler;
    private final PortalController portalController;
    private final DungeonPlayers dungeonPlayers;
    private final DungeonType dungeonType;

    public DungeonInstance(JavaPlugin plugin,
                           DungeonLifecycleService dungeonLifecycleService,
                           World dungeonWorld,
                           DungeonInstanceTemplate dungeonInstanceTemplate,
                           BlockRegistry blockRegistry,
                           String instanceFileName,
                           MessageCreator messageCreator,
                           TextFormatter textFormatter,
                           TaskRunner taskRunner,
                           BannedItems bannedItems, DungeonType dungeonType) {

        this.plugin = plugin;
        this.dungeonLifecycleService = dungeonLifecycleService;
        this.dungeonWorld = dungeonWorld;
        this.instanceFileName = instanceFileName;
        this.blockRegistry = blockRegistry;
        this.dungeonType = dungeonType;

        var graveyard = dungeonInstanceTemplate.getGraveyard();
        var entityManager = new EntityManager(dungeonWorld, taskRunner, messageCreator, textFormatter);
        this.areaController = new AreaController(entityManager, graveyard, dungeonWorld, blockRegistry, dungeonInstanceTemplate.getAreaControllerBuilder());
        this.instanceEventHandler = new InstanceEventHandler(this, areaController, entityManager);
        this.portalController = new PortalController(plugin, this, dungeonInstanceTemplate.getDungeonPortal(), bannedItems);
        this.dungeonPlayers = new DungeonPlayers();

        new GameRules(dungeonWorld).applyRules();
        dungeonInstanceTemplate.getOnDungeonStart().accept(new DungeonContext(dungeonWorld, entityManager, graveyard, dungeonWorld::getPlayers));

        portalController.openDungeonPortal();
        dungeonLifecycleService.openPortal(this);
    }

    public void closeDungeonPortal() {
        portalController.closeDungeonPortal();
        dungeonLifecycleService.closePortal(this);
        if (shouldEndDungeon()) endDungeon(true);
    }

    public boolean shouldEndDungeon() {
        return dungeonPlayers.isEmpty() && !portalController.isOpen();
    }

    public void endDungeon(boolean destroyWorldFolder) {
        // Dungeon may be force ended... if so destroy portal.
        if (portalController.isOpen()) {
            portalController.closeDungeonPortal();
            dungeonLifecycleService.closePortal(this);
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

        blockRegistry.removeAll(location -> location.getWorld().equals(dungeonWorld));

        dungeonLifecycleService.destroyInstanceRuntime(this, dungeonType);

        if (destroyWorldFolder) {
            Bukkit.getScheduler().runTaskLater(
                    plugin, () -> dungeonLifecycleService.destroyWorld(instanceFileName), 30 * 20);
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