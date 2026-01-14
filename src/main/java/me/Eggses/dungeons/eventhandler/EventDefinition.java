package me.Eggses.dungeons.eventhandler;

import org.bukkit.event.Event;

import java.util.function.Supplier;

public record EventDefinition<E extends Event>(Class<E> eventClass, Supplier<EventBehaviour<E>> createEventBehaviour) {
}
