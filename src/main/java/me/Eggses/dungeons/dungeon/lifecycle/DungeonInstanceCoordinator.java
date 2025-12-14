package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;

import java.util.Set;

public interface DungeonInstanceCoordinator {
    void openPortal(DungeonInstance dungeonInstance, Set<Long> portalChunkKeys);
    void closePortal(Set<Long> portalChunkKeys);
    void destroyInstance(DungeonInstance dungeonInstance);
}