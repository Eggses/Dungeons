package me.Eggses.dungeons.tasks;

import me.Eggses.dungeons.dungeonentity.TaskManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class ActiveEntityTasks {

    private final List<BukkitTask> activeTasks = new ArrayList<>();

    public void addAndRunTasks(EntityTaskBehaviour entityTaskBehaviour, TaskManager taskManager) {
        for (EntityTask entityTask : entityTaskBehaviour.getEntityTasks()) {
            activeTasks.add(entityTask.schedule(taskManager));
        }
    }

    public void clearAllTasks() {
        for (BukkitTask task : activeTasks) {
            if (!task.isCancelled()) {
                task.cancel();
            }
        }
        activeTasks.clear();
    }
}