package me.Eggses.dungeons.tasks;

import org.bukkit.scheduler.BukkitTask;

public class TaskContext<O> {

    private final O owner;
    private final ActiveTasks ownersActiveTasks;
    private final TaskRunner taskRunner;

    public TaskContext(O owner, ActiveTasks ownersActiveTasks, TaskRunner taskRunner) {
        this.owner = owner;
        this.ownersActiveTasks = ownersActiveTasks;
        this.taskRunner = taskRunner;
    }

    public O getOwner() {
        return owner;
    }

    public BukkitTask runTaskRepeatedly(Runnable runnable, long delayInTicks, long periodInTicks) {
        BukkitTask task = taskRunner.runTaskRepeatedly(runnable, delayInTicks, periodInTicks);
        ownersActiveTasks.addTask(task);
        return task;
    }

    public BukkitTask runTaskLater(Runnable runnable, long delayInTicks) {
        BukkitTask task = taskRunner.runTaskLater(runnable, delayInTicks);
        ownersActiveTasks.addTask(task);
        return task;
    }

    public void removeAndEndTask(BukkitTask bukkitTask) {
        ownersActiveTasks.endTask(bukkitTask);
    }
}
