package me.Eggses.dungeons.dungeon.files.reading;

@FunctionalInterface
public interface TriConsumer<T, U, V> {
    void accept(T t, U u, V v);
}