package me.Eggses.dungeons.tasks.definitions;

import me.Eggses.dungeons.tasks.running.TaskManager;
import org.bukkit.scheduler.BukkitTask;

public interface TaskDefinition<T> {
    BukkitTask runTask(T t, TaskManager taskManager);
}