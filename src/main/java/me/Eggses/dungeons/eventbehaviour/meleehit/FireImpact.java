package me.Eggses.dungeons.eventbehaviour.meleehit;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FireImpact implements MeleeHitEventBehaviour {

    @Override
    public void handleEvent(EntityDamageByEntityEvent event) {
        event.getEntity().setFireTicks(20 * 5);
    }
}