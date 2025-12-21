package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.function.Consumer;

public class DungeonEventRouter {

    private final DungeonRegistry dungeonRegistry;
    private final DungeonOpenPortalRegistry dungeonOpenPortalRegistry;

    public DungeonEventRouter(DungeonRegistry dungeonRegistry, DungeonOpenPortalRegistry dungeonOpenPortalRegistry) {
        this.dungeonRegistry = dungeonRegistry;
        this.dungeonOpenPortalRegistry = dungeonOpenPortalRegistry;
    }

    /* =========================================================
     * Core Dungeon Area Control Events
     * ========================================================= */

    public void handleMovementEvent(Player player, Location destination, long chunkKey) {

        boolean ran = runIfInstanceExists(destination.getWorld(),
                (instance) -> instance.getInstanceEventHandler().handleMovementEventInDungeon(player, destination, chunkKey));
        if (ran) return;

        DungeonInstance dungeonInstance = dungeonOpenPortalRegistry.getDungeonInstance(chunkKey);
        if (dungeonInstance == null) return;
        dungeonInstance.getInstanceEventHandler().handleMovementEventOutsideDungeon(player, destination);
    }

    public void handlePlayerInteractEvent(PlayerInteractEvent event) {
        World world = event.getPlayer().getWorld();
        runIfInstanceExists(world, (instance) -> instance.getInstanceEventHandler().handlePlayerInteractEvent(event));
    }

    public void handleDungeonTriggerCommand(World world, String argument) {
        runIfInstanceExists(world, (instance) -> instance.getInstanceEventHandler().handleDungeonTriggerCommand(argument));
    }

    /* =========================================================
     * Other Player Events
     * ========================================================= */

    public void handlePlayerRespawnEvent(PlayerRespawnEvent event) {
        World world = event.getPlayer().getWorld();
        runIfInstanceExists(world, (instance) -> instance.getInstanceEventHandler().handlePlayerRespawnEvent(event));
    }

    public void handlePlayerQuitEvent(PlayerQuitEvent event) {
        World world = event.getPlayer().getWorld();
        runIfInstanceExists(world, (instance) -> instance.getInstanceEventHandler().handlePlayerQuitEvent(event));
    }


    public void handlePlayerChangeWorldEvent(Player player, World worldLeft, World worldEntered) {
        runIfInstanceExists(worldLeft, (instance) -> instance.removePlayer(player));
        runIfInstanceExists(worldEntered, (instance) -> instance.addPlayer(player));
    }

    /* =========================================================
     * Entity Events
     * ========================================================= */

    public void handleEntityDeathEvent(EntityDeathEvent event) {
        World world = event.getEntity().getWorld();
        runIfInstanceExists(world, (instance) -> instance.getInstanceEventHandler().handleEntityDeathEvent(event));
    }

    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        World world = event.getEntity().getWorld();
        runIfInstanceExists(world, (instance) -> instance.getInstanceEventHandler().handleEntityDamageByEntityEvent(event));
    }

    public void handleEntityExplodeEvent(EntityExplodeEvent event) {
        World world = event.getLocation().getWorld();
        runIfInstanceExists(world, (instance) -> instance.getInstanceEventHandler().handleEntityExplodeEvent(event));
    }

    private boolean runIfInstanceExists(World world, Consumer<DungeonInstance> action) {
        DungeonInstance dungeonInstance = dungeonRegistry.getDungeonInstance(world);
        if (dungeonInstance == null) return false;

        action.accept(dungeonInstance);
        return true;
    }
}