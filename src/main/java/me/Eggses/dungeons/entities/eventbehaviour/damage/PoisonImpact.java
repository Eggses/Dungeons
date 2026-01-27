package me.Eggses.dungeons.entities.eventbehaviour.damage;

import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
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

        LivingEntity attacker = eventContext.getTrueAttacker();
        DungeonEntity owner = eventContext.getOwnerOfBehaviour();
        if (!DungeonEntity.equalsIgnoreNull(owner, attacker)) return;

        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;

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