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
        ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, valueInRange(0.37, 0.4));
        ac.setBaseAttribute(Attribute.KNOCKBACK_RESISTANCE, valueInRange(0.05, 0.12));

        if (dungeonEntity.getEntity() instanceof CraftEntity craftEntity) {
            if (craftEntity.getHandle() instanceof Mob mob) {
                mob.goalSelector.addGoal(4, new LeapAtTargetGoal(mob, 0.4F));
            }
        }
    }, new MobName("Fiend", false)),

    /*
    attributres are all wrong:
    knight stoo big, too much kb

    fiends are 1000x to fast
    jumping is weird

    I think the bees creeper spawns dont target players, they msut so cast to NMS

    jumping is wierd

    oh also name tags need to auto update to 0 on death... idk why  ithink maybe change
    event priority somehow who knows idk
     like mobs die with 3 as thier name or 6 for instance


i just saw a Knight KILL the bogged so wtf?

how does it get threat???

maybe fix by making the same team idk but???

wtf???

everytime mob dies ms gin console maybe prevent

when you get hit with an arrow its a NPE in ENtityDmaageByEntityEvent

[12:01:33 ERROR]: Could not pass event EntityDamageByEntityEvent to Dungeons v1.0
java.lang.NullPointerException: Cannot invoke "me.Eggses.dungeons.entities.mobs.DungeonEntity.getEntityEventBehaviour()" because "mob" is null
        at Dungeons-1.0.jar/me.Eggses.dungeons.listeners.EntityDamageEvent.onDamageEvent(EntityDamageEvent.java:46) ~[Dungeons-1.0.jar:?]
        at co.aikar.timings.TimedEventExecutor.execute(TimedEventExecutor.java:80) ~[paper-api-1.21.10-R0.1-SNAPSHOT.jar:?]
        at org.bukkit.plugin.RegisteredListener.callEvent(RegisteredListener.java:71) ~[paper-api-1.21.10-R0.1-SNAPSHOT.jar:?]
        at io.papermc.paper.plugin.manager.PaperEventManager.callEvent(PaperEventManager.java:54) ~[paper-1.21.10.jar:1.21.10-115-af06383]
        at io.papermc.paper.plugin.manager.PaperPluginManagerImpl.callEvent(PaperPluginManagerImpl.java:131) ~[paper-1.21.10.jar:1.21.10-115-af06383]
        at org.bukkit.plugin.SimplePluginManager.callEvent(SimplePluginManager.java:628) ~[paper-api-1.21.10-R0.1-SNAPSHOT.jar:?]
        at org.bukkit.event.Event.callEvent(Event.java:46) ~[paper-api-1.21.10-R0.1-SNAPSHOT.jar:?]




/*
attributers below suck
way to much to fast,  too shit
     */

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