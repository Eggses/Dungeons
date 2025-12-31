package me.Eggses.dungeons.blocks;

import me.Eggses.dungeons.dispatch.EventManagerRegistry;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.tasks.definitions.TaskBehaviour;
import me.Eggses.dungeons.tasks.running.ActiveTasks;
import me.Eggses.dungeons.tasks.running.TaskManager;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BlockRegistry {

    private final TaskManager taskManager;

    private final EventManagerRegistry<Location> blockEventRegistry = new EventManagerRegistry<>();
    private final Map<Location, TextDisplay> blockTextDisplayRegistry = new HashMap<>();
    private final Map<Location, ActiveTasks<Location>> blockActiveTaskRegistry = new HashMap<>();

    public BlockRegistry(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public <E extends Event> void addBlockAndEvent(Location location,
                                           Class<E> eventClass,
                                           EventBehaviour<E> eventBehaviour) {

        blockEventRegistry.addOrUpdate(location, eventClass, eventBehaviour);
    }

    public void addBlockAndName(Location location, Consumer<TextDisplay> settings) {
        TextDisplay textDisplay = blockTextDisplayRegistry.get(location);
        if (textDisplay == null) {
            Location spawnLocation = location.clone().add(0.5, 1.2, 0.5);
            textDisplay = location.getWorld().spawn(spawnLocation, TextDisplay.class);
            blockTextDisplayRegistry.put(location, textDisplay);
        }
        settings.accept(textDisplay);
    }

    public void addBlockAndTaskBehaviour(Location location, TaskBehaviour<Location> taskBehaviour) {

        blockActiveTaskRegistry.putIfAbsent(location, new ActiveTasks<>());
        ActiveTasks<Location> blockTasks = blockActiveTaskRegistry.get(location);
        blockTasks.endAllWithoutClearing();
        blockTasks.addAndRunTasks(location, taskBehaviour, taskManager);
    }

    public void remove(Location location) {
        blockEventRegistry.remove(location);

        TextDisplay textDisplay = blockTextDisplayRegistry.remove(location);
        if (textDisplay != null) textDisplay.remove();

        ActiveTasks<Location> blockTasks = blockActiveTaskRegistry.remove(location);
        if (blockTasks != null) blockTasks.endAllTasks();
    }

    public void removeAll(Predicate<Location> locationPredicate) {

        Set<Location> allKeys = new HashSet<>();
        allKeys.addAll(blockEventRegistry.getKeySet());
        allKeys.addAll(blockTextDisplayRegistry.keySet());
        allKeys.addAll(blockActiveTaskRegistry.keySet());

        for (Location location : Set.copyOf(allKeys)) {
            if (locationPredicate.test(location)) {
                remove(location);
            }
        }
    }

    public <E extends Event> void handleEvent(Location location, E event, EventContext eventContext) {
        blockEventRegistry.handleEvent(location, event, eventContext);
    }
}
