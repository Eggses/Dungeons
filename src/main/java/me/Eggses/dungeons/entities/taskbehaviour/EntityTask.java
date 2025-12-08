package me.Eggses.dungeons.entities.taskbehaviour;

import me.Eggses.dungeons.entities.dungeonentity.TaskManager;
import me.Eggses.dungeons.entities.dungeonentity.mobs.DungeonEntity;
import org.bukkit.scheduler.BukkitTask;

public interface EntityTask {
    BukkitTask schedule(DungeonEntity dungeonEntity, TaskManager taskManager);
}