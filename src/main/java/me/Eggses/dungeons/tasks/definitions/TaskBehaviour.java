package me.Eggses.dungeons.tasks.definitions;

import java.util.ArrayList;
import java.util.List;

public class TaskBehaviour<T> {

    private final List<TaskDefinition<T>> taskDefinitions = new ArrayList<>();

    public void addTask(TaskDefinition<T> task) {
        taskDefinitions.add(task);
    }

    public List<TaskDefinition<T>> getTaskDefinitions() {
        return taskDefinitions;
    }
}
