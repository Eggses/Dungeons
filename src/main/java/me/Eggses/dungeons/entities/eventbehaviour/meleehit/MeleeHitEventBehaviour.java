package me.Eggses.dungeons.entities.eventbehaviour.meleehit;

import me.Eggses.dungeons.entities.dungeonentity.mobs.DungeonEntity;
import me.Eggses.dungeons.entities.eventbehaviour.EventBehaviour;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface MeleeHitEventBehaviour extends EventBehaviour {
    void handleEvent(DungeonEntity dungeonEntity, EntityDamageByEntityEvent event);
}