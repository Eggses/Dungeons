package me.Eggses.dungeons.blocks;

import me.Eggses.dungeons.dungeon.regions.WorldPosition;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.eventhandler.EventManager;
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
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class BlockRegistry {

    private final TaskRunner taskRunner;

    private final Map<WorldPosition, BlockHandler> blocks = new HashMap<>();

    public BlockRegistry(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    public <E extends Event> void addBlockAndEvent(WorldPosition worldPosition,
                                                   Class<E> eventClass,
                                                   EventBehaviour<E> eventBehaviour) {

        BlockHandler blockHandler = getOrCreateBlockHandlerFor(worldPosition);
        blockHandler.eventManager.addEventBehaviour(eventClass, eventBehaviour);
    }

    public void addBlockAndTextDisplay(WorldPosition worldPosition, Component name) {
        BlockHandler blockHandler = getOrCreateBlockHandlerFor(worldPosition);
        TextDisplay textDisplay = blockHandler.textDisplayManager.getTextDisplay();
        textDisplay.text(name);
    }

    public void addBlockAndTaskBehaviour(WorldPosition worldPosition, Task<WorldPosition> blockTask) {
        BlockHandler blockHandler = getOrCreateBlockHandlerFor(worldPosition);
        var taskContext = new TaskContext<>(worldPosition, blockHandler.activeTasks, taskRunner);
        blockTask.runTask(taskContext);
    }

    private BlockHandler getOrCreateBlockHandlerFor(WorldPosition worldPosition) {
        return blocks.computeIfAbsent(worldPosition, key
                -> new BlockHandler(new EventManager(), new TextDisplayManager(key), new ActiveTasks())
        );
    }

    public <E extends Event> void handleEvent(WorldPosition worldPosition, E event, EventContext eventContext) {
        BlockHandler blockHandler = blocks.get(worldPosition);
        if (blockHandler == null) return;
        blockHandler.eventManager.handleEvent(event, eventContext);
    }

    public void remove(WorldPosition worldPosition) {

        BlockHandler blockHandler = blocks.remove(worldPosition);
        if (blockHandler == null) return;

        // Only need some clean up - GC destroys.
        blockHandler.textDisplayManager.remove();
        blockHandler.activeTasks.endAllTasks();
    }

    public void removeAll(Predicate<WorldPosition> worldPositionPredicate) {

        Set<WorldPosition> worldPositions = Set.copyOf(blocks.keySet());

        for (WorldPosition worldPosition : worldPositions) {
            if (worldPositionPredicate.test(worldPosition)) {
                remove(worldPosition);
            }
        }
    }

    public void removeAllTextDisplays() {
        for (BlockHandler blockHandler : blocks.values()) {
            blockHandler.textDisplayManager.remove();
        }
    }

    private record BlockHandler(EventManager eventManager, TextDisplayManager textDisplayManager, ActiveTasks activeTasks) { }

    private static class TextDisplayManager {

        private final WorldPosition worldPosition;
        private TextDisplay textDisplay;

        private TextDisplayManager(WorldPosition worldPosition) {
            this.worldPosition = worldPosition;
        }

        public TextDisplay getTextDisplay() {
            if (textDisplay == null || !textDisplay.isValid()) {
                createTextDisplayAt();
            }
            return textDisplay;
        }

        private void createTextDisplayAt() {
            Location spawnLocation = worldPosition.toLocation().add(0.5, 1.2, 0.5);
            textDisplay = spawnLocation.getWorld().spawn(spawnLocation, TextDisplay.class);
            textDisplay.setBillboard(Display.Billboard.CENTER);
            textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        }

        public void remove() {
            if (textDisplay == null) return;
            Chunk chunk = textDisplay.getChunk();
            if (!chunk.isLoaded()) chunk.load();
            textDisplay.remove();
            textDisplay = null;
        }
    }
}
