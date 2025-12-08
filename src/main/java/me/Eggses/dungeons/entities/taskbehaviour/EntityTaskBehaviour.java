package me.Eggses.dungeons.entities.taskbehaviour;

import java.util.ArrayList;
import java.util.List;

public class EntityTaskBehaviour {

    private final List<EntityTask> entityTasks = new ArrayList<>();

    public EntityTaskBehaviour addEntityTask(EntityTask entityTask) {
        this.entityTasks.add(entityTask);
        return this;
    }

    public List<EntityTask> getEntityTasks() {
        return entityTasks;
    }
}