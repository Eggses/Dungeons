package me.Eggses.dungeons.entities.eventbehaviour.damage;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FireImpact implements EntityDamageEntityBehaviour {

    @Override
    public void handleEvent(DungeonEntity dungeonEntity, EntityDamageByEntityEvent event) {
        event.getEntity().setFireTicks(20 * 5);
    }
}