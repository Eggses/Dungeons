package me.Eggses.dungeons.entities.tasks;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class ActiveEntityTasks {

    private final List<BukkitTask> activeTasks = new ArrayList<>();

    public void addAndRunTasks(EntityTaskBehaviour entityTaskBehaviour,
                               DungeonEntity dungeonEntity,
                               TaskManager taskManager) {

        for (EntityTask entityTask : entityTaskBehaviour.getEntityTasks()) {
            activeTasks.add(entityTask.schedule(dungeonEntity, taskManager));
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