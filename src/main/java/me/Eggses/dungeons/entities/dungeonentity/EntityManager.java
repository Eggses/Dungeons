package me.Eggses.dungeons.entities.dungeonentity;

import me.Eggses.dungeons.entities.dungeonentity.mobs.DungeonEntity;
import me.Eggses.dungeons.entities.dungeonentity.mobs.DungeonMob;
import me.Eggses.dungeons.entities.dungeonentity.mobs.MobBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityManager {

    private final Map<UUID, DungeonEntity> entities = new HashMap<>();

    private final TaskManager taskManager;

    public EntityManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void spawnMob(MobBuilder mobBuilder) {

        for (int i = 0; i < mobBuilder.getCount(); i++) {
            DungeonMob dungeonMob = new DungeonMob(mobBuilder);
            entities.put(dungeonMob.getUUID(), dungeonMob);
            dungeonMob.startTasks(taskManager);
        }
    }

    /*
    return boolean if sucessful, maybe method in each class
    can count how many mobs, count how ,many return trues
    to work out if an area is cleared.


     */
    public void removeMob(UUID uuid) {

        DungeonEntity dungeonEntity = entities.remove(uuid);
        if (dungeonEntity == null) return;

        dungeonEntity.endTasks();
    }
}
