package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.eventinvoker.EventContext;
import me.Eggses.dungeons.entities.nameutility.MobName;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import java.util.UUID;

public interface DungeonEntity {

    // Core Methods
    UUID getUUID();
    LivingEntity getEntity();
    void endTasks();
    void handleEvent(Event event, EventContext eventContext);
    AttributeController getAttributeController();

    // More Methods
    int getDungeonLevel();
    MobName getMobName();
    void updateHealthDisplay(double damageToBeTaken);

    static boolean equals(DungeonEntity dungeonEntity, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) return false;
        return (livingEntity.equals(dungeonEntity.getEntity()));
    }
}