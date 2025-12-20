package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.entities.tasks.EntityTask;
import me.Eggses.dungeons.utility.NMS;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

@NMS("Used to change goals of mobs.")
public enum MobType {

    KNIGHT(mobBuilder -> {

        mobBuilder.mobName(new MobName("Knight", false));

        mobBuilder.spawnChanges((dungeonEntity -> {
            var ac = dungeonEntity.getAttributeController();
            ac.setBaseAttribute(Attribute.SCALE, 1.1);
            ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, 0.25);
            ac.setBaseAttribute(Attribute.KNOCKBACK_RESISTANCE, 0.3);
            ac.setBaseAttribute(Attribute.ATTACK_KNOCKBACK, 1.5);
        }));
    }),

    FIEND(mobBuilder -> {

        mobBuilder.mobName(new MobName("Fiend", false));

        mobBuilder.spawnChanges(dungeonEntity -> {
            var ac = dungeonEntity.getAttributeController();

            ac.setBaseAttribute(Attribute.SCALE, 0.7);
            ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, 0.4);

            PathfinderMob pathfinderMob = toPathFinderMobWithClearedGoal(dungeonEntity.getEntity());
            if (pathfinderMob == null) return;

            pathfinderMob.goalSelector.addGoal(1, new FloatGoal(pathfinderMob));
            pathfinderMob.goalSelector.addGoal(3, new LeapAtTargetGoal(pathfinderMob, 0.4f));
            pathfinderMob.goalSelector.addGoal(4, new MeleeAttackGoal(pathfinderMob, 1.2, true));
            pathfinderMob.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(pathfinderMob, 0.8));
            pathfinderMob.goalSelector.addGoal(6, new LookAtPlayerGoal(pathfinderMob, Player.class, 8.0f));
            pathfinderMob.goalSelector.addGoal(6, new RandomLookAroundGoal(pathfinderMob));

            pathfinderMob.targetSelector.addGoal(1, new HurtByTargetGoal(pathfinderMob));
            pathfinderMob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(pathfinderMob, Player.class, true));
        });
    }),

    BRUISER(mobBuilder -> {

        mobBuilder.mobName(new MobName("Bruiser", false));

        mobBuilder.spawnChanges(dungeonEntity -> {
            var ac = dungeonEntity.getAttributeController();
            ac.setBaseAttribute(Attribute.SCALE, 1.04);
            ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, 0.3);
        });
    }),

    ENCHANTER(mobBuilder -> {

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

        mobBuilder.mobName(new MobName("Enchanter", true));

        mobBuilder.spawnChanges(dungeonEntity -> {
            var ac = dungeonEntity.getAttributeController();
            ac.setBaseAttribute(Attribute.SCALE, 1.1);
            ac.setBaseAttribute(Attribute.MAX_HEALTH, 30.0);

            PathfinderMob pathfinderMob = toPathFinderMobWithClearedGoal(dungeonEntity.getEntity());
            if (pathfinderMob == null) return;

            float avoidDistance = 8.0f;
            double fleeWalkSpeed = 1.0;
            double fleeSprintSpeed = 1.15;

            double followSpeed = 1.0;

            pathfinderMob.goalSelector.addGoal(1, new FloatGoal(pathfinderMob));
            pathfinderMob.goalSelector.addGoal(2, new AvoidEntityGoal<>(pathfinderMob, Player.class, avoidDistance, fleeWalkSpeed, fleeSprintSpeed));
            pathfinderMob.goalSelector.addGoal(3, new FollowMobGoal(pathfinderMob, followSpeed, FOLLOW_START_DISTANCE, FOLLOW_STOP_DISTANCE));
            pathfinderMob.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(pathfinderMob, 0.8D));
            pathfinderMob.goalSelector.addGoal(7, new LookAtPlayerGoal(pathfinderMob, Player.class, 8.0F));
            pathfinderMob.goalSelector.addGoal(7, new RandomLookAroundGoal(pathfinderMob));
        });

        mobBuilder.entityTask(EntityTask.repeating((dungeonEntity, taskManager) -> {

            LivingEntity enchanter = dungeonEntity.getEntity();
            if (enchanter == null || enchanter.isDead()) return;

            List<Entity> nearbyEntities = enchanter.getNearbyEntities(BUFF_RANGE, BUFF_RANGE, BUFF_RANGE);
            List<LivingEntity> buffTargets = nearbyEntities.stream()
                    .filter(entity -> entity instanceof LivingEntity)
                    .map(entity -> (LivingEntity) entity)
                    .filter(livingEntity -> !(livingEntity instanceof Player))
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
        }, 0, REPEATING_PERIOD));
    }),

    POISON_COW(mobBuilder -> {

        /*
        this AI is probably quite universal.. maybe add a lambda for it... that accepts a COnsumer<Pathfindermob>
        and applies general AI for a typical enemy... add a static lambda in the enum below.
         */

        // base health 20
        // base attack same as zombie
        // clear all
        // set its AI to that of a zombie
        // give it a on hit apply posion event...
        // done.
    }),
    VILLAGER(mobBuilder -> {
        // immortal bassicla
    }),
    BEEHIVE_CREEPER( mobBuilder -> {
        // behive on head
        // spwan bees on death event or epxlosion idc.
    })

    ;

    private final Consumer<MobBuilder> mobBuilderConsumer;

    MobType(Consumer<MobBuilder> mobBuilderConsumer) {
        this.mobBuilderConsumer = mobBuilderConsumer;
    }

    public Consumer<MobBuilder> getMobBuilder() {
        return mobBuilderConsumer;
    }

    private static PathfinderMob toPathFinderMobWithClearedGoal(LivingEntity livingEntity) {

        if (!(livingEntity instanceof CraftEntity craftEntity)) return null;
        if (!(craftEntity.getHandle() instanceof PathfinderMob pathfinderMob)) return null;

        pathfinderMob.goalSelector.removeAllGoals(goal -> true);
        pathfinderMob.targetSelector.removeAllGoals(goal -> true);

        return pathfinderMob;
    }

    public static MobType getMobType(String mobType) {
        if (mobType == null) return null;
        try {
            return MobType.valueOf(mobType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}