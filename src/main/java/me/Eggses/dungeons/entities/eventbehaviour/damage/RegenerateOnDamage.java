package me.Eggses.dungeons.entities.eventbehaviour.damage;

import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RegenerateOnDamage implements EventBehaviour<EntityDamageByEntityEvent> {

    private static final int EFFECT_DURATION = 20 * 3;
    private static final int EFFECT_AMPLIFIER = 49;

    //TODO nametag will not get updated as it runs on health loss not gain.
    private static final PotionEffect POTION_EFFECT = new PotionEffect(
            PotionEffectType.REGENERATION,
            EFFECT_DURATION,
            EFFECT_AMPLIFIER,
            false,
            true,
            true);

    @Override
    public void handleEvent(EntityDamageByEntityEvent event, EventContext eventContext) {
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
        livingEntity.addPotionEffect(POTION_EFFECT);
    }
}