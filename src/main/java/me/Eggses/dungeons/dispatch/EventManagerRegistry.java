package me.Eggses.dungeons.dispatch;

import me.Eggses.dungeons.eventinvoker.EventContext;
import me.Eggses.dungeons.eventinvoker.EventRegistry;
import me.Eggses.dungeons.eventinvoker.Invoker;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class EventManagerRegistry<T> {

    private final Map<T, EventRegistry> registry = new HashMap<>();
    private final Logger logger;

    public EventManagerRegistry(Logger logger) {
        this.logger = logger;
    }

    public void addOrUpdate(T t, Invoker invoker) {
        EventRegistry eventRegistry = registry.computeIfAbsent(t, k -> new EventRegistry(logger));
        eventRegistry.registerInvoker(invoker);
    }

    public void handleEvent(T t, Event event, EventContext eventContext) {

        EventRegistry eventRegistry = registry.get(t);
        if (eventRegistry == null) return;

        eventRegistry.handleEvent(event, eventContext);
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