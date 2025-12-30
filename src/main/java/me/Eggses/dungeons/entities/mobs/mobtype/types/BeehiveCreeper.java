package me.Eggses.dungeons.entities.mobs.mobtype.types;

import me.Eggses.dungeons.entities.eventbehaviour.explosion.BeeExplosion;
import me.Eggses.dungeons.entities.eventbehaviour.explosion.SlownessExplosion;
import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.mobs.mobtype.MobPreset;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.function.Consumer;

public class BeehiveCreeper implements MobPreset {

    private final String displayName;

    public BeehiveCreeper(TextFormatter textFormatter) {
        this.displayName = textFormatter.formatName(this.getClass().getSimpleName(), TextFormatter.SPLITTER_INNER_WORD, TextFormatter.SEPARATOR_SPACE);
    }

    @Override
    public Consumer<MobBuilder> getBuilderConsumer() {
        return mobBuilder -> {
            mobBuilder.mobName(new MobName(displayName, true));
            mobBuilder.eventBehaviour(ExplosionPrimeEvent.class, new BeeExplosion());
            mobBuilder.eventBehaviour(ExplosionPrimeEvent.class, new SlownessExplosion());
        };
    }
}