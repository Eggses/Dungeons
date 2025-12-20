package me.Eggses.dungeons.dungeon.instance;

import me.Eggses.dungeons.dungeon.areas.AreaController;
import me.Eggses.dungeons.dungeon.areas.EntityManager;
import me.Eggses.dungeons.dungeon.areas.EntityAbilityEventHandler;
import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import me.Eggses.dungeons.dungeon.instance.configurations.DungeonTemplate;
import me.Eggses.dungeons.dungeon.players.DungeonPlayers;
import me.Eggses.dungeons.dungeon.portals.PortalController;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.GameRules;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonInstanceCoordinator;
import me.Eggses.dungeons.entities.tasks.TaskManager;
import me.Eggses.dungeons.utility.MessageCreator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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
    private final EntityAbilityEventHandler entityAbilityEventHandler;
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
        this.entityAbilityEventHandler = new EntityAbilityEventHandler(entityManager);
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

    public void handleMovementEventOutsideDungeon(Player player, Location destination) {

        if (!portalController.isOpen()) return;

        if (portalController.isInPortalOutsideDungeon(destination)) {
            portalController.enterDungeon(player, dungeonWorld);
        }
    }

    public void handleMovementEventInDungeon(Player player, Location destination, long chunkKey) {

        areaController.handlePlayerMoveEvent(destination, chunkKey);

        if (portalController.isInPortalInDungeonWorld(destination)) {
            portalController.leaveDungeon(player);
        }
    }

    public void handleEvent(Event event) {
        switch (event) {
            case EntityDamageByEntityEvent e -> entityAbilityEventHandler.handleEntityDamageEntityEvent(e);
            case EntityExplodeEvent e -> entityAbilityEventHandler.handleEntityExplodeEvent(e);
            case EntityDeathEvent e -> areaController.handleEntityDeathEvent(e.getEntity().getUniqueId());
            case PlayerRespawnEvent e -> areaController.handlePlayerRespawnEvent(e);
            case PlayerInteractEvent e -> this.handlePlayerInteractEvent(e);
            case PlayerQuitEvent e -> this.removePlayer(e.getPlayer());
            default -> {}
        }
    }

    public void handleDungeonTriggerCommand(String argument) {
        areaController.handleDungeonTriggerCommand(argument);
    }

    private void handlePlayerInteractEvent(PlayerInteractEvent event) {

        Block block = event.getClickedBlock();
        if (block == null) return;

        Action action = event.getAction();
        Material type = block.getType();
        String name = type.name();

        boolean trigger = (action == Action.RIGHT_CLICK_BLOCK && (type == Material.LEVER || name.endsWith("_BUTTON")))
                || (action == Action.PHYSICAL && name.endsWith("_PRESSURE_PLATE"));

        if (!trigger) return;

        areaController.handleInteractEvent(new Position(block.getX(), block.getY(), block.getZ()));
    }

    public World getDungeonWorld() {
        return dungeonWorld;
    }

    public Set<Long> getPortalChunkKeys() {
        return portalController.getChunkKeysEncompassed();
    }
}