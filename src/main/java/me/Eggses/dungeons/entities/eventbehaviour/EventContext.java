package me.Eggses.dungeons.entities.eventbehaviour;

import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class EventContext {

    public static final EventContext EMPTY = new EventContext();

    private final @Nullable LivingEntity trueAttacker;

    public EventContext(@Nullable LivingEntity trueAttacker) {
        this.trueAttacker = trueAttacker;
    }

    public @Nullable LivingEntity getTrueAttacker() {
        return trueAttacker;
    }

    private EventContext() {
        this.trueAttacker = null;
    }
}
