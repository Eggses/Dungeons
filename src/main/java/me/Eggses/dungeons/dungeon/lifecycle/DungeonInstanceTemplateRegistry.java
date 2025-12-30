package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.files.templates.DungeonInstanceTemplate;
import me.Eggses.dungeons.dungeon.types.DungeonType;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class DungeonInstanceTemplateRegistry {

    private final Map<DungeonType, DungeonInstanceTemplate> dungeonBuilderMap = new EnumMap<>(DungeonType.class);
    private final Set<DungeonType> dungeonTypesInUse = EnumSet.noneOf(DungeonType.class);

    public void add(DungeonType dungeonType, DungeonInstanceTemplate dungeonInstanceTemplate) {
        dungeonBuilderMap.put(dungeonType, dungeonInstanceTemplate);
    }

    public DungeonInstanceTemplate getDungeonInstanceTemplate(DungeonType dungeonType) {
        return dungeonBuilderMap.get(dungeonType);
    }

    public void reserveTemplate(DungeonType dungeonType) {
        dungeonTypesInUse.add(dungeonType);
    }

    public void freeTemplate(DungeonType dungeonType) {
        dungeonTypesInUse.remove(dungeonType);
    }

    public boolean isTemplateFree(DungeonType dungeonType) {
        return !dungeonTypesInUse.contains(dungeonType);
    }
}