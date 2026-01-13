package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.regions.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;

public class DungeonEventRouter {

    private final DungeonRegistry dungeonRegistry;

    public DungeonEventRouter(DungeonRegistry dungeonRegistry) {
        this.dungeonRegistry = dungeonRegistry;
    }

    public <E extends Event> void handleEvent(E event, World world) {
        DungeonInstance dungeonInstance = dungeonRegistry.getDungeonInstance(world);
        if (dungeonInstance != null) dungeonInstance.handleEvent(event);
    }

    public void handleDungeonTriggerCommand(Location locationOfBlock) {
        DungeonInstance dungeonInstance = dungeonRegistry.getDungeonInstance(locationOfBlock.getWorld());
        if (dungeonInstance != null) dungeonInstance.handleDungeonTriggerCommand(new Position(locationOfBlock));
    }
}
