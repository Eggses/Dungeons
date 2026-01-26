package me.Eggses.dungeons.tasks;

@FunctionalInterface
public interface Task<O> {
    void runTask(TaskContext<O> taskContext);
}
