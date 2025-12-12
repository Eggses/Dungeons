package me.Eggses.dungeons.dungeon;

import org.bukkit.plugin.java.JavaPlugin;

public class MalignantMarsh extends DungeonInstance {

    private static final String DUNGEON_NAME = "malignant_marsh";
    private static int COUNTER = 1;

    public MalignantMarsh(JavaPlugin plugin) {
        super(plugin, DUNGEON_NAME);
    }

    @Override
    protected void openPortal() {

    }

    @Override
    protected void closePortal() {

    }

    @Override
    public String produceInstanceName() {
        return DUNGEON_NAME + "_instance_" + COUNTER++;
    }
}