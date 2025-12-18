package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import java.util.Set;

public interface DungeonInstanceCoordinator {
    void openPortal(DungeonInstance dungeonInstance, Set<Long> portalChunkKeys);
    void closePortal(Set<Long> portalChunkKeys);
    void destroyInstanceRuntime(DungeonInstance dungeonInstance);
    void destroyWorld(String folderName);

    void endAllInstances(boolean destroyWorldFolder);
    void destroyLeftAllInstanceWorlds();
}