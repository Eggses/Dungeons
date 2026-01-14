package me.Eggses.dungeons.eventhandler;

import me.Eggses.dungeons.dungeon.events.extra.PoisonWater;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Locale;
import java.util.Map;

public class EventRegistry {

    private final Map<String, EventDefinition<?>> extraEvents = Map.of(
            "poisonwater", new EventDefinition<>(PlayerMoveEvent.class, PoisonWater::new)
    );

    public EventDefinition<?> getEventDefinition(String key) {
        if (key == null) return null;
        return extraEvents.get(key.toLowerCase(Locale.ROOT));
    }
}
