package me.Eggses.dungeons.entities.eventbehaviour.explosion;

import me.Eggses.dungeons.entities.eventbehaviour.EventBehaviour;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Bee;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BeeExplosion implements EventBehaviour<EntityExplodeEvent> {

    @Override
    public void handleEvent(DungeonEntity dungeonEntity, EntityExplodeEvent event) {

        Location location = event.getEntity().getLocation();
        World world = location.getWorld();

        for (int i = 0; i < 3; i++) {
            Bee bee = world.spawn(location, Bee.class);
            bee.setAnger(6000);
        }
    }
}