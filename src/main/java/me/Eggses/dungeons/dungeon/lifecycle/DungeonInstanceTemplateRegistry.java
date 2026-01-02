package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.files.templates.DungeonInstanceTemplate;
import me.Eggses.dungeons.dungeon.types.DungeonType;

import java.util.EnumMap;
import java.util.Map;

public class DungeonInstanceTemplateRegistry {

    private final Map<DungeonType, DungeonInstanceTemplate> dungeonBuilderMap = new EnumMap<>(DungeonType.class);

    public void add(DungeonType dungeonType, DungeonInstanceTemplate dungeonInstanceTemplate) {
        dungeonBuilderMap.put(dungeonType, dungeonInstanceTemplate);
    }

    public DungeonInstanceTemplate getDungeonInstanceTemplate(DungeonType dungeonType) {
        return dungeonBuilderMap.get(dungeonType);
    }
}
