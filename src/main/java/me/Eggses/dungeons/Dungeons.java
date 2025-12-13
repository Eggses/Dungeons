package me.Eggses.dungeons;

import me.Eggses.dungeons.entities.EntityManager;
import me.Eggses.dungeons.entities.taskbehaviour.TaskManager;
import me.Eggses.dungeons.listeners.entities.Combustion;
import me.Eggses.dungeons.utility.MessageCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Dungeons extends JavaPlugin {

    @Override
    public void onEnable() {

        // ?
        TaskManager taskManager = new TaskManager(this);
        MessageCreator messageCreator = new MessageCreator();
        EntityManager entityManager = new EntityManager(taskManager, messageCreator);

        // Events
        PluginManager pluginManager = getServer().getPluginManager();

        //Entities
        pluginManager.registerEvents(new Combustion(entityManager), this);

        //Players


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        /*
        maybe some how go through: and delete every instance that is open?
        like iterate over the set of instances and call thier delete methods...

        also when you delete an instance delete its entry in the name manaager
         */
    }
}
