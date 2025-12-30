package me.Eggses.dungeons.tasks.definitions;

import me.Eggses.dungeons.tasks.running.TaskManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.BiConsumer;

public class RepeatingTask<T> implements TaskDefinition<T> {

    private final BiConsumer<T, TaskManager> task;
    private final long delayInTicks;
    private final long repeatingPeriodInTicks;

    public RepeatingTask(BiConsumer<T, TaskManager> task, long delayInTicks, long repeatingPeriodInTicks) {
        this.task = task;
        this.delayInTicks = delayInTicks;
        this.repeatingPeriodInTicks = repeatingPeriodInTicks;
    }

    @Override
    public BukkitTask runTask(T t, TaskManager taskManager) {
        Runnable runnable = () -> task.accept(t, taskManager);
        return taskManager.runTaskRepeatedly(runnable, delayInTicks, repeatingPeriodInTicks);
    }
}
