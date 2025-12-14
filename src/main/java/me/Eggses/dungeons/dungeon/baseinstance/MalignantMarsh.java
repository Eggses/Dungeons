package me.Eggses.dungeons.dungeon.baseinstance;

import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.progress.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import org.bukkit.World;

import java.util.function.Consumer;

public class MalignantMarsh implements DungeonConfiguration {

    private static final String DUNGEON_TEMPLATE_NAME = "malignant_marsh";

    public MalignantMarsh() {

    }

    @Override
    public String getTemplateName() {
    return DUNGEON_TEMPLATE_NAME;
    }

    @Override
    public DungeonPortal getDungeonPortal() {
        return null;
    }

    @Override
    public Consumer<World> getDungeonRules() {
        return null;
    }

    @Override
    public AreaControllerBuilder getAreaControllerBuilder() {
        return null;
    }

    @Override
    public BannedItems getBannedItems() {
        return null;
    }
}