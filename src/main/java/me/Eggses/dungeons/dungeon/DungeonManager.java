package me.Eggses.dungeons.dungeon;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DungeonManager {

    private final List<DungeonInstance> dungeonInstances = new ArrayList<>();

    private final JavaPlugin plugin;

    public DungeonManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isInDungeon(Player player) {
        for (DungeonInstance dungeonInstance : dungeonInstances) {
            if (dungeonInstance.isInDungeon(player)) {
                return true;
            }
        }
        return false;
    }

    /*
    for this it will be like the region check in each
    move event for the areas inside each dungeon instance
    currently it jsut gives false.
     */
    public boolean isInNormalWorldPortalRoom(Player player) {
        for (DungeonInstance dungeonInstance : dungeonInstances) {
            if (dungeonInstance.isInNormalWorldPortalRoom()) {
                return true;
            }
        }
        return false;
    }


    can now prevent item drop in that room
            check for bundles there and saddles dont let entry with them


    public void createDungeon(DungeonType dungeonType) {

        switch (dungeonType) {

            case MALIGNANT_MARSH -> dungeonInstances.add(new MalignantMarsh(plugin));

            case null, default -> {

            }
        }
    }

    public enum DungeonType {
        MALIGNANT_MARSH(),
    }
}
