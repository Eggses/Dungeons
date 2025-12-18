package me.Eggses.dungeons.dungeon.areas.utility;

import me.Eggses.dungeons.dungeon.utility.DungeonContext;

import java.util.function.Consumer;

@SuppressWarnings("ClassCanBeRecord")
public class DungeonAction<K> {

    private final K k;
    private final Consumer<DungeonContext> action;

    public DungeonAction(K k, Consumer<DungeonContext> action) {
        this.k = k;
        this.action = action;
    }

    public K getK() {
        return k;
    }

    public Consumer<DungeonContext> getAction() {
        return action;
    }
}