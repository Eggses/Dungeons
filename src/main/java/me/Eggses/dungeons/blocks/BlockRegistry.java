package me.Eggses.dungeons.blocks;

import me.Eggses.dungeons.dispatch.EventManagerRegistry;
import me.Eggses.dungeons.dungeon.regions.WorldPosition;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.tasks.ActiveTasks;
import me.Eggses.dungeons.tasks.Task;
import me.Eggses.dungeons.tasks.TaskContext;
import me.Eggses.dungeons.tasks.TaskRunner;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class BlockRegistry {

    private final TaskRunner taskRunner;

    private final EventManagerRegistry<WorldPosition> blockEventRegistry = new EventManagerRegistry<>();
    private final Map<WorldPosition, TextDisplay> blockTextDisplayRegistry = new HashMap<>();
    private final Map<WorldPosition, ActiveTasks> blockActiveTaskRegistry = new HashMap<>();

    public BlockRegistry(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    public <E extends Event> void addBlockAndEvent(WorldPosition worldPosition,
                                           Class<E> eventClass,
                                           EventBehaviour<E> eventBehaviour) {

        blockEventRegistry.addOrUpdate(worldPosition, eventClass, eventBehaviour);
    }

    public void addBlockAndName(WorldPosition worldPosition, Component name) {
        TextDisplay textDisplay = blockTextDisplayRegistry.get(worldPosition);

        if (textDisplay == null) {
            Location spawnLocation = worldPosition.toLocation().add(0.5, 1.2, 0.5);
            textDisplay = spawnLocation.getWorld().spawn(spawnLocation, TextDisplay.class);
            textDisplay.setBillboard(Display.Billboard.CENTER);
            textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
            blockTextDisplayRegistry.put(worldPosition, textDisplay);
        }
        textDisplay.text(name);
    }

    public void addBlockAndTaskBehaviour(WorldPosition worldPosition, Task<WorldPosition> task) {
        blockActiveTaskRegistry.putIfAbsent(worldPosition, new ActiveTasks());
        ActiveTasks activeTasks = blockActiveTaskRegistry.get(worldPosition);

        TaskContext<WorldPosition> taskContext = new TaskContext<>(worldPosition, activeTasks, taskRunner);
        task.runTask(taskContext);
    }

    public void remove(WorldPosition worldPosition) {

        blockEventRegistry.remove(worldPosition);

        removeTextDisplay(worldPosition);

        ActiveTasks blockTasks = blockActiveTaskRegistry.remove(worldPosition);
        if (blockTasks != null) blockTasks.endAllTasks();
    }

    public void removeAll(Predicate<WorldPosition> worldPositionPredicate) {

        Set<WorldPosition> allKeys = new HashSet<>();
        allKeys.addAll(blockEventRegistry.getKeySet());
        allKeys.addAll(blockTextDisplayRegistry.keySet());
        allKeys.addAll(blockActiveTaskRegistry.keySet());

        for (WorldPosition worldPosition : Set.copyOf(allKeys)) {
            if (worldPositionPredicate.test(worldPosition)) {
                remove(worldPosition);
            }
        }
    }

    public <E extends Event> void handleEvent(WorldPosition worldPosition, E event, EventContext eventContext) {
        blockEventRegistry.handleEvent(worldPosition, event, eventContext);
    }

    private void removeTextDisplay(WorldPosition worldPosition) {

        TextDisplay textDisplay = blockTextDisplayRegistry.remove(worldPosition);
        if (textDisplay != null) {

            Chunk chunk = textDisplay.getChunk();
            if (!chunk.isLoaded()) {
                chunk.load();
            }
            textDisplay.remove();
        }
    }

    public void removeAllTextDisplays() {
        Set.copyOf(blockTextDisplayRegistry.keySet()).forEach(this::removeTextDisplay);
    }
}
