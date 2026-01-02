package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.types.DungeonType;

import java.util.EnumSet;
import java.util.Set;

public class TemplateReservation {

    private final Set<DungeonType> reservedDungeonTypes = EnumSet.noneOf(DungeonType.class);

    public synchronized void reserve(DungeonType dungeonType) {
        reservedDungeonTypes.add(dungeonType);
    }

    public synchronized void free(DungeonType dungeonType) {
        reservedDungeonTypes.remove(dungeonType);
    }

    public synchronized boolean isTemplateFree(DungeonType dungeonType) {
        return !reservedDungeonTypes.contains(dungeonType);
    }
}
