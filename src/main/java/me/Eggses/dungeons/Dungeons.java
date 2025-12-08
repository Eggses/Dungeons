package me.Eggses.dungeons;

import me.Eggses.dungeons.entities.EntityManager;
import me.Eggses.dungeons.entities.taskbehaviour.TaskManager;
import me.Eggses.dungeons.listeners.AdminTestingDelete;
import me.Eggses.dungeons.listeners.EntityDamageEvent;
import me.Eggses.dungeons.listeners.EntityExplosionEvent;
import me.Eggses.dungeons.utility.MessageCreator;
import org.bukkit.plugin.java.JavaPlugin;

public final class Dungeons extends JavaPlugin {

    @Override
    public void onEnable() {

        MessageCreator messageCreator = new MessageCreator();
        TaskManager taskManager = new TaskManager(this);

        EntityManager entityManager = new EntityManager(taskManager, messageCreator);

        getServer().getPluginManager().registerEvents(new EntityDamageEvent(entityManager), this);
        getServer().getPluginManager().registerEvents(new EntityExplosionEvent(entityManager), this);
        getServer().getPluginManager().registerEvents(new AdminTestingDelete(entityManager), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
