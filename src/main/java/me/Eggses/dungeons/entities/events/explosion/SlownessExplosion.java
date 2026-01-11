package me.Eggses.dungeons.entities.events.explosion;

import me.Eggses.dungeons.eventinvoker.EventContext;
import me.Eggses.dungeons.eventinvoker.EventInvoker;
import me.Eggses.dungeons.eventinvoker.Invoker;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class SlownessExplosion implements Invoker {

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

    @EventInvoker
    public void handleEvent(ExplosionPrimeEvent event, EventContext eventContext) {

        Location location = event.getEntity().getLocation();
        Collection<Entity> nearby = location.getNearbyEntities(EFFECT_RADIUS, EFFECT_RADIUS, EFFECT_RADIUS);

        nearby.stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .forEach(player -> player.addPotionEffect(POTION_EFFECT));
    }
}