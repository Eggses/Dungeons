package me.Eggses.dungeons.dungeon.areas.utility;

import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;

import java.util.function.Consumer;

public class DungeonArea {

    private final Region entryRegion;
    private final Consumer<DungeonContext> onEnterFirstTime;
    private final Consumer<DungeonContext> onClearArea;

    public DungeonArea(Region entryRegion,
                       Consumer<DungeonContext> onEnterFirstTime,
                       Consumer<DungeonContext> onClearArea) {

        this.entryRegion = entryRegion;
        this.onEnterFirstTime = onEnterFirstTime;
        this.onClearArea = onClearArea;
    }

    public Region getEntryRegion() {
        return entryRegion;
    }

    public void onEnterFirstTime(DungeonContext dungeonContext) {
        onEnterFirstTime.accept(dungeonContext);
    }

    public void onClearArea(DungeonContext dungeonContext) {
        onClearArea.accept(dungeonContext);
    }
}