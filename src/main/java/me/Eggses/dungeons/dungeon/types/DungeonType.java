package me.Eggses.dungeons.dungeon.types;

public enum DungeonType {

    FLAT_TEST("flat_test.yml", "flat_test"),
    //MALIGNANT_MARSH("malignant_marsh.yml", "malignant_marsh"),
    ;

    private final String dungeonConfigFileName;
    private final String uniqueKey;

    DungeonType(String dungeonConfigFileName, String uniqueKey) {
        this.dungeonConfigFileName = dungeonConfigFileName;
        this.uniqueKey = uniqueKey;
    }

    public String getDungeonConfigFileName() {
        return dungeonConfigFileName;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }
}
