package me.Eggses.dungeons.entities.eventbehaviour.damage;

import me.Eggses.dungeons.entities.eventbehaviour.EventBehaviour;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FireImpact implements EventBehaviour<EntityDamageByEntityEvent> {

    @Override
    public void handleEvent(DungeonEntity dungeonEntity, EntityDamageByEntityEvent event) {
        // wont work called when it is the victum too.
        event.getEntity().setFireTicks(20 * 5);
    }
}