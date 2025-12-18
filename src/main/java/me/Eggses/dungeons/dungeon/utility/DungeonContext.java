package me.Eggses.dungeons.dungeon.utility;

import me.Eggses.dungeons.dungeon.areas.EntityManager;
import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public class DungeonContext {

    private final @Nullable World world;
    private final @Nullable EntityManager entityManager;
    private final @Nullable Graveyard graveyard;

    public DungeonContext(@Nullable World world,
                          @Nullable EntityManager entityManager,
                          @Nullable Graveyard graveyard) {

        this.world = world;
        this.entityManager = entityManager;
        this.graveyard = graveyard;
    }

    public DungeonContext() {
        this(null, null, null);
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
}
