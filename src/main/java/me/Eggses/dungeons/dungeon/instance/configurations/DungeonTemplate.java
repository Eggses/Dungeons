package me.Eggses.dungeons.dungeon.instance.configurations;

import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import org.bukkit.World;

import java.util.function.Consumer;

public interface DungeonTemplate {

    Consumer<World> EMPTY_CONSUMER = (world) -> {};

    String getTemplateFolderName();
    DungeonPortal getDungeonPortal();
    AreaControllerBuilder getAreaControllerBuilder();

    default Consumer<World> getDungeonRules() {
        return EMPTY_CONSUMER;
    }
}