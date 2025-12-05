package me.Eggses.dungeons.taskbehaviour;

import me.Eggses.dungeons.dungeonentity.TaskManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

public interface EntityTask {
    BukkitTask schedule(LivingEntity livingEntity, TaskManager taskManager);
}