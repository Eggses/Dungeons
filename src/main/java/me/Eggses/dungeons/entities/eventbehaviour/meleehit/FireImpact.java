package me.Eggses.dungeons.entities.eventbehaviour.meleehit;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FireImpact implements MeleeHitEventBehaviour {

    @Override
    public void handleEvent(DungeonEntity dungeonEntity, EntityDamageByEntityEvent event) {
        event.getEntity().setFireTicks(20 * 5);
    }
}