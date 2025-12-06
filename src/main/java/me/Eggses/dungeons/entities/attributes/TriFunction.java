package me.Eggses.dungeons.entities.attributes;

public interface TriFunction<T, U, V, R> {
    R transform(T t, U u, V v);
}