package me.Eggses.dungeons.dungeon.instance.configurations;

import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.graveyard.GraveyardDefinition;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import org.bukkit.World;

import java.util.List;
import java.util.function.Consumer;

public interface DungeonConfiguration {
    DungeonPortal getDungeonPortal();
    Consumer<World> getDungeonRules();
    AreaControllerBuilder getAreaControllerBuilder();
    BannedItems getBannedItems();
    List<GraveyardDefinition> getGraveyardDefinitionList();
}