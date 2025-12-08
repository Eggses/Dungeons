package me.Eggses.dungeons.entities.eventbehaviour;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.event.Event;

public interface EventBehaviour<E extends Event> {
    void handleEvent(DungeonEntity dungeonEntity, E event);
}