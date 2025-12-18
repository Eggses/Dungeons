package me.Eggses.dungeons.dungeon.instance.configurations;

import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;

public class MalignantMarsh implements DungeonTemplate {

    @Override
    public String getTemplateFolderName() {
        return "";
    }

    @Override
    public DungeonPortal getDungeonPortal() {
        return null;
    }

    @Override
    public AreaControllerBuilder getAreaControllerBuilder() {
        return null;
    }
}