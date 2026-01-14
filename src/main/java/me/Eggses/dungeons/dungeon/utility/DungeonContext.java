package me.Eggses.dungeons.dungeon.utility;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.entities.mobs.EntityManager;
import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Supplier;

public class DungeonContext {

    public static final DungeonContext EMPTY = new DungeonContext();

    private final @Nullable DungeonInstance dungeonInstance;
    private final @Nullable World world;
    private final @Nullable EntityManager entityManager;
    private final @Nullable Graveyard graveyard;
    private final @Nullable Supplier<Collection<? extends Player>> players;

    public DungeonContext(DungeonContextBuilder dungeonContextBuilder) {
        this.dungeonInstance = dungeonContextBuilder.dungeonInstance;
        this.world = dungeonContextBuilder.world;
        this.entityManager = dungeonContextBuilder.entityManager;
        this.graveyard = dungeonContextBuilder.graveyard;
        this.players = dungeonContextBuilder.players;
    }

    public DungeonContext() {
        this.dungeonInstance = null;
        this.world = null;
        this.entityManager = null;
        this.graveyard = null;
        this.players = null;
    }

    public @Nullable World getWorld() {
        return world;
    }

    public @Nullable EntityManager getEntityManager() {
        return entityManager;
    }

    public @Nullable DungeonInstance getDungeonInstance() {
        return dungeonInstance;
    }

    public @Nullable Graveyard getGraveyard() {
        return graveyard;
    }

    public @Nullable Supplier<Collection<? extends Player>> getPlayers() {
        return players;
    }

    public static DungeonContextBuilder builder() {
        return new DungeonContextBuilder();
    }

    public static final class DungeonContextBuilder {
        private DungeonInstance dungeonInstance;
        private World world;
        private EntityManager entityManager;
        private Graveyard graveyard;
        private Supplier<Collection<? extends Player>> players;

        public DungeonContextBuilder dungeonInstance(DungeonInstance dungeonInstance) {
            this.dungeonInstance = dungeonInstance;
            return this;
        }

        public DungeonContextBuilder world(World world) {
            this.world = world;
            return this;
        }

        public DungeonContextBuilder entityManager(EntityManager entityManager) {
            this.entityManager = entityManager;
            return this;
        }

        public DungeonContextBuilder graveyard(Graveyard graveyard) {
            this.graveyard = graveyard;
            return this;
        }

        public DungeonContextBuilder players(Supplier<Collection<? extends Player>> players) {
            this.players = players;
            return this;
        }

        public DungeonContext build() {
            return new DungeonContext(this);
        }
    }
}
