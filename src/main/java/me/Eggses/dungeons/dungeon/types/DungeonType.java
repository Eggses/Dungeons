package me.Eggses.dungeons.dungeon.types;

public enum DungeonType {

    FLAT_TEST("flat_test.yml"),
    //MALIGNANT_MARSH("malignant_marsh.yml", "malignant_marsh"),
    ;

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
            return DungeonType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
