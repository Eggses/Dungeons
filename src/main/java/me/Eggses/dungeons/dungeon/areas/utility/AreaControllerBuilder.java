package me.Eggses.dungeons.dungeon.areas.utility;
import me.Eggses.dungeons.dispatch.ChunkMappingRegistry;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;

import java.util.*;
import java.util.function.Consumer;

public class AreaControllerBuilder {

    private final ChunkMappingRegistry<DungeonArea> dungeonAreaChunkMappingRegistry = new ChunkMappingRegistry<>();
    private final Map<Position, Consumer<DungeonContext>> blockInteractionMap = new HashMap<>();
    private final Map<Position, Consumer<DungeonContext>> dungeonTriggerCommandMap = new HashMap<>();

    public void addDungeonArea(DungeonArea dungeonArea) {
        dungeonAreaChunkMappingRegistry.add(dungeonArea, dungeonArea.getEntryRegion().getCoveredChunkKeys());
    }

    public void addBlockInteractionList(List<DungeonAction<Position>> blockInteractionList) {
        blockInteractionList.forEach(action
                -> blockInteractionMap.put(action.getK(), action.getAction()));
    }

    public void addDungeonTriggerCommandList(List<DungeonAction<Position>> dungeonTriggerCommandList) {
        dungeonTriggerCommandList.forEach(action
                -> dungeonTriggerCommandMap.put(action.getK(), action.getAction()));
    }

    public ChunkMappingRegistry<DungeonArea> getDungeonAreaChunkMapping() {
        return dungeonAreaChunkMappingRegistry.copy();
    }

    public Map<Position, Consumer<DungeonContext>> getBlockInteractionMap() {
        return new HashMap<>(blockInteractionMap);
    }

    public Map<Position, Consumer<DungeonContext>> getDungeonTriggerCommandMap() {
        return new HashMap<>(dungeonTriggerCommandMap);
    }
}