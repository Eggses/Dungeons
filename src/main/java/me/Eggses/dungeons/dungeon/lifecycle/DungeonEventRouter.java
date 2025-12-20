package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.function.Consumer;

public class DungeonEventRouter {

    private final DungeonRegistry dungeonRegistry;
    private final DungeonOpenPortalRegistry dungeonOpenPortalRegistry;

    public DungeonEventRouter(DungeonRegistry dungeonRegistry, DungeonOpenPortalRegistry dungeonOpenPortalRegistry) {
        this.dungeonRegistry = dungeonRegistry;
        this.dungeonOpenPortalRegistry = dungeonOpenPortalRegistry;
    }

    public void handleMovementEvent(Player player, Location destination, long chunkKey) {
        boolean ran = runIfInstanceExists(destination.getWorld(),
                (instance) -> instance.handleMovementEventInDungeon(player, destination, chunkKey));
        if (ran) return;

        DungeonInstance dungeonInstance = dungeonOpenPortalRegistry.getDungeonInstance(chunkKey);
        if (dungeonInstance == null) return;
        dungeonInstance.handleMovementEventOutsideDungeon(player, destination);
    }

    public void handleEvent(World world, Event event) {
        runIfInstanceExists(world, (instance -> instance.handleEvent(event)));
    }

    public void handleDungeonTriggerCommand(World world, String argument) {
        runIfInstanceExists(world, (instance) -> instance.handleDungeonTriggerCommand(argument));
    }

    public void handlePlayerChangeWorldEvent(Player player, World worldLeft, World worldEntered) {
        runIfInstanceExists(worldLeft, (instance) -> instance.removePlayer(player));
        runIfInstanceExists(worldEntered, (instance) -> instance.addPlayer(player));
    }

    private boolean runIfInstanceExists(World world, Consumer<DungeonInstance> action) {
        DungeonInstance dungeonInstance = dungeonRegistry.getDungeonInstance(world);
        if (dungeonInstance == null) return false;

        action.accept(dungeonInstance);
        return true;
    }
}