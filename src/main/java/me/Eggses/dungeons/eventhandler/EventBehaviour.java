package me.Eggses.dungeons.eventhandler;

import org.bukkit.event.Event;

@FunctionalInterface
public interface EventBehaviour<E extends Event> {
    void handleEvent(E event, EventContext eventContext);
}