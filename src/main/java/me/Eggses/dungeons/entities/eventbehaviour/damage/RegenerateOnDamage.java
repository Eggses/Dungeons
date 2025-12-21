package me.Eggses.dungeons.entities.eventbehaviour.damage;

import me.Eggses.dungeons.entities.eventbehaviour.EventBehaviour;
import me.Eggses.dungeons.entities.eventbehaviour.EventContext;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RegenerateOnDamage implements EventBehaviour<EntityDamageByEntityEvent> {

    private static final int EFFECT_DURATION = 20 * 3;
    private static final int EFFECT_AMPLIFIER = 49;

    private static final PotionEffect POTION_EFFECT = new PotionEffect(
            PotionEffectType.REGENERATION,
            EFFECT_DURATION,
            EFFECT_AMPLIFIER,
            false,
            true,
            true);

    @Override
    public void handleEvent(DungeonEntity dungeonEntity, EntityDamageByEntityEvent event, EventContext eventContext) {
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
        livingEntity.addPotionEffect(POTION_EFFECT);
    }
}