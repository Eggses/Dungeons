package me.Eggses.dungeons.entities.mobs.mobtype.types;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.mobs.mobtype.MobPreset;
import me.Eggses.dungeons.entities.mobs.mobtype.MobUtility;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.tasks.definitions.RepeatingTask;
import me.Eggses.dungeons.tasks.running.TaskManager;
import me.Eggses.dungeons.utility.misc.NMS;
import me.Eggses.dungeons.utility.text.TextFormatter;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@NMS
public class Enchanter implements MobPreset {

    private final JavaPlugin plugin;
    private final MobUtility mobUtility;
    private final String displayName;

    public Enchanter(JavaPlugin plugin, MobUtility mobUtility, TextFormatter textFormatter) {
        this.plugin = plugin;
        this.mobUtility = mobUtility;
        this.displayName = textFormatter.formatName(this.getClass().getSimpleName(), TextFormatter.SPLITTER_INNER_WORD, TextFormatter.SEPARATOR_SPACE);
    }

    @Override
    public Consumer<MobBuilder> getBuilderConsumer() {

        return mobBuilder -> {

            final double BUFF_RANGE = 12.0;
            final float FOLLOW_START_DISTANCE = (float) BUFF_RANGE;
            final float FOLLOW_STOP_DISTANCE  = 8.0f;

            final int MAX_BUFF_EFFECTS = 2;
            final int MAX_BUFF_AMPLIFIER = 1; // 0 = Effect Level 1, 1 = Effect Level 2 etc.
            final int REPEATING_PERIOD = 20 * 5;
            final int BUFF_DURATION = 20 * 5;
            final int MAX_BUFF_TARGETS = 5;

            final List<PotionEffectType> ENCHANTER_EFFECTS = List.of(
                    PotionEffectType.SPEED,
                    PotionEffectType.STRENGTH,
                    PotionEffectType.RESISTANCE
            );

            mobBuilder.mobName(new MobName(displayName, true));

            mobBuilder.spawnChanges(dungeonEntity -> {
                var ac = dungeonEntity.getAttributeController();
                ac.setBaseAttribute(Attribute.SCALE, 1.06);
                ac.setBaseAttribute(Attribute.MAX_HEALTH, 30.0);

                PathfinderMob pathfinderMob = mobUtility.toPathFinderMobWithClearedGoal(dungeonEntity.getEntity());
                if (pathfinderMob == null) return;

                float avoidDistance = 8.0f;

                double fleeWalkSpeed = 0.8;
                double fleeSprintSpeed = 0.8;
                double followSpeed = 1.8;

                pathfinderMob.goalSelector.addGoal(1, new FloatGoal(pathfinderMob));
                pathfinderMob.goalSelector.addGoal(2, new AvoidEntityGoal<>(pathfinderMob, Player.class, avoidDistance, fleeWalkSpeed, fleeSprintSpeed));
                pathfinderMob.goalSelector.addGoal(3, new FollowMobGoal(pathfinderMob, followSpeed, FOLLOW_START_DISTANCE, FOLLOW_STOP_DISTANCE));
                pathfinderMob.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(pathfinderMob, 0.8));
                pathfinderMob.goalSelector.addGoal(7, new LookAtPlayerGoal(pathfinderMob, Player.class, 8.0f));
                pathfinderMob.goalSelector.addGoal(7, new RandomLookAroundGoal(pathfinderMob));
            });

            BiConsumer<DungeonEntity, TaskManager> task = (dungeonEntity, taskManager) -> {

                LivingEntity enchanter = dungeonEntity.getEntity();
                if (enchanter == null || enchanter.isDead()) return;

                List<Entity> nearbyEntities = enchanter.getNearbyEntities(BUFF_RANGE, BUFF_RANGE, BUFF_RANGE);
                List<LivingEntity> buffTargets = nearbyEntities.stream()
                        .filter(entity -> !(entity instanceof org.bukkit.entity.Player))
                        .filter(entity -> entity instanceof LivingEntity)
                        .map(entity -> (LivingEntity) entity)
                        .filter(livingEntity -> !(livingEntity instanceof Illusioner))
                        .filter(livingEntity -> !livingEntity.getUniqueId().equals(enchanter.getUniqueId()))
                        .limit(MAX_BUFF_TARGETS)
                        .toList();

                for (LivingEntity buffTarget : buffTargets) {

                    List<PotionEffectType> effects = new ArrayList<>(ENCHANTER_EFFECTS);
                    Collections.shuffle(effects);

                    int buffs = ThreadLocalRandom.current().nextInt(1, MAX_BUFF_EFFECTS + 1);
                    for (int i = 0; i < buffs; i++) {

                        int amplifier = ThreadLocalRandom.current().nextInt(0, MAX_BUFF_AMPLIFIER + 1);

                        buffTarget.addPotionEffect(new PotionEffect(
                                effects.get(i),
                                BUFF_DURATION,
                                amplifier,
                                false,
                                true,
                                true
                        ));
                    }
                }
            };
            mobBuilder.entityTask(new RepeatingTask<>(task, 0, REPEATING_PERIOD));
        };
    }
}