package me.Eggses.dungeons.entities.taskbehaviour;

import me.Eggses.dungeons.entities.dungeonentity.TaskManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class ActiveEntityTasks {

    private final List<BukkitTask> activeTasks = new ArrayList<>();

    public void addAndRunTasks(EntityTaskBehaviour entityTaskBehaviour,
                               LivingEntity livingEntity,
                               TaskManager taskManager) {

        for (EntityTask entityTask : entityTaskBehaviour.getEntityTasks()) {
            activeTasks.add(entityTask.schedule(livingEntity, taskManager));
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