package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.utility.NMS;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.entity.CraftEntity;

import java.util.function.Consumer;

@NMS
public enum MobType {

    KNIGHT((dungeonEntity) -> {
        AttributeController ac = dungeonEntity.getAttributeController();
        ac.setBaseAttribute(Attribute.SCALE, valueInRange(1.05, 1.25));
        ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, valueInRange(0.23, 0.27));
        ac.setBaseAttribute(Attribute.KNOCKBACK_RESISTANCE, valueInRange(0.28, 0.45));
        ac.setBaseAttribute(Attribute.ATTACK_KNOCKBACK, valueInRange(1.6, 2.2));
    }, new MobName("Knight", false)),

    FIEND((dungeonEntity) -> {
        AttributeController ac = dungeonEntity.getAttributeController();
        ac.setBaseAttribute(Attribute.SCALE, valueInRange(0.55, 0.75));
        ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, valueInRange(0.37, 0.5));
        ac.setBaseAttribute(Attribute.KNOCKBACK_RESISTANCE, valueInRange(0.05, 0.12));

        if (dungeonEntity.getEntity() instanceof CraftEntity craftEntity) {
            if (craftEntity.getHandle() instanceof Mob mob) {
                mob.goalSelector.addGoal(3, new LeapAtTargetGoal(mob, 0.4F));
            }
        }
    }, new MobName("Fiend", false)),

    BRUISER((dungeonEntity) -> {
        AttributeController ac = dungeonEntity.getAttributeController();
        ac.setBaseAttribute(Attribute.SCALE, valueInRange(1.0, 1.1));
        ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, valueInRange(0.28, 0.32));
        ac.setBaseAttribute(Attribute.KNOCKBACK_RESISTANCE, valueInRange(0.15, 0.22));
        ac.setBaseAttribute(Attribute.ATTACK_KNOCKBACK, valueInRange(0.8, 1.2));
    }, new MobName("Bruiser", false)),

    ENCHANTER((dungeonEntity) -> {
        AttributeController ac = dungeonEntity.getAttributeController();
        ac.setBaseAttribute(Attribute.SCALE, valueInRange(1.1, 1.2));
        ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, valueInRange(0.22, 0.26));

       /*
       fix this add in like run away
       and follow other mobs
        */

    }, new MobName("Enchanter", true)),

    WEB_THROWER((dungeonEntity) -> {

       /*
       fix this add in like run away
       and like pause basically i think..

       maybe end this if its hard... seems annoying...

       maybe this one has some intresting attrbiutes.
        */


    }, new MobName("Thrower", false));

    private final Consumer<DungeonEntity> spawnFinalizer;
    private final MobName mobName;

    MobType(Consumer<DungeonEntity> spawnFinalizer, MobName mobName) {
        this.spawnFinalizer = spawnFinalizer;
        this.mobName = mobName;
    }

    public Consumer<DungeonEntity> getSpawnFinalizer() {
        return spawnFinalizer;
    }

    public MobName getMobName() {
        return mobName;
    }

    public static double valueInRange(double lowest, double highest) {
        return lowest + Math.random() * (highest - lowest);
    }
}