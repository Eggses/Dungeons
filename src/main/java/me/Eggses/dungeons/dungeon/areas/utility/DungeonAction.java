package me.Eggses.dungeons.dungeon.areas.utility;

import org.bukkit.World;

import java.util.function.Consumer;

@SuppressWarnings("ClassCanBeRecord")
public class DungeonAction<K> {

    private final K k;
    private final Consumer<World> action;

    public DungeonAction(K k, Consumer<World> action) {
        this.k = k;
        this.action = action;
    }

    public K getK() {
        return k;
    }

    public Consumer<World> getAction() {
        return action;
    }
}