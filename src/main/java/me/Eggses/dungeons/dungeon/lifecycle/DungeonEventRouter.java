package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.*;

import java.util.Set;
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

    public void handleMovementEvent(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        Location destination = event.getTo();
        long chunkKeyOfDestination = event.getTo().getChunk().getChunkKey();

        boolean ran = runIfInstanceExists(destination.getWorld(),
                (instance) -> instance.getInstanceEventHandler().handleMovementEventInDungeon(player, destination, chunkKeyOfDestination));
        if (ran) return;

        Set<DungeonInstance> dungeonInstances = dungeonOpenPortalRegistry.getDungeonInstance(chunkKeyOfDestination);
        if (dungeonInstances == null) return;
        for (DungeonInstance dungeonInstance : dungeonInstances) {
            dungeonInstance.getInstanceEventHandler().handleMovementEventOutsideDungeon(player, destination);
        }
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


    public void handlePlayerChangeWorldEvent(PlayerChangedWorldEvent event) {

        Player player = event.getPlayer();

        World originalWorld = event.getFrom();
        World currentWorld = player.getWorld();

        runIfInstanceExists(originalWorld, (instance) -> instance.removePlayer(player));
        runIfInstanceExists(currentWorld, (instance) -> instance.addPlayer(player));
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

    public void handleExplosionPrimeEvent(ExplosionPrimeEvent event) {
        World world = event.getEntity().getWorld();
        runIfInstanceExists(world, (instance) -> instance.getInstanceEventHandler().handleExplosionPrimeEvent(event));
    }

    private boolean runIfInstanceExists(World world, Consumer<DungeonInstance> action) {
        DungeonInstance dungeonInstance = dungeonRegistry.getDungeonInstance(world);
        if (dungeonInstance == null) return false;

        action.accept(dungeonInstance);
        return true;
    }
}