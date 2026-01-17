package me.Eggses.dungeons.dungeon.types;

import java.util.Locale;

public enum DungeonType {

    MALIGNANT_MARSH("malignant_marsh.yml");

    private final String dungeonConfigFileName;

    DungeonType(String dungeonConfigFileName) {
        this.dungeonConfigFileName = dungeonConfigFileName;
    }

    public String getDungeonConfigFileName() {
        return dungeonConfigFileName;
    }

    public String getUniqueKey() {
        return dungeonConfigFileName.substring(0, dungeonConfigFileName.indexOf('.'));
    }

    public static DungeonType getType(String type) {
        try {
            return DungeonType.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
