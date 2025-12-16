package me.Eggses.dungeons.dungeon.instance.configurations;

import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import org.bukkit.World;

import java.util.function.Consumer;

public interface DungeonConfiguration {
    DungeonPortal getDungeonPortal();
    Consumer<World> getDungeonRules();
    AreaControllerBuilder getAreaControllerBuilder();
    BannedItems getBannedItems();
}