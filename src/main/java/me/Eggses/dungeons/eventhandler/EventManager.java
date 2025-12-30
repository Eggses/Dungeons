package me.Eggses.dungeons.eventhandler;

import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    private final Map<Class<? extends Event>, List<EventBehaviour<? extends Event>>> entityEventBehaviours = new HashMap<>();

    public <E extends Event> void addEventBehaviour(Class<E> eventClass, EventBehaviour<E> eventBehaviour) {

        entityEventBehaviours.putIfAbsent(eventClass, new ArrayList<>());
        List<EventBehaviour<? extends Event>> eventBehaviours = entityEventBehaviours.get(eventClass);
        eventBehaviours.add(eventBehaviour);
    }

    public <E extends Event> void handleEvent(E event, EventContext eventContext) {

        List<EventBehaviour<? extends Event>> eventBehaviours = entityEventBehaviours.get(event.getClass());
        if (eventBehaviours == null) return;

        for (EventBehaviour<? extends Event> eventBehaviour : eventBehaviours) {
            @SuppressWarnings("unchecked")
            EventBehaviour<E> trueEventBehaviour = (EventBehaviour<E>) eventBehaviour;

            trueEventBehaviour.handleEvent(event, eventContext);
        }
    }
}