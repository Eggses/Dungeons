package me.Eggses.dungeons.blocks;

import me.Eggses.dungeons.dispatch.EventManagerRegistry;
import me.Eggses.dungeons.eventinvoker.EventContext;
import me.Eggses.dungeons.eventinvoker.Invoker;
import me.Eggses.dungeons.tasks.ActiveTasks;
import me.Eggses.dungeons.tasks.TaskContext;
import me.Eggses.dungeons.tasks.TaskRunner;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class BlockRegistry {

    private final TaskRunner taskRunner;

    private final EventManagerRegistry<Location> blockEventRegistry;
    private final Map<Location, TextDisplay> blockTextDisplayRegistry = new HashMap<>();
    private final Map<Location, ActiveTasks> blockActiveTaskRegistry = new HashMap<>();

    public BlockRegistry(TaskRunner taskRunner, Logger logger) {
        this.taskRunner = taskRunner;
        blockEventRegistry = new EventManagerRegistry<>(logger);
    }

    public void addBlockAndEvent(Location location, Invoker invoker) {
        blockEventRegistry.addOrUpdate(location, invoker);
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

    public void addBlockAndTaskBehaviour(Location location, Consumer<TaskContext<Location>> task) {

        blockActiveTaskRegistry.putIfAbsent(location, new ActiveTasks());
        ActiveTasks activeTasks = blockActiveTaskRegistry.get(location);

        TaskContext<Location> taskContext = new TaskContext<>(location, activeTasks, taskRunner);
        task.accept(taskContext);
    }

    public void remove(Location location) {
        blockEventRegistry.remove(location);

        TextDisplay textDisplay = blockTextDisplayRegistry.remove(location);
        if (textDisplay != null) textDisplay.remove();

        ActiveTasks blockTasks = blockActiveTaskRegistry.remove(location);
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

    public void handleEvent(Location location, Event event, EventContext eventContext) {
        blockEventRegistry.handleEvent(location, event, eventContext);
    }
}
