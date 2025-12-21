package me.Eggses.dungeons.entities.eventbehaviour;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityEventBehaviour {

    private final Map<Class<? extends Event>, List<EventBehaviour<? extends Event>>> entityEventBehaviours = new HashMap<>();

    public <E extends Event> void addEventBehaviour(Class<E> eventBehaviourClass,
                                                    EventBehaviour<E> eventBehaviour) {

        entityEventBehaviours.putIfAbsent(eventBehaviourClass, new ArrayList<>());
        List<EventBehaviour<? extends Event>> eventBehaviours = entityEventBehaviours.get(eventBehaviourClass);
        eventBehaviours.add(eventBehaviour);
    }

    public <E extends Event> void handleEvent(DungeonEntity dungeonEntity, E event, EventContext eventContext) {

        List<EventBehaviour<? extends Event>> eventBehaviours = entityEventBehaviours.get(event.getClass());
        if (eventBehaviours == null) return;

        if (event.getClass() == EntityExplodeEvent.class) {
            System.out.println("We called handle event in the entity event behaviour and the class is the explode event");
            System.out.println("The List of Event behaviours is this long: " + eventBehaviours.size());
        }

        for (EventBehaviour<? extends Event> eventBehaviour : eventBehaviours) {
            @SuppressWarnings("unchecked")
            EventBehaviour<E> trueEventBehaviour = (EventBehaviour<E>) eventBehaviour;

            trueEventBehaviour.handleEvent(dungeonEntity, event, eventContext);
        }
    }
}