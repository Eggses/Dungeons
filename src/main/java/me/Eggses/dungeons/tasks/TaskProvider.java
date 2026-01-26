package me.Eggses.dungeons.tasks;

@FunctionalInterface
public interface TaskProvider<O> {
    Task<O> getTask();
}
