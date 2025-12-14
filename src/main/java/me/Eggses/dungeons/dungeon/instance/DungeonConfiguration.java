package me.Eggses.dungeons.dungeon.instance;

import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.progress.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import org.bukkit.World;

import java.util.function.Consumer;

public interface DungeonConfiguration {
    DungeonPortal getDungeonPortal();
    Consumer<World> getDungeonRules();
    AreaControllerBuilder getAreaControllerBuilder();
    BannedItems getBannedItems();
}