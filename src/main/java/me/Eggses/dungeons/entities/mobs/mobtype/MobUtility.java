package me.Eggses.dungeons.entities.mobs.mobtype;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.utility.misc.NMS;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;

@NMS()
public class MobUtility {

    public MobUtility() {
    }

    public PathfinderMob toPathFinderMobWithClearedGoal(LivingEntity livingEntity) {

        if (!(livingEntity instanceof CraftEntity craftEntity)) return null;
        if (!(craftEntity.getHandle() instanceof PathfinderMob pathfinderMob)) return null;

        pathfinderMob.goalSelector.removeAllGoals(goal -> true);
        pathfinderMob.targetSelector.removeAllGoals(goal -> true);

        return pathfinderMob;
    }

    public void toZombieStyleMeleeGoals(PathfinderMob pathfinderMob) {

        pathfinderMob.goalSelector.addGoal(1, new FloatGoal(pathfinderMob));

        if (pathfinderMob instanceof Monster monster) {
            pathfinderMob.goalSelector.addGoal(2, new SpearUseGoal<>(monster, 1.0, 1.0, 10.0f, 2.0f));
        }

        pathfinderMob.goalSelector.addGoal(3, new MeleeAttackGoal(pathfinderMob, 1.0, false));
        pathfinderMob.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(pathfinderMob, 1.0));
        pathfinderMob.goalSelector.addGoal(8, new LookAtPlayerGoal(pathfinderMob, Player.class, 8.0f));
        pathfinderMob.goalSelector.addGoal(8, new RandomLookAroundGoal(pathfinderMob));

        pathfinderMob.targetSelector.addGoal(1, new HurtByTargetGoal(pathfinderMob));
        pathfinderMob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(pathfinderMob, Player.class, true));
    }

    public void addAttackDamageAttribute(PathfinderMob pathfinderMob) {

        AttributeMap attributes = pathfinderMob.getAttributes();

        if (!attributes.hasAttribute(Attributes.ATTACK_DAMAGE)) {
            attributes.registerAttribute(Attributes.ATTACK_DAMAGE);
        }

        AttributeInstance attackDamage = attributes.getInstance(Attributes.ATTACK_DAMAGE);

        if (attackDamage != null) {
            attackDamage.setBaseValue(2.5);
        }
    }

    public void normaliseSize(DungeonEntity dungeonEntity) {
        if (dungeonEntity.getEntity() instanceof Ageable entity) {
            entity.setAdult();
        }
    }
}