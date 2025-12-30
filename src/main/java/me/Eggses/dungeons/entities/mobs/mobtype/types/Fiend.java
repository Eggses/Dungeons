package me.Eggses.dungeons.entities.mobs.mobtype.types;

import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.mobs.mobtype.MobPreset;
import me.Eggses.dungeons.entities.mobs.mobtype.MobUtility;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.utility.misc.NMS;
import me.Eggses.dungeons.utility.text.TextFormatter;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.bukkit.attribute.Attribute;

import java.util.function.Consumer;

@NMS
public class Fiend implements MobPreset {

    private final MobUtility mobUtility;
    private final String displayName;

    public Fiend(MobUtility mobUtility, TextFormatter textFormatter) {
        this.mobUtility = mobUtility;
        this.displayName = textFormatter.formatName(this.getClass().getSimpleName(), TextFormatter.SPLITTER_INNER_WORD, TextFormatter.SEPARATOR_SPACE);
    }

    @Override
    public Consumer<MobBuilder> getBuilderConsumer() {
        return mobBuilder -> {

            mobBuilder.mobName(new MobName(displayName, false));

            mobBuilder.spawnChanges(dungeonEntity -> {
                var ac = dungeonEntity.getAttributeController();

                ac.setBaseAttribute(Attribute.SCALE, 0.7);
                ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, 0.29);

                PathfinderMob pathfinderMob = mobUtility.toPathFinderMobWithClearedGoal(dungeonEntity.getEntity());
                if (pathfinderMob == null) return;

                pathfinderMob.goalSelector.addGoal(1, new FloatGoal(pathfinderMob));
                pathfinderMob.goalSelector.addGoal(3, new LeapAtTargetGoal(pathfinderMob, 0.4f));
                pathfinderMob.goalSelector.addGoal(4, new MeleeAttackGoal(pathfinderMob, 1.2, true));
                pathfinderMob.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(pathfinderMob, 0.8));
                pathfinderMob.goalSelector.addGoal(6, new LookAtPlayerGoal(pathfinderMob, Player.class, 8.0f));
                pathfinderMob.goalSelector.addGoal(6, new RandomLookAroundGoal(pathfinderMob));

                pathfinderMob.targetSelector.addGoal(1, new HurtByTargetGoal(pathfinderMob));
                pathfinderMob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(pathfinderMob, Player.class, true));

                mobUtility.normaliseSize(dungeonEntity);
            });
        };
    }
}