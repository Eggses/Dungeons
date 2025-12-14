package me.Eggses.dungeons.dungeon.baseinstance;

import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.progress.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import org.bukkit.World;

import java.util.function.Consumer;

public interface DungeonConfiguration {

    String getTemplateName();
    DungeonPortal getDungeonPortal();
    Consumer<World> getDungeonRules();
    AreaControllerBuilder getAreaControllerBuilder();
    BannedItems getBannedItems();
}