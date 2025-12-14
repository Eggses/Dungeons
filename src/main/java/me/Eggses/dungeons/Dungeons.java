package me.Eggses.dungeons;

import org.bukkit.plugin.java.JavaPlugin;

public final class Dungeons extends JavaPlugin {

    @Override
    public void onEnable() {

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
