package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DungeonOpenPortalRegistry {

    private final Map<Long, DungeonInstance> instancesWithOpenPortals = new HashMap<>();

    public void addToOpenPortals(DungeonInstance dungeonInstance, Set<Long> portalChunkKeys) {

        for (Long chunkKey : portalChunkKeys) {
            instancesWithOpenPortals.put(chunkKey, dungeonInstance);
        }
    }

    public void removeFromOpenPortals(Set<Long> portalChunkKeys) {

        for (Long chunkKey : portalChunkKeys) {
            instancesWithOpenPortals.remove(chunkKey);
        }
    }

    public DungeonInstance getDungeonInstance(long chunkKey) {
        return instancesWithOpenPortals.get(chunkKey);
    }
}