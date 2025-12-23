package me.Eggses.dungeons.entities.eventbehaviour.damage;

import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class PoisonImpact implements EventBehaviour<EntityDamageByEntityEvent> {

    private static final int BUFF_DURATION = 20 * 10;
    private static final int MAX_AMPLIFIER = 1;

    @Override
    public void handleEvent(EntityDamageByEntityEvent event, EventContext eventContext) {

        Entity victim = event.getEntity();
        LivingEntity attacker = eventContext.getTrueAttacker();
        if (attacker == null) return;

        DungeonEntity dungeonEntity = eventContext.getOwnerOfBehaviour();
        if (dungeonEntity == null) return;
        if (!DungeonEntity.equals(dungeonEntity, attacker)) return;

        if (!(victim instanceof LivingEntity livingEntity)) return;

        int amplifier = ThreadLocalRandom.current().nextInt(0, MAX_AMPLIFIER + 1);

        livingEntity.addPotionEffect(new PotionEffect(
                PotionEffectType.POISON,
                BUFF_DURATION,
                amplifier,
                false,
                true,
                true
        ));
    }
}