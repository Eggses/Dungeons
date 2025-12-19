package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.entities.taskbehaviour.EntityTaskBehaviour;
import me.Eggses.dungeons.utility.NMS;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;

import java.util.function.Consumer;

@NMS
public enum MobType {

    KNIGHT((dungeonEntity) -> {
        var ac = dungeonEntity.getAttributeController();
        ac.setBaseAttribute(Attribute.SCALE, 1.1);
        ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, 0.25);
        ac.setBaseAttribute(Attribute.KNOCKBACK_RESISTANCE, 0.3);
        ac.setBaseAttribute(Attribute.ATTACK_KNOCKBACK, 1.5);

    }, new EntityTaskBehaviour(), new MobName("Knight", false)),

    FIEND((dungeonEntity) -> {
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

    }, new EntityTaskBehaviour(), new MobName("Fiend", false)),

    BRUISER((dungeonEntity) -> {
        var ac = dungeonEntity.getAttributeController();
        ac.setBaseAttribute(Attribute.SCALE, 1.04);
        ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, 0.3);
    }, new EntityTaskBehaviour(), new MobName("Bruiser", false)),

    ENCHANTER((dungeonEntity) -> {
        var ac = dungeonEntity.getAttributeController();
        ac.setBaseAttribute(Attribute.SCALE, 1.1);

        PathfinderMob pathfinderMob = toPathFinderMobWithClearedGoal(dungeonEntity.getEntity());
        if (pathfinderMob == null) return;

        float avoidDistance = 8.0f;
        double fleeWalkSpeed = 1.0;
        double fleeSprintSpeed = 1.15;

        double followSpeed = 1.0;
        float followStartDistance = 18.0f;
        float followStopDistance  = 8.0f;

        pathfinderMob.goalSelector.addGoal(1, new FloatGoal(pathfinderMob));
        pathfinderMob.goalSelector.addGoal(2, new AvoidEntityGoal<>(pathfinderMob, Player.class, avoidDistance, fleeWalkSpeed, fleeSprintSpeed));
        pathfinderMob.goalSelector.addGoal(3, new FollowMobGoal(pathfinderMob, followSpeed, followStartDistance, followStopDistance));
        pathfinderMob.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(pathfinderMob, 0.8D));
        pathfinderMob.goalSelector.addGoal(7, new LookAtPlayerGoal(pathfinderMob, Player.class, 8.0F));
        pathfinderMob.goalSelector.addGoal(7, new RandomLookAroundGoal(pathfinderMob));

    }, new EntityTaskBehaviour(), new MobName("Enchanter", true)),
    ;

    private final Consumer<DungeonEntity> spawnFinalizer;
    private final EntityTaskBehaviour entityTaskBehaviour;
    private final MobName mobName;

    MobType(Consumer<DungeonEntity> spawnFinalizer, EntityTaskBehaviour entityTaskBehaviour, MobName mobName) {
        this.spawnFinalizer = spawnFinalizer;
        this.entityTaskBehaviour = entityTaskBehaviour;
        this.mobName = mobName;
    }

    public Consumer<DungeonEntity> getSpawnFinalizer() {
        return spawnFinalizer;
    }

    public EntityTaskBehaviour getEntityTaskBehaviour() {
        return entityTaskBehaviour;
    }

    public MobName getMobName() {
        return mobName;
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