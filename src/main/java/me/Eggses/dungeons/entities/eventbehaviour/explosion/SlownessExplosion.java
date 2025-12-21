package me.Eggses.dungeons.entities.eventbehaviour.explosion;

import me.Eggses.dungeons.entities.eventbehaviour.EventBehaviour;
import me.Eggses.dungeons.entities.eventbehaviour.EventContext;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class SlownessExplosion implements EventBehaviour<EntityExplodeEvent> {

    private static final double EFFECT_RADIUS = 7.0;

    private static final int EFFECT_DURATION = 20 * 4;
    private static final int EFFECT_AMPLIFIER = 0;

    private static final PotionEffect POTION_EFFECT = new PotionEffect(
            PotionEffectType.SLOWNESS,
            EFFECT_DURATION,
            EFFECT_AMPLIFIER,
            false,
            true,
            true);

    @Override
    public void handleEvent(DungeonEntity dungeonEntity, EntityExplodeEvent event, EventContext eventContext) {

        Location location = event.getLocation();
        Collection<Entity> nearby = location.getNearbyEntities(EFFECT_RADIUS, EFFECT_RADIUS, EFFECT_RADIUS);

        nearby.stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .forEach(player -> player.addPotionEffect(POTION_EFFECT));
    }
}