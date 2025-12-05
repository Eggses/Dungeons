package me.Eggses.dungeons.entities.taskbehaviour;

import me.Eggses.dungeons.entities.dungeonentity.TaskManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

public interface EntityTask {
    BukkitTask schedule(LivingEntity livingEntity, TaskManager taskManager);
}