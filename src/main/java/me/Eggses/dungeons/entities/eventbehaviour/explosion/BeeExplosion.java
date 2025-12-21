package me.Eggses.dungeons.entities.eventbehaviour.explosion;

import me.Eggses.dungeons.entities.eventbehaviour.EventBehaviour;
import me.Eggses.dungeons.entities.eventbehaviour.EventContext;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Bee;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BeeExplosion implements EventBehaviour<EntityExplodeEvent> {

    private static final int BEES_TO_SPAWN = 3;

    @Override
    public void handleEvent(DungeonEntity dungeonEntity, EntityExplodeEvent event, EventContext eventContext) {

        Location location = event.getLocation();
        World world = location.getWorld();

        for (int i = 0; i < BEES_TO_SPAWN; i++) {
            Bee bee = world.spawn(location, Bee.class);
            bee.setAnger(6000);
        }
    }
}