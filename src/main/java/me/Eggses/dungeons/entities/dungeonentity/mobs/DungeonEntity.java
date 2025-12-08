package me.Eggses.dungeons.entities.dungeonentity.mobs;

import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public interface DungeonEntity {
    UUID getUUID();
    void endTasks();
    LivingEntity getEntity();
    MobName getMobName();
    void updateName();
    int getDungeonLevel();
}
