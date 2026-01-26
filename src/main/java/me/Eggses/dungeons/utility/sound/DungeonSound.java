package me.Eggses.dungeons.utility.sound;

public enum DungeonSound {

    EVOKER_PREPARE_SUMMON("minecraft:entity.evoker.prepare_summon"),
    EVOKER_PREPARE_ATTACK("minecraft:entity.evoker.prepare_attack"),
    EVOKER_CAST_SPELL("minecraft:entity.evoker.cast_spell"),

    WARDEN_ATTACK_IMPACT("minecraft:entity.warden.attack_impact"),
    MUD_STEP("minecraft:block.mud.step"),
    MUD_FALL("minecraft:block.mud.fall"),

    GRINDSTONE_USE("minecraft:block.grindstone.use"),
    ILLUSIONER_PREPARE_BLINDNESS("minecraft:entity.illusioner.prepare_blindness"),
    GENERIC_EXPLODE("minecraft:entity.generic.explode"),

    GUARDIAN_AMBIENT("minecraft:entity.guardian.ambient"),

    ;

    private final String minecraftSound;

    DungeonSound(String minecraftSound) {
        this.minecraftSound = minecraftSound;
    }

    public String getMinecraftSound() {
        return minecraftSound;
    }
}
