package me.Eggses.dungeons.dungeonentity;

import me.Eggses.dungeons.dungeonentity.mobs.DungeonEntity;
import me.Eggses.dungeons.dungeonentity.mobs.DungeonMob;
import me.Eggses.dungeons.dungeonentity.mobs.MobBuilder;

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
        DungeonMob dungeonMob = new DungeonMob(mobBuilder);
        entities.put(dungeonMob.getUUID(), dungeonMob);
        dungeonMob.startTasks(taskManager);
    }

    public void removeMob(UUID uuid) {

        DungeonEntity dungeonEntity = entities.remove(uuid);
        if (dungeonEntity == null) return;

        if (dungeonEntity instanceof DungeonMob dungeonMob) {
            dungeonMob.endTasks();
        }
    }
}
