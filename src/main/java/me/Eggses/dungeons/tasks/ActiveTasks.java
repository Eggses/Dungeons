package me.Eggses.dungeons.tasks;

import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class ActiveTasks {

    private final List<BukkitTask> activeTasks = new ArrayList<>();

    public ActiveTasks() {}

    public void addTask(BukkitTask task) {
        activeTasks.add(task);
    }

    public void removeAndEndTask(BukkitTask task) {
        if (!task.isCancelled()) task.cancel();
        activeTasks.remove(task);
    }

    public void endAllTasks() {
        for (BukkitTask task : activeTasks) {
            if (!task.isCancelled()) task.cancel();
        }
        activeTasks.clear();
    }
}
