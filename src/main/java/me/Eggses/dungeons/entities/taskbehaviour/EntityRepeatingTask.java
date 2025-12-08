package me.Eggses.dungeons.entities.taskbehaviour;

import me.Eggses.dungeons.entities.dungeonentity.TaskManager;
import me.Eggses.dungeons.entities.dungeonentity.mobs.DungeonEntity;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.BiConsumer;

public class EntityRepeatingTask implements EntityTask {

    private final BiConsumer<DungeonEntity, TaskManager> task;
    private final long delayInTicks;
    private final long repeatingPeriodInTicks;

    public EntityRepeatingTask(BiConsumer<DungeonEntity, TaskManager> task, long delayInTicks, long repeatingPeriodInTicks) {
        this.task = task;
        this.delayInTicks = delayInTicks;
        this.repeatingPeriodInTicks = repeatingPeriodInTicks;
    }


    @Override
    public BukkitTask schedule(DungeonEntity dungeonEntity, TaskManager taskManager) {
        Runnable runnable = () -> task.accept(dungeonEntity, taskManager);
        return taskManager.runTaskRepeatedly(runnable, delayInTicks, repeatingPeriodInTicks);
    }
}