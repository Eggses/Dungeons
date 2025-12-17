package me.Eggses.dungeons.dungeon.instance.configurations;

import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.graveyard.GraveyardDefinition;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import org.bukkit.World;

import java.util.List;
import java.util.function.Consumer;

public class FlatTest implements DungeonConfiguration {

    private static final String TEMPLATE_NAME = "dungeon_test";

    @Override
    public DungeonPortal getDungeonPortal() {
        return null;
    }

    @Override
    public AreaControllerBuilder getAreaControllerBuilder() {
        return null;
    }

    @Override
    public List<GraveyardDefinition> getGraveyardDefinitionList() {
        return List.of();
    }

    @Override
    public Consumer<World> getDungeonRules() {
        return null;
    }

    @Override
    public BannedItems getBannedItems() {
        return null;
    }

    @Override
    public String getTemplateName() {
        return TEMPLATE_NAME;
    }
}
