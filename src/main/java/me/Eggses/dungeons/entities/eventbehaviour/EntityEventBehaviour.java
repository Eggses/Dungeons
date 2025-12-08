package me.Eggses.dungeons.entities.eventbehaviour;

import me.Eggses.dungeons.entities.eventbehaviour.damage.EntityDamageEntityBehaviour;
import me.Eggses.dungeons.entities.eventbehaviour.explosion.ExplosionBehaviour;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityEventBehaviour {

    private final List<EventBehaviour<?>> entityEventBehaviours = new ArrayList<>();

    public void addEventBehaviour(EventBehaviour<?> eventBehaviour) {
        entityEventBehaviours.add(eventBehaviour);
    }

    public void handleEntityDamageEntityEvent(DungeonEntity dungeonEntity, EntityDamageByEntityEvent event) {
        for (EventBehaviour<?> eventBehaviour : entityEventBehaviours) {
            if (eventBehaviour instanceof EntityDamageEntityBehaviour entityDamageEntityBehaviour) {
                entityDamageEntityBehaviour.handleEvent(dungeonEntity, event);
            }
        }
    }

    public void handleExplosionEvent(DungeonEntity dungeonEntity, EntityExplodeEvent entityExplodeEvent) {
        for (EventBehaviour<?> eventBehaviour : entityEventBehaviours) {
            if (eventBehaviour instanceof ExplosionBehaviour explosionBehaviour) {
                explosionBehaviour.handleEvent(dungeonEntity, entityExplodeEvent);
            }
        }
    }
}