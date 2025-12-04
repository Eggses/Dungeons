package me.Eggses.dungeons.attempt;

import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public interface DungeonEntity {
    UUID getUUID();
    LivingEntity getEntity();
}