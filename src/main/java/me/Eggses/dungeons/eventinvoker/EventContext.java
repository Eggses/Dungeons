package me.Eggses.dungeons.eventinvoker;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class EventContext {

    public static final EventContext EMPTY = new EventContext();

    private final @Nullable DungeonEntity ownerOfBehaviour;
    private final @Nullable LivingEntity trueAttacker;

    private EventContext(EventContextBuilder builder) {
        this.ownerOfBehaviour = builder.ownerOfBehaviour;
        this.trueAttacker = builder.trueAttacker;
    }

    private EventContext() {
        this.ownerOfBehaviour = null;
        this.trueAttacker = null;
    }

    public @Nullable DungeonEntity getOwnerOfBehaviour() {
        return ownerOfBehaviour;
    }

    public @Nullable LivingEntity getTrueAttacker() {
        return trueAttacker;
    }

    public static EventContextBuilder builder() {
        return new EventContextBuilder();
    }

    public static final class EventContextBuilder {

        private DungeonEntity ownerOfBehaviour;
        private LivingEntity trueAttacker;

        private EventContextBuilder() {
        }

        public EventContextBuilder ownerOfBehaviour(DungeonEntity ownerOfBehaviour) {
            this.ownerOfBehaviour = ownerOfBehaviour;
            return this;
        }

        public EventContextBuilder trueAttacker(LivingEntity trueAttacker) {
            this.trueAttacker = trueAttacker;
            return this;
        }

        public EventContext build() {
            return new EventContext(this);
        }
    }
}