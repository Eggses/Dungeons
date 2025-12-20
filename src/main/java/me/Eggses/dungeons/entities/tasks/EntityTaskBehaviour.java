package me.Eggses.dungeons.entities.tasks;

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