package me.Eggses.dungeons.dungeon;

public class MalignantMarsh extends DungeonInstance {

    private static final String DUNGEON_NAME = "Malignant_Marsh";
    private static int COUNTER = 1;

    public MalignantMarsh(String worldName) {
        super(worldName);
    }

    @Override
    public String produceInstanceName() {
        return DUNGEON_NAME + COUNTER++;
    }


}
