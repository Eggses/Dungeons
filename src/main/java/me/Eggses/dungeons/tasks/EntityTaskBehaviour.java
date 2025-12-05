package me.Eggses.dungeons.tasks;

import java.util.ArrayList;
import java.util.List;

public class EntityTaskBehaviour {

    private final List<EntityTask> entityTasks = new ArrayList<>();

    public void addEntityTask(EntityTask entityTask) {
        this.entityTasks.add(entityTask);
    }

    public List<EntityTask> getEntityTasks() {
        return entityTasks;
    }
}