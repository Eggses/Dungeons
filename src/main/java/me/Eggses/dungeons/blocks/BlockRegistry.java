package me.Eggses.dungeons.blocks;

import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.eventhandler.EventManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlockRegistry {

    private final Map<Location, EventManager> customBlocks = new HashMap<>();

    public <E extends Event> void addOrUpdateEventBehaviour(Location locationOfBlock,
                                                            Class<E> eventBehaviourClass,
                                                            EventBehaviour<E> eventBehaviour) {

        EventManager eventManager = customBlocks.computeIfAbsent(locationOfBlock, key -> new EventManager());
        eventManager.addEventBehaviour(eventBehaviourClass, eventBehaviour);
    }

    public <E extends Event> void handleEvent(Location locationOfBlock, E event, EventContext eventContext) {

        EventManager eventManager = customBlocks.get(locationOfBlock);
        if (eventManager == null) return;

        eventManager.handleEvent(event, eventContext);
    }

    public void removeCustomBlock(Location locationOfBlock) {
        customBlocks.remove(locationOfBlock);
    }

    public void removeAllByWorld(World world) {

        Set<Location> locationsOfBlocks = Set.copyOf(customBlocks.keySet());

        for (Location location : locationsOfBlocks) {
            if (world.equals(location.getWorld())) {
                customBlocks.remove(location);
            }
        }
    }
}