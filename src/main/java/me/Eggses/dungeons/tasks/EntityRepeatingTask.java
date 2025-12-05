package me.Eggses.dungeons.tasks;

import me.Eggses.dungeons.dungeonentity.TaskManager;
import org.bukkit.scheduler.BukkitTask;

public class EntityRepeatingTask implements EntityTask {

    private final Runnable runnable;
    private final long delayInTicks;
    private final long repeatingPeriodInTicks;

    public EntityRepeatingTask(Runnable runnable, long delayInTicks, long repeatingPeriodInTicks) {
        this.runnable = runnable;
        this.delayInTicks = delayInTicks;
        this.repeatingPeriodInTicks = repeatingPeriodInTicks;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public long getDelayInTicks() {
        return delayInTicks;
    }

    public long getRepeatingPeriodInTicks() {
        return repeatingPeriodInTicks;
    }


    @Override
    public BukkitTask schedule(TaskManager taskManager) {
        return taskManager.runTaskRepeatedly(runnable, delayInTicks, repeatingPeriodInTicks);
    }
}