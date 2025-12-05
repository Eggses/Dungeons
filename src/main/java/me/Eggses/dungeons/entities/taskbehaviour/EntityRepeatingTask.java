package me.Eggses.dungeons.entities.taskbehaviour;

import me.Eggses.dungeons.entities.dungeonentity.TaskManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.BiConsumer;

public class EntityRepeatingTask implements EntityTask {

    private final BiConsumer<LivingEntity, TaskManager> task;
    private final long delayInTicks;
    private final long repeatingPeriodInTicks;

    public EntityRepeatingTask(BiConsumer<LivingEntity, TaskManager> task, long delayInTicks, long repeatingPeriodInTicks) {
        this.task = task;
        this.delayInTicks = delayInTicks;
        this.repeatingPeriodInTicks = repeatingPeriodInTicks;
    }

    public BiConsumer<LivingEntity, TaskManager> getTask() {
        return task;
    }

    public long getDelayInTicks() {
        return delayInTicks;
    }

    public long getRepeatingPeriodInTicks() {
        return repeatingPeriodInTicks;
    }


    @Override
    public BukkitTask schedule(LivingEntity livingEntity, TaskManager taskManager) {
        Runnable runnable = () -> task.accept(livingEntity, taskManager);
        return taskManager.runTaskRepeatedly(runnable, delayInTicks, repeatingPeriodInTicks);
    }
}