package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.tasks.TaskRunner;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import java.util.*;

public class EntityManager {

    private final Map<UUID, DungeonEntity> dungeonEntities = new HashMap<>();

    private final World dungeonWorld;
    private final TaskRunner taskRunner;
    private final MessageCreator messageCreator;
    private final TextFormatter textFormatter;

    public EntityManager(World dungeonWorld,
                         TaskRunner taskRunner,
                         MessageCreator messageCreator,
                         TextFormatter textFormatter) {

        this.dungeonWorld = dungeonWorld;
        this.taskRunner = taskRunner;
        this.messageCreator = messageCreator;
        this.textFormatter = textFormatter;
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

    public <E extends Event> void passEventToMobIfExists(Entity entity, E event, EventContext eventContext) {
        DungeonEntity dungeonEntity = dungeonEntities.get(entity.getUniqueId());
        if (dungeonEntity != null) dungeonEntity.handleEvent(event, eventContext);
    }

    public void spawnMob(MobBuilder mobBuilder) {
        for (int i = 0; i < mobBuilder.getCount(); i++) {
            addMob(new DungeonMob(mobBuilder, dungeonWorld, taskRunner, messageCreator, textFormatter));
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

        Set<UUID> keys = Set.copyOf(dungeonEntities.keySet());
        for (UUID uuid : keys) {
            removeMob(uuid);
        }
    }
}
