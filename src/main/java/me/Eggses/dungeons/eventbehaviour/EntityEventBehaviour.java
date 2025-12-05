package me.Eggses.dungeons.eventbehaviour;

import java.util.ArrayList;
import java.util.List;

public class EntityEventBehaviour {

    private final List<EventBehaviour> entityEventBehaviours = new ArrayList<>();

    public void addEventBehaviour(EventBehaviour eventBehaviour) {
        entityEventBehaviours.add(eventBehaviour);
    }

    public List<EventBehaviour> getEntityEventBehaviours() {
        return entityEventBehaviours;
    }
}