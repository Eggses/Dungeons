package me.Eggses.dungeons.entities.eventbehaviour;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.event.Event;

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

    public <E extends Event> void handleEvent(DungeonEntity dungeonEntity, E event ) {

        List<EventBehaviour<? extends Event>> eventBehaviours = entityEventBehaviours.get(event.getClass());
        if (eventBehaviours == null) return;

        for (EventBehaviour<? extends Event> eventBehaviour : eventBehaviours) {
            @SuppressWarnings("unchecked")
            EventBehaviour<E> trueEventBehaviour = (EventBehaviour<E>) eventBehaviour;

            trueEventBehaviour.handleEvent(dungeonEntity, event);
        }
    }
}