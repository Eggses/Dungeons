package me.Eggses.dungeons.tasks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class TaskRunner {

    private final JavaPlugin plugin;

    public TaskRunner(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public BukkitTask runTaskRepeatedly(Runnable runnable, long delayInTicks, long periodInTicks) {
        return Bukkit.getScheduler().runTaskTimer(plugin, runnable, delayInTicks, periodInTicks);
    }

    public BukkitTask runTaskLater(Runnable runnable, long delayInTicks) {
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, delayInTicks);
    }
}
