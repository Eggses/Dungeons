package me.Eggses.dungeons.dungeon.instance;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.dungeon.bosses.controller.BossArenaController;
import me.Eggses.dungeons.dungeon.files.PlayerStats;
import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.types.DungeonType;
import me.Eggses.dungeons.dungeon.files.templates.DungeonInstanceTemplate;
import me.Eggses.dungeons.dungeon.areas.AreaController;
import me.Eggses.dungeons.entities.mobs.EntityManager;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonLifecycleService;
import me.Eggses.dungeons.dungeon.players.Players;
import me.Eggses.dungeons.dungeon.portals.PortalController;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;
import me.Eggses.dungeons.dungeon.utility.DungeonGameRules;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventDefinition;
import me.Eggses.dungeons.tasks.TaskRunner;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DungeonInstance {

    private final JavaPlugin plugin;
    private final DungeonLifecycleService dungeonLifecycleService;
    private final World dungeonWorld;
    private final String instanceFileName;
    private final BlockRegistry blockRegistry;
    private final AreaController areaController;
    private final InstanceEventHandler instanceEventHandler;
    private final PortalController portalController;
    private final Players players;
    private final DungeonType dungeonType;
    private final PlayerStats playerStats;
    private long dungeonStartTime = 0;

    public DungeonInstance(JavaPlugin plugin,
                           DungeonLifecycleService dungeonLifecycleService,
                           World dungeonWorld,
                           DungeonInstanceTemplate dungeonInstanceTemplate,
                           BlockRegistry blockRegistry,
                           String instanceFileName,
                           MessageCreator messageCreator,
                           TextFormatter textFormatter,
                           TaskRunner taskRunner,
                           BannedItems bannedItems,
                           DungeonType dungeonType,
                           PlayerStats playerStats) {

        this.plugin = plugin;
        this.dungeonLifecycleService = dungeonLifecycleService;
        this.dungeonWorld = dungeonWorld;
        this.instanceFileName = instanceFileName;
        this.blockRegistry = blockRegistry;
        this.dungeonType = dungeonType;
        this.playerStats = playerStats;

        var graveyard = new Graveyard();
        var entityManager = new EntityManager(dungeonWorld, taskRunner, messageCreator, textFormatter);
        this.areaController = new AreaController(this, entityManager, graveyard, dungeonWorld, blockRegistry, dungeonInstanceTemplate.getAreaControllerBuilder());
        this.portalController = new PortalController(plugin, this, dungeonInstanceTemplate.getDungeonPortal(), bannedItems);
        this.players = new Players();

        new DungeonGameRules(dungeonWorld).applyRules();

        var dungeonContext = DungeonContext.builder()
                .dungeonInstance(this)
                .world(dungeonWorld)
                .entityManager(entityManager)
                .graveyard(graveyard)
                .players(dungeonWorld::getPlayers)
                .build();

        var bossTemplate = dungeonInstanceTemplate.getBossArenaTemplate();
        BossArenaController bossArenaController = new BossArenaController(
                plugin,
                this,
                dungeonContext,
                dungeonWorld,
                entityManager,
                taskRunner,
                messageCreator,
                textFormatter,
                bossTemplate.entryRegion(),
                bossTemplate.playerSpawningRotationPosition(),
                bossTemplate.bossBuilderSupplier(),
                bossTemplate.onBossDefeat()
        );
        this.instanceEventHandler = new InstanceEventHandler(this, areaController, entityManager, bossArenaController);

        dungeonInstanceTemplate.getOnDungeonStart().accept(dungeonContext);

        portalController.openDungeonPortal();
        dungeonLifecycleService.openPortal(portalController);
    }

    public void closeDungeonPortal() {
        portalController.closeDungeonPortal();
        dungeonLifecycleService.closePortal(portalController, dungeonType);
        if (shouldEndDungeon()) endDungeon(true);
    }

    public boolean shouldEndDungeon() {
        return players.isEmpty() && !portalController.isOpen();
    }

    public void endDungeon(boolean destroyWorldFolder) {
        // Dungeon may be force ended... if so destroy portal.
        if (portalController.isOpen()) {
            portalController.closeDungeonPortal();
            dungeonLifecycleService.closePortal(portalController, dungeonType);
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

        blockRegistry.removeAll(worldPosition -> worldPosition.getWorld().equals(dungeonWorld));

        dungeonLifecycleService.destroyInstanceRuntime(this);

        if (destroyWorldFolder) {
            Bukkit.getScheduler().runTaskLater(
                    plugin, () -> dungeonLifecycleService.destroyWorld(instanceFileName), 500 * 20);
        }
    }

    public void addPlayer(Player player) {
        players.add(player);
        player.setGameMode(GameMode.ADVENTURE);
        if (dungeonStartTime == 0) {
            dungeonStartTime = System.currentTimeMillis();
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        player.setGameMode(GameMode.SURVIVAL);
        if (shouldEndDungeon()) endDungeon(true);
    }

    public boolean isInDungeon(Player player) {
        return players.contains(player);
    }

    public PortalController getPortalController() {
        return portalController;
    }

    public <E extends Event> void handleEvent(E event) {
        instanceEventHandler.handleEvent(event);
    }

    public <E extends Event> void addEventBehaviour(EventDefinition<E> eventDefinition) {

        Class<E> eventClass = eventDefinition.eventClass();
        EventBehaviour<E> eventBehaviour = eventDefinition.createEventBehaviour().get();

        instanceEventHandler.addEventBehaviour(eventClass, eventBehaviour);
    }

    public void handleDungeonTriggerCommand(Position positionOfBlock) {
        areaController.handleDungeonTriggerCommand(positionOfBlock);
    }

    public World getDungeonWorld() {
        return dungeonWorld;
    }

    public void defeatDungeon() {
        long timeTaken = System.currentTimeMillis() - dungeonStartTime;
        for (Player player : players.getPlayers()) {
            playerStats.addOrUpdateEntry(player, dungeonType, timeTaken);
            System.out.println("Updated stats for " + player.getName());
        }
    }
}
