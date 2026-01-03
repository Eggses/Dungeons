package me.Eggses.dungeons.dungeon.items;

import me.Eggses.dungeons.dungeon.types.DungeonType;
import me.Eggses.dungeons.items.ItemRecord;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class DungeonKeys {

    private final Map<DungeonType, ItemRecord> dungeonKeys = new EnumMap<>(DungeonType.class);

    public void add(DungeonType dungeonType, ItemRecord itemRecord) {
        dungeonKeys.put(dungeonType, itemRecord);
    }

    public void remove(DungeonType dungeonType) {
        dungeonKeys.remove(dungeonType);
    }

    public ItemRecord getItemRecord(DungeonType dungeonType) {
        return dungeonKeys.get(dungeonType);
    }

    public Set<DungeonType> getDungeonTypeSet() {
        return Set.copyOf(dungeonKeys.keySet());
    }
}
