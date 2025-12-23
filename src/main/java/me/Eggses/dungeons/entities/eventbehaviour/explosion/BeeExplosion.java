package me.Eggses.dungeons.entities.eventbehaviour.explosion;

import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BeeExplosion implements EventBehaviour<ExplosionPrimeEvent> {

    private static final int BEES_TO_SPAWN = 3;

    private static final PotionEffect SPAWN_RESISTANCE = new PotionEffect(
            PotionEffectType.RESISTANCE,
            20,
            200,
            false,
            false,
            false
    );

    @Override
    public void handleEvent(ExplosionPrimeEvent event, EventContext eventContext) {

        if (!(event.getEntity() instanceof Creeper)) return;

        Location location = event.getEntity().getLocation();
        World world = location.getWorld();

        for (int i = 0; i < BEES_TO_SPAWN; i++) {
            Bee bee = world.spawn(location, Bee.class);
            bee.addPotionEffect(SPAWN_RESISTANCE);
            bee.setAnger(600000);
        }
    }
}