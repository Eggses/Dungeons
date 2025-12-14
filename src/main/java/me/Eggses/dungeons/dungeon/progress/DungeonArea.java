package me.Eggses.dungeons.dungeon.progress;

import me.Eggses.dungeons.dungeon.regions.Region;
import org.bukkit.World;

public interface DungeonArea {
    Region getEntryRegion();
    void onEnterFirstTime(World world);
    void onClearArea(World world);
}