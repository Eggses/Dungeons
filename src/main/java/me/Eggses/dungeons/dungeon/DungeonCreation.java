package me.Eggses.dungeons.dungeon;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.function.Consumer;

public class DungeonCreation {

    private World createdWorld;


    public DungeonCreation(JavaPlugin plugin,
                           String fileNameOfTemplate,
                           String fileNameOfNewInstance,
                           Consumer<World> onCreation,
                           Consumer<IOException> onFailure) {

    }

}
