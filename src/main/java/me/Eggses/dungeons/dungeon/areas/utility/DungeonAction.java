package me.Eggses.dungeons.dungeon.areas.utility;

import me.Eggses.dungeons.configuration.TriConsumer;
import me.Eggses.dungeons.dungeon.areas.EntityManager;
import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import org.bukkit.World;

@SuppressWarnings("ClassCanBeRecord")
public class DungeonAction<K> {

    private final K k;
    private final TriConsumer<World, EntityManager, Graveyard> action;

    public DungeonAction(K k, TriConsumer<World, EntityManager, Graveyard> action) {
        this.k = k;
        this.action = action;
    }

    public K getK() {
        return k;
    }

    public TriConsumer<World, EntityManager, Graveyard> getAction() {
        return action;
    }
}