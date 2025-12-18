package me.Eggses.dungeons.utility;

@Deprecated
@FunctionalInterface
public interface TriConsumer<T, U, V> {
    void accept(T t, U u, V v);
}