package me.Eggses.dungeons.eventbehaviour.meleehit;

import me.Eggses.dungeons.eventbehaviour.EventBehaviour;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface MeleeHitEventBehaviour extends EventBehaviour {
    void handleEvent(EntityDamageByEntityEvent event);
}