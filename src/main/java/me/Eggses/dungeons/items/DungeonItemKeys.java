package me.Eggses.dungeons.items;

import me.Eggses.dungeons.dungeon.types.DungeonType;
import me.Eggses.dungeons.utility.text.Placeholders;

import java.util.EnumMap;
import java.util.Map;

public class DungeonItemKeys {

    private final Map<DungeonType, ItemRecord> dungeonKeyItems = new EnumMap<>(DungeonType.class);

    public void add(DungeonType dungeonType, ItemStackTemplate itemStackTemplate, String uniqueKey, Placeholders placeholders) {
        dungeonKeyItems.put(dungeonType, new ItemRecord(itemStackTemplate, uniqueKey, placeholders));
    }

    public void remove(DungeonType dungeonType) {
        dungeonKeyItems.remove(dungeonType);
    }

    public ItemRecord getItemRecord(DungeonType dungeonType) {
        return dungeonKeyItems.get(dungeonType);
    }
}