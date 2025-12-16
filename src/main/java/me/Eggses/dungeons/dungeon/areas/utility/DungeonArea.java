package me.Eggses.dungeons.dungeon.areas.utility;

import me.Eggses.dungeons.dungeon.areas.EntityManager;
import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import me.Eggses.dungeons.dungeon.regions.Region;
import org.bukkit.World;

import java.util.function.BiConsumer;

public class DungeonArea {

    private final Region entryRegion;
    private final BiConsumer<World, EntityManager> onEnterFirstTime;
    private final BiConsumer<World, Graveyard> onClearArea;

    public DungeonArea(Region entryRegion,
                       BiConsumer<World, EntityManager> onEnterFirstTime,
                       BiConsumer<World, Graveyard> onClearArea) {

        this.entryRegion = entryRegion;
        this.onEnterFirstTime = onEnterFirstTime;
        this.onClearArea = onClearArea;
    }

    public Region getEntryRegion() {
        return entryRegion;
    }

    public void onEnterFirstTime(World world, EntityManager entityManager) {
        onEnterFirstTime.accept(world, entityManager);
    }

    public void onClearArea(World world, Graveyard graveyard) {
        onClearArea.accept(world, graveyard);
    }
}