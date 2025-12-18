package me.Eggses.dungeons.dungeon.areas.utility;

import me.Eggses.dungeons.configuration.TriConsumer;
import me.Eggses.dungeons.dungeon.areas.EntityManager;
import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import me.Eggses.dungeons.dungeon.regions.Region;
import org.bukkit.World;


public class DungeonArea {

    private final Region entryRegion;
    private final TriConsumer<World, EntityManager, Graveyard> onEnterFirstTime;
    private final TriConsumer<World, EntityManager, Graveyard> onClearArea;

    public DungeonArea(Region entryRegion,
                       TriConsumer<World, EntityManager, Graveyard> onEnterFirstTime,
                       TriConsumer<World, EntityManager, Graveyard> onClearArea) {

        this.entryRegion = entryRegion;
        this.onEnterFirstTime = onEnterFirstTime;
        this.onClearArea = onClearArea;
    }

    public Region getEntryRegion() {
        return entryRegion;
    }

    public void onEnterFirstTime(World world, EntityManager entityManager, Graveyard graveyard) {
        onEnterFirstTime.accept(world, entityManager, graveyard);
    }

    public void onClearArea(World world, EntityManager entityManager, Graveyard graveyard) {
        onClearArea.accept(world, entityManager, graveyard);
    }
}