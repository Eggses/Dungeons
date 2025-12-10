package me.Eggses.dungeons.dungeon;

import org.bukkit.plugin.java.JavaPlugin;

public class MalignantMarsh extends DungeonInstance {

    private static final String DUNGEON_NAME = "Malignant_Marsh";
    private static int COUNTER = 1;

    public MalignantMarsh(JavaPlugin plugin) {
        super(plugin, DUNGEON_NAME);
    }

    @Override
    public void openPortal() {

    }

    @Override
    public void closePortal() {

    }

    @Override
    public String produceInstanceName() {
        return DUNGEON_NAME + "_Instance_" + COUNTER++;
    }
}