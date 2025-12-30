package me.Eggses.dungeons.dispatch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChunkMappingRegistry<T> {

    private final Map<Long, Set<T>> chunkKeyToSetMap = new HashMap<>();

    public Set<T> get(long chunkKey) {
        return chunkKeyToSetMap.get(chunkKey);
    }

    public void add(T t, Set<Long> encompassedChunkKeys) {
        for (Long chunkKey : encompassedChunkKeys) {
            chunkKeyToSetMap.putIfAbsent(chunkKey, new HashSet<>());
            Set<T> set = chunkKeyToSetMap.get(chunkKey);
            set.add(t);
        }
    }

    public void remove(T t, Set<Long> encompassedChunkKeys) {
        for (Long chunkKey : encompassedChunkKeys) {

            Set<T> set = chunkKeyToSetMap.get(chunkKey);
            if (set == null) continue;

            set.remove(t);

            if (set.isEmpty()) chunkKeyToSetMap.remove(chunkKey);
        }
    }
}