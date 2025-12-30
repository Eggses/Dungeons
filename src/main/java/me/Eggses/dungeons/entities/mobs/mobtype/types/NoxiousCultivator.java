package me.Eggses.dungeons.entities.mobs.mobtype.types;

import me.Eggses.dungeons.entities.eventbehaviour.damage.PoisonImpact;
import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.mobs.mobtype.MobPreset;
import me.Eggses.dungeons.entities.mobs.mobtype.MobUtility;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.utility.misc.NMS;
import me.Eggses.dungeons.utility.text.TextFormatter;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.function.Consumer;

@NMS
public class NoxiousCultivator implements MobPreset {

    private final MobUtility mobUtility;
    private final String displayName;

    public NoxiousCultivator(MobUtility mobUtility, TextFormatter textFormatter) {
        this.mobUtility = mobUtility;
        this.displayName = textFormatter.formatName(this.getClass().getSimpleName(), TextFormatter.SPLITTER_INNER_WORD, TextFormatter.SEPARATOR_SPACE);
    }

    @Override
    public Consumer<MobBuilder> getBuilderConsumer() {

        return mobBuilder -> {
            mobBuilder.spawnChanges(dungeonEntity -> {

                var ac = dungeonEntity.getAttributeController();
                ac.setBaseAttribute(Attribute.MAX_HEALTH, 20.0);
                ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, 0.31);

                PathfinderMob pathfinderMob = mobUtility.toPathFinderMobWithClearedGoal(dungeonEntity.getEntity());
                if (pathfinderMob == null) return;

                mobUtility.addAttackDamageAttribute(pathfinderMob);
                mobUtility.toZombieStyleMeleeGoals(pathfinderMob);
                mobUtility.normaliseSize(dungeonEntity);
            });

            mobBuilder.mobName(new MobName(displayName, true));
            mobBuilder.eventBehaviour(EntityDamageByEntityEvent.class, new PoisonImpact());
        };
    }
}