package me.Eggses.dungeons.eventhandler;

import org.bukkit.event.Event;

import java.util.*;

public class EventManager {

    private final Map<Class<? extends Event>, List<EventBehaviour<? extends Event>>> behaviours = new HashMap<>();

    public <E extends Event> void addEventBehaviour(Class<E> eventClass, EventBehaviour<E> eventBehaviour) {

        List<EventBehaviour<? extends Event>> eventBehaviours = behaviours.computeIfAbsent(eventClass,
                key -> new ArrayList<>()
        );
        eventBehaviours.add(eventBehaviour);
    }

    public <E extends Event> void handleEvent(E event, EventContext eventContext) {

        Class<?> currentClass = event.getClass();

        // If an Event reference can refer to an object whose class is currentClass.
        while (currentClass != null && Event.class.isAssignableFrom(currentClass)) {

            List<EventBehaviour<? extends Event>> eventBehaviours = behaviours.get(currentClass);
            currentClass = currentClass.getSuperclass();

            if (eventBehaviours == null) continue;

            for (EventBehaviour<? extends Event> eventBehaviour : eventBehaviours) {
                @SuppressWarnings("unchecked")
                EventBehaviour<? super E> trueEventBehaviour = (EventBehaviour<? super E>) eventBehaviour;
                trueEventBehaviour.handleEvent(event, eventContext);
            }
        }
    }

    public void removeAll() {
        behaviours.clear();
    }
}
