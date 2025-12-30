package me.Eggses.dungeons.entities.mobs.mobtype;

import me.Eggses.dungeons.entities.mobs.MobBuilder;

import java.util.function.Consumer;

public interface MobPreset {
    Consumer<MobBuilder> getBuilderConsumer();
}