package me.Eggses.dungeons.dungeon;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DungeonManager {

    private final List<DungeonInstance> dungeonInstances = new ArrayList<>();

    public boolean playerIsInDungeon(Player player) {
        for (DungeonInstance dungeonInstance : dungeonInstances) {
            if (dungeonInstance.containsPlayer(player)) {
                return true;
            }
        }
        return false;
    }
}