package me.Eggses.dungeons.dungeon.instance.configurations;

import me.Eggses.dungeons.dungeon.graveyard.GraveyardDefinition;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import org.bukkit.World;

import java.util.List;
import java.util.function.Consumer;

public class MalignantMarsh implements DungeonConfiguration {

    private static final String TEMPLATE_FOLDER_NAME = "malignant_marsh"; // stored in file now...

    public MalignantMarsh() {
    }

    @Override
    public DungeonPortal getDungeonPortal() {
        return null;
        // file makes this?
    }

    @Override
    public AreaControllerBuilder getAreaControllerBuilder() {
        // file makes this... defer to file..
        return null;
    }

    @Override
    public List<GraveyardDefinition> getGraveyardDefinitionList() {
        return List.of();
        // think this pointless.graveyards get auto set now no longer running a list.
    }

    @Override
    public Consumer<World> getDungeonRules() {
        return null;
    }

    @Override
    public BannedItems getBannedItems() {
        return null;
        // probably do not need this...
    }

    @Override
    public String getTemplateFolderName() {
        return TEMPLATE_FOLDER_NAME;
    }
}