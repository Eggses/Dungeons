package me.Eggses.dungeons.dungeon.areas;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.entities.mobs.DungeonMob;
import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.tasks.TaskManager;
import me.Eggses.dungeons.utility.MessageCreator;
import org.bukkit.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EntityManager {

    private final Map<UUID, DungeonEntity> dungeonEntities = new HashMap<>();

    private final World dungeonWorld;
    private final TaskManager taskManager;
    private final MessageCreator messageCreator;

    public EntityManager(World dungeonWorld, TaskManager taskManager, MessageCreator messageCreator) {
        this.dungeonWorld = dungeonWorld;
        this.taskManager = taskManager;
        this.messageCreator = messageCreator;
    }

    public boolean isEmpty() {
        return dungeonEntities.isEmpty();
    }

    public boolean contains(UUID uuid) {
        return dungeonEntities.containsKey(uuid);
    }

    public DungeonEntity getDungeonEntity(UUID uuid) {
        return dungeonEntities.get(uuid);
    }

    public void spawnMobs(List<MobBuilder> mobsToSpawn) {
        mobsToSpawn.forEach(this::spawnMob);
    }

    public void spawnMob(MobBuilder mobBuilder) {
        for (int i = 0; i < mobBuilder.getCount(); i++) {
            addMob(new DungeonMob(mobBuilder, dungeonWorld, taskManager, messageCreator));
        }
    }

    public void spawnMobList(List<MobBuilder> mobBuilders) {
        mobBuilders.forEach(this::spawnMob);
    }

    private void addMob(DungeonEntity dungeonEntity) {
        dungeonEntities.put(dungeonEntity.getUUID(), dungeonEntity);
    }

    public void removeMob(UUID uuid) {
        DungeonEntity dungeonEntity = dungeonEntities.remove(uuid);

        if (dungeonEntity == null) return;
        dungeonEntity.endTasks();
    }

    public void removeAll() {
        dungeonEntities.keySet().forEach(this::removeMob);
    }
}