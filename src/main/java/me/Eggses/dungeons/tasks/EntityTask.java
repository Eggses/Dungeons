package me.Eggses.dungeons.tasks;

import me.Eggses.dungeons.dungeonentity.TaskManager;
import org.bukkit.scheduler.BukkitTask;

public interface EntityTask {
    BukkitTask schedule(TaskManager taskManager);
}