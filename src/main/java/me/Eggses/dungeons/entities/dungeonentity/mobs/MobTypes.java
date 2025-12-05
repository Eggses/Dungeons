package me.Eggses.dungeons.entities.dungeonentity.mobs;

import org.bukkit.entity.LivingEntity;

import java.util.function.Consumer;

public enum MobTypes {

    KNIGHT((entity) -> {}, new MobName("Knight", false)),
    FIEND((entity) -> {}, new MobName("Fiend", false)),
    ENCHANTER((entity) -> {}, new MobName("Enchanter", true));

    private final Consumer<LivingEntity> onSpawn;
    private final MobName mobName;

    MobTypes(Consumer<LivingEntity> onSpawn, MobName mobName) {
        this.onSpawn = onSpawn;
        this.mobName = mobName;
    }

    public Consumer<LivingEntity> getOnSpawn() {
        return onSpawn;
    }

    public MobName getMobName() {
        return mobName;
    }
}
