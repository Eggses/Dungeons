package me.Eggses.dungeons.entities.eventbehaviour.damage;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FrostImpact implements EntityDamageEntityBehaviour {

    @Override
    public void handleEvent(DungeonEntity dungeonEntity, EntityDamageByEntityEvent event) {
        event.getEntity().setFreezeTicks(20 * 5);
    }
}
