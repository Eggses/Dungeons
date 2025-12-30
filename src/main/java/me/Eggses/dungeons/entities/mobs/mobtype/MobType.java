package me.Eggses.dungeons.entities.mobs.mobtype;

public enum MobType {

    KNIGHT(),
    FIEND(),
    BRUISER(),
    ENCHANTER(),
    NOXIOUS_CULTIVATOR(),
    VILLAGER(),
    BEEHIVE_CREEPER(),
    ;

    public static MobType getMobType(String mobType) {
        if (mobType == null) return null;
        try {
            return MobType.valueOf(mobType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}