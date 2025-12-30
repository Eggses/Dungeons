package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dispatch.ChunkMappingRegistry;
import me.Eggses.dungeons.dungeon.instance.DungeonInstance;

import java.util.Set;

public class DungeonOpenPortalRegistry {

    private final ChunkMappingRegistry<DungeonInstance> instancesWithOpenPortals = new ChunkMappingRegistry<>();

    public void addToOpenPortals(DungeonInstance dungeonInstance, Set<Long> portalChunkKeys) {
        instancesWithOpenPortals.add(dungeonInstance, portalChunkKeys);
    }

    public void removeFromOpenPortals(DungeonInstance dungeonInstance, Set<Long> portalChunkKeys) {
        instancesWithOpenPortals.remove(dungeonInstance, portalChunkKeys);
    }

    public Set<DungeonInstance> getDungeonInstance(long chunkKey) {
        return instancesWithOpenPortals.get(chunkKey);
    }
}