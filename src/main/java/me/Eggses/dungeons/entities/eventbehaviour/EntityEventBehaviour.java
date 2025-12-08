package me.Eggses.dungeons.entities.eventbehaviour;

import me.Eggses.dungeons.entities.dungeonentity.mobs.DungeonEntity;
import me.Eggses.dungeons.entities.eventbehaviour.meleehit.MeleeHitEventBehaviour;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityEventBehaviour {

    private final List<EventBehaviour> entityEventBehaviours = new ArrayList<>();

    public void addEventBehaviour(EventBehaviour eventBehaviour) {
        entityEventBehaviours.add(eventBehaviour);
    }

    public void handleMeleeHitEvent(DungeonEntity dungeonEntity, EntityDamageByEntityEvent event) {
        for (EventBehaviour eventBehaviour : entityEventBehaviours) {
            if (eventBehaviour instanceof MeleeHitEventBehaviour meleeHitEventBehaviour) {
                meleeHitEventBehaviour.handleEvent(dungeonEntity, event);
            }
        }
    }
}