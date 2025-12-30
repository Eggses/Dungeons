package me.Eggses.dungeons.dispatch;

import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.eventhandler.EventManager;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class EventManagerRegistry<T> {

    private final Map<T, EventManager> registry = new HashMap<>();

    public <E extends Event> void addOrUpdate(T t, Class<E> eventClass, EventBehaviour<E> eventBehaviour) {

        registry.putIfAbsent(t, new EventManager());
        EventManager eventManager = registry.get(t);

        eventManager.addEventBehaviour(eventClass, eventBehaviour);
    }

    public <E extends Event> void handleEvent(T t, E event, EventContext eventContext) {

        EventManager eventManager = registry.get(t);
        if (eventManager == null) return;

        eventManager.handleEvent(event, eventContext);
    }

    public void remove(T t) {
        registry.remove(t);
    }

    public void removeAll(Predicate<T> shouldRemove) {

        Set<T> keySet = Set.copyOf(registry.keySet());

        for (T t : keySet) {
            if (shouldRemove.test(t)) {
                registry.remove(t);
            }
        }
    }

    public Set<T> getKeySet() {
        return Set.copyOf(registry.keySet());
    }
}