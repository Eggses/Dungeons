package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.entities.taskbehaviour.EntityTaskBehaviour;
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

        ac.setBaseAttribute(Attribute.SCALE, 1.1);
        ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, 0.25);
        ac.setBaseAttribute(Attribute.KNOCKBACK_RESISTANCE, 0.3);
        ac.setBaseAttribute(Attribute.ATTACK_KNOCKBACK, 1.5);
    }, new EntityTaskBehaviour(), new MobName("Knight", false)),

    FIEND((dungeonEntity) -> {
        AttributeController ac = dungeonEntity.getAttributeController();

        ac.setBaseAttribute(Attribute.SCALE, 0.7);
        ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, 0.4);

        /*
        maybe remove this?
         */
        if (dungeonEntity.getEntity() instanceof CraftEntity craftEntity) {
            if (craftEntity.getHandle() instanceof Mob mob) {
                mob.goalSelector.addGoal(3, new LeapAtTargetGoal(mob, 0.4F));

                // clear its Goals,, find spider goals... copy those
                // and mayeb the targeting goals
                // and just yeah bing

                /*
                for illusioer you have to do the same....

                to allow for mobs to set thier own entity event TASKs!! consider illujsioers tasks..
                expose it somehow so that it overrides hte mob builder one...

                maybe a method on dungeon entity returns it,... and rather than being set
                in consutror to active tasks you manually call it idk.....

                 */
            }
        }
    }, new EntityTaskBehaviour(), new MobName("Fiend", false)),

    /*
    Zombie has same goals as spider then it works normally dont add have to fully fix goals

    Ghost -> charged creeper with same goals as zombie (forest dungeon) can lower render distance
    at specific points...

    !!!
     */

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

ALSO the fiends jumped backwards at while while attacking me.

bees dont target people

for fiend, maybe you need more NMS probably clear all goals and build the mob from scratch exactyl like spider maybe?
but then tpy eis locked

maybe forget jumping

add all mobs to same team idk to prevent idk the mobs fighting somehow???

it might actaully be worth simplifying mobs expontietally, remove tasks + ecvents from mobs
becuase they might be too complex to create.

instead, make bosses more unique / have thier aiblitties.

idk its also a time constraint..

for enchanter, extend the illisor class, make fiends normal


     */



/*
attributers below suck
way to much to fast,  too shit
     */

    BRUISER((dungeonEntity) -> {
        AttributeController ac = dungeonEntity.getAttributeController();
        ac.setBaseAttribute(Attribute.SCALE, 1.04);
        ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, 0.3);
    }, new EntityTaskBehaviour(), new MobName("Bruiser", false)),

    ENCHANTER((dungeonEntity) -> {
        AttributeController ac = dungeonEntity.getAttributeController();
        ac.setBaseAttribute(Attribute.SCALE, 1.1);

       /*
       fix this add in like run away
       and follow other mobs
        */

    }, new EntityTaskBehaviour(), new MobName("Enchanter", true)),

    WEB_THROWER((dungeonEntity) -> {

       /*
       fix this add in like run away
       and like pause basically i think..

       maybe end this if its hard... seems annoying...

       maybe this one has some intresting attrbiutes.
        */


    }, new EntityTaskBehaviour(), new MobName("Thrower", false));

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

    public static MobType getMobType(String mobType) {
        if (mobType == null) return null;
        try {
            return MobType.valueOf(mobType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}