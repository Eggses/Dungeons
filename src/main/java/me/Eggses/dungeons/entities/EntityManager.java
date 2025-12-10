package me.Eggses.dungeons.entities;

import me.Eggses.dungeons.entities.taskbehaviour.TaskManager;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.entities.mobs.DungeonMob;
import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.utility.MessageCreator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityManager {

    private final Map<UUID, DungeonEntity> entities = new HashMap<>();

    private final TaskManager taskManager;
    private final MessageCreator messageCreator;

    public EntityManager(TaskManager taskManager, MessageCreator messageCreator) {
        this.taskManager = taskManager;
        this.messageCreator = messageCreator;
    }

    public boolean contains(UUID uuid) {
        return entities.get(uuid) != null;
    }

    public DungeonEntity getDungeonEntity(UUID uuid) {
        return entities.get(uuid);
    }

    public void spawnMob(MobBuilder mobBuilder) {

        for (int i = 0; i < mobBuilder.getCount(); i++) {
            DungeonMob dungeonMob = new DungeonMob(mobBuilder, taskManager, messageCreator);
            entities.put(dungeonMob.getUUID(), dungeonMob);
        }
    }

    /*
    return boolean if sucessful, maybe method in each class
    can count how many mobs, count how ,many return trues
    to work out if an area is cleared.
     */
    public void removeMob(UUID uuid) {

        // remove on death
        DungeonEntity dungeonEntity = entities.remove(uuid);
        if (dungeonEntity == null) return;

        dungeonEntity.endTasks();
    }
}
