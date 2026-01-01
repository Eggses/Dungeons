package me.Eggses.dungeons.tasks;

import java.util.function.Consumer;

public interface TaskContextProvider<O> {
    Consumer<TaskContext<O>> getTaskContext();
}
