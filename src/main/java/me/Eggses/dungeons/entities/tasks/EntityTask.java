package me.Eggses.dungeons.entities.tasks;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.BiConsumer;

public interface EntityTask {
    BukkitTask schedule(DungeonEntity dungeonEntity, TaskManager taskManager);

    static EntityTask repeating(BiConsumer<DungeonEntity, TaskManager> task, long delay, long period) {
        return new EntityRepeatingTask(task, delay, period);
    }
}