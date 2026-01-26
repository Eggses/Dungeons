package me.Eggses.dungeons.entities.mobs;

import me.Eggses.dungeons.entities.attributes.AttributeController;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import java.util.UUID;

public interface DungeonEntity {

    UUID getUUID();
    LivingEntity getEntity();
    void endTasks();
    AttributeController getAttributeController();
    <E extends Event> void addEvent(Class<E> eventClass, EventBehaviour<E> eventBehaviour);
    <E extends Event> void handleEvent(E event, EventContext eventContext);
    int getDungeonLevel();
    MobName getMobName();
    void takeDamage(double damage);

    static boolean equalsIgnoreNull(DungeonEntity dungeonEntity, Entity entity) {
        if (dungeonEntity == null || entity == null) return false;
        if (!(entity instanceof LivingEntity livingEntity)) return false;
        return (livingEntity.equals(dungeonEntity.getEntity()));
    }
}