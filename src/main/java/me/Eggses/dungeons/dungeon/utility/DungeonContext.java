package me.Eggses.dungeons.dungeon.utility;

import me.Eggses.dungeons.dungeon.areas.EntityManager;
import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Supplier;

@SuppressWarnings("ClassCanBeRecord")
public class DungeonContext {

    public static final DungeonContext EMPTY = new DungeonContext(
            null,
            null,
            null,
            null);

    private final @Nullable World world;
    private final @Nullable EntityManager entityManager;
    private final @Nullable Graveyard graveyard;
    private final @Nullable Supplier<Collection<? extends Player>> players;

    public DungeonContext(@Nullable World world,
                          @Nullable EntityManager entityManager,
                          @Nullable Graveyard graveyard,
                          @Nullable Supplier<Collection<? extends Player>> players) {

        this.world = world;
        this.entityManager = entityManager;
        this.graveyard = graveyard;
        this.players = players;
    }

    public @Nullable World getWorld() {
        return world;
    }

    public @Nullable EntityManager getEntityManager() {
        return entityManager;
    }

    public @Nullable Graveyard getGraveyard() {
        return graveyard;
    }

    public @Nullable Supplier<Collection<? extends Player>> getPlayers() {
        return players;
    }
}