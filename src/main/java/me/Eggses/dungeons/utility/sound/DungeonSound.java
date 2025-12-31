package me.Eggses.dungeons.utility.sound;

public enum DungeonSound {

    EVOKER_PREPARE_SUMMON("minecraft:entity.evoker.prepare_summon"),
    EVOKER_PREPARE_ATTACK("minecraft:entity.evoker.prepare_attack"),
    EVOKER_CAST_SPELL("minecraft:entity.evoker.cast_spell"),

    ;

    private final String minecraftSound;

    DungeonSound(String minecraftSound) {
        this.minecraftSound = minecraftSound;
    }

    public String getMinecraftSound() {
        return minecraftSound;
    }
}
