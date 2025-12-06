package me.Eggses.dungeons.entities.dungeonentity.mobs;

import org.bukkit.entity.LivingEntity;

import java.util.function.Consumer;

public enum MobTypes {

    KNIGHT((entity) -> {


        // increase size
        // kb
        // thats it..

    }, new MobName("Knight", false)),


    FIEND((entity) -> {
        // NMS spider jumpiung goal
    }, new MobName("Fiend", false)),


    ENCHANTER((entity) -> {
        // NMS flee from player goal.
    }, new MobName("Enchanter", true));

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