package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.regions.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;
import java.util.function.Consumer;

public class DungeonEventRouter {

    private final DungeonRegistry dungeonRegistry;
    private final DungeonOpenPortalRegistry dungeonOpenPortalRegistry;

    public DungeonEventRouter(DungeonRegistry dungeonRegistry, DungeonOpenPortalRegistry dungeonOpenPortalRegistry) {
        this.dungeonRegistry = dungeonRegistry;
        this.dungeonOpenPortalRegistry = dungeonOpenPortalRegistry;
    }

    public void handleMovementEventInWorld(Player player, Location destination, long chunkKey) {

        DungeonInstance dungeonInstance = dungeonOpenPortalRegistry.getDungeonInstance(chunkKey);
        if (dungeonInstance == null) return;

        dungeonInstance.handleMovementEventInWorld(player, destination);
    }

    public void handleMovementEventInDungeon(Player player, Location destination, long chunkKey) {
        runIfInstanceExists(destination.getWorld(),
                (instance) ->
                        instance.handleMovementEventInDungeon(player, destination, chunkKey));
    }

    public void handleInteractEventInDungeon(World world, Position positionOfBlock) {
        runIfInstanceExists(world, (instance) -> instance.handlePlayerInteractEvent(positionOfBlock));
    }

    public void handleDungeonTriggerCommand(World world, int argumentValue) {
        runIfInstanceExists(world, (instance) -> instance.handleDungeonTriggerCommand(argumentValue));
    }

    public void handleEntityDeathEventInDungeon(World world, UUID uuid) {
        runIfInstanceExists(world, (instance) -> instance.handleEntityDeathEvent(uuid));
    }

    public void handlePlayerChangeWorldEvent(Player player, World worldLeft, World worldEntered) {
        runIfInstanceExists(worldLeft, (instance) -> instance.removePlayer(player));
        runIfInstanceExists(worldEntered, (instance) -> instance.addPlayer(player));
    }

    public void handlePlayerExitGameEvent(Player player) {
        runIfInstanceExists(player.getWorld(), (instance) -> instance.removePlayer(player));
    }

    public void handleEntityDamageEntityEvent(World world, EntityDamageByEntityEvent event) {
        runIfInstanceExists(world, (instance) -> instance.handleEntityDamageEntityEvent(event));
    }

    private void runIfInstanceExists(World world, Consumer<DungeonInstance> action) {
        DungeonInstance dungeonInstance = dungeonRegistry.getDungeonInstance(world);
        if (dungeonInstance == null) return;

        action.accept(dungeonInstance);
    }
}