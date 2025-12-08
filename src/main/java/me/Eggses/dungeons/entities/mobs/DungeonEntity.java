package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.entities.eventbehaviour.EntityEventBehaviour;
import me.Eggses.dungeons.entities.nameutility.MobName;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public interface DungeonEntity {

    // Core Methods
    UUID getUUID();
    LivingEntity getEntity();
    void endTasks();
    EntityEventBehaviour getEntityEventBehaviour();

    // More Methods
    int getDungeonLevel();
    MobName getMobName();
    void updateName();

}
