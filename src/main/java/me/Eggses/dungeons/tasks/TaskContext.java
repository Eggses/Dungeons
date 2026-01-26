package me.Eggses.dungeons.tasks;

import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicReference;

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

    public void runTaskRepeatedly(Runnable runnable, long delayInTicks, long periodInTicks) {
        BukkitTask task = taskRunner.runTaskRepeatedly(runnable, delayInTicks, periodInTicks);
        ownersActiveTasks.addTask(task);
    }

    public void runTaskLaterAndRemove(Runnable runnable, long delayInTicks) {

        AtomicReference<BukkitTask> ref = new AtomicReference<>();

        BukkitTask task = taskRunner.runTaskLater(() -> {
            try {
                runnable.run();
            } finally {
               ownersActiveTasks.removeAndEndTask(ref.get());
            }
        }, delayInTicks);

        ref.set(task);
        ownersActiveTasks.addTask(task);
    }
}
