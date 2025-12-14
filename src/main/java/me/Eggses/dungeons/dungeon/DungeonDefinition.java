package me.Eggses.dungeons.dungeon;

import me.Eggses.dungeons.dungeon.portals.PortalController;
import org.bukkit.World;

import java.util.function.Consumer;

public interface DungeonDefinition {

    String getTemplateFileName();
    PortalController getPortalController();
    Consumer<World> getDungeonRules();


}
