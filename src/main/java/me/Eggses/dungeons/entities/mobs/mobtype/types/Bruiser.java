package me.Eggses.dungeons.entities.mobs.mobtype.types;

import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.mobs.mobtype.MobPreset;
import me.Eggses.dungeons.entities.mobs.mobtype.MobUtility;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.utility.misc.NMS;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.attribute.Attribute;

import java.util.function.Consumer;

@NMS
public class Bruiser implements MobPreset {

    private final MobUtility mobUtility;
    private final String displayName;

    public Bruiser(MobUtility mobUtility, TextFormatter textFormatter) {
        this.mobUtility = mobUtility;
        this.displayName = textFormatter.formatName(this.getClass().getSimpleName(), TextFormatter.SPLITTER_INNER_WORD, TextFormatter.SEPARATOR_SPACE);
    }

    @Override
    public Consumer<MobBuilder> getBuilderConsumer() {
        return mobBuilder -> {

            mobBuilder.mobName(new MobName(displayName, false));

            mobBuilder.spawnChanges(dungeonEntity -> {
                var ac = dungeonEntity.getAttributeController();
                ac.setBaseAttribute(Attribute.SCALE, 1.04);
                ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, 0.27);

                mobUtility.normaliseSize(dungeonEntity);
            });
        };
    }
}