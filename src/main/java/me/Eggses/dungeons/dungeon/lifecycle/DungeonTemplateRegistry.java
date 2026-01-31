package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.files.templates.DungeonInstanceTemplate;
import me.Eggses.dungeons.dungeon.files.templates.NonInstanceDungeonTemplate;
import me.Eggses.dungeons.dungeon.types.DungeonType;

import java.util.EnumMap;
import java.util.Map;

public class DungeonTemplateRegistry {

    private final Map<DungeonType, DungeonTemplate> dungeonBuilderMap = new EnumMap<>(DungeonType.class);

    public void add(DungeonType dungeonType,
                    DungeonInstanceTemplate dungeonInstanceTemplate,
                    NonInstanceDungeonTemplate nonInstanceDungeonTemplate) {


        dungeonBuilderMap.put(dungeonType, new DungeonTemplate(dungeonInstanceTemplate, nonInstanceDungeonTemplate));
    }

    public DungeonInstanceTemplate getDungeonInstanceTemplate(DungeonType dungeonType) {
        return dungeonBuilderMap.get(dungeonType).dungeonInstanceTemplate;
    }

    public NonInstanceDungeonTemplate getNonInstanceDungeonTemplate(DungeonType dungeonType) {
        return dungeonBuilderMap.get(dungeonType).nonInstanceDungeonTemplate;
    }

    private record DungeonTemplate(DungeonInstanceTemplate dungeonInstanceTemplate,
                                   NonInstanceDungeonTemplate nonInstanceDungeonTemplate) {}
}
