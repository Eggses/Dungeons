package me.Eggses.dungeons.entities.mobs.mobtype.types;

import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.mobs.mobtype.MobPreset;
import me.Eggses.dungeons.entities.mobs.mobtype.MobUtility;
import org.bukkit.attribute.Attribute;

import java.util.function.Consumer;

public class Villager implements MobPreset {

    private final MobUtility mobUtility;

    public Villager(MobUtility mobUtility) {
        this.mobUtility = mobUtility;
    }

    @Override
    public Consumer<MobBuilder> getBuilderConsumer() {

        return mobBuilder -> {
            mobBuilder.spawnChanges(dungeonEntity -> {
                var ac = dungeonEntity.getAttributeController();
                ac.setBaseAttribute(Attribute.MAX_HEALTH, 1000.0);
                mobUtility.normaliseSize(dungeonEntity);
            });
        };
    }
}