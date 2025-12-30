package me.Eggses.dungeons.tasks.running;

import me.Eggses.dungeons.tasks.definitions.TaskBehaviour;
import me.Eggses.dungeons.tasks.definitions.TaskDefinition;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class ActiveTasks<T> {

    private final List<BukkitTask> activeTasks = new ArrayList<>();

    public void addAndRunTasks(T t, TaskBehaviour<T> taskBehaviour, TaskManager taskManager) {
        for (TaskDefinition<T> task : taskBehaviour.getTaskDefinitions()) {
            activeTasks.add(task.runTask(t, taskManager));
        }
    }

    public void endAllTasks() {
        for (BukkitTask task : activeTasks) {
            if (!task.isCancelled()) {
                task.cancel();
            }
        }
        activeTasks.clear();
    }
}
