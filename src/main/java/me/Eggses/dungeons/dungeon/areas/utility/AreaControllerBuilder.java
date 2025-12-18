package me.Eggses.dungeons.dungeon.areas.utility;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;

import java.util.*;
import java.util.function.Consumer;

public class AreaControllerBuilder {

    private final Map<Long, Set<DungeonArea>> dungeonAreasMap = new HashMap<>();
    private final Map<Position, Consumer<DungeonContext>> blockInteractionMap = new HashMap<>();
    private final Map<String, Consumer<DungeonContext>> dungeonTriggerCommandMap = new HashMap<>();

    public void addDungeonArea(DungeonArea dungeonArea) {

        Set<Long> encompassedChunkKeys = dungeonArea.getEntryRegion().getCoveredChunkKeys();

        for (Long chunkKey : encompassedChunkKeys) {
            dungeonAreasMap.putIfAbsent(chunkKey, new HashSet<>());

            Set<DungeonArea> dungeonAreas = dungeonAreasMap.get(chunkKey);
            dungeonAreas.add(dungeonArea);
        }
    }

    public void addBlockInteractionList(List<DungeonAction<Position>> blockInteractionList) {
        blockInteractionList.forEach(action
                -> blockInteractionMap.put(action.getK(), action.getAction()));
    }

    public void addDungeonTriggerCommandList(List<DungeonAction<String>> dungeonTriggerCommandList) {
        dungeonTriggerCommandList.forEach(action
                -> dungeonTriggerCommandMap.put(action.getK(), action.getAction()));
    }

    public Map<Long, Set<DungeonArea>> getDungeonAreasMap() {
        return dungeonAreasMap;
    }

    public Map<Position, Consumer<DungeonContext>> getBlockInteractionMap() {
        return blockInteractionMap;
    }

    public Map<String, Consumer<DungeonContext>> getDungeonTriggerCommandMap() {
        return dungeonTriggerCommandMap;
    }
}