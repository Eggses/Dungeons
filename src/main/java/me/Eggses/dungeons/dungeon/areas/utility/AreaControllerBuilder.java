package me.Eggses.dungeons.dungeon.areas.utility;
import me.Eggses.dungeons.dungeon.regions.Position;
import org.bukkit.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class AreaControllerBuilder {

    private final Map<Long, Set<DungeonArea>> dungeonAreasMap = new HashMap<>();
    private final Map<Position, Consumer<World>> blockInteractionMap = new HashMap<>();
    private final Map<Integer, Consumer<World>> dungeonTriggerCommandMap = new HashMap<>();

    public AreaControllerBuilder dungeonArea(DungeonArea dungeonArea) {

        Set<Long> encompassedChunkKeys = dungeonArea.getEntryRegion().getCoveredChunkKeys();

        for (Long chunkKey : encompassedChunkKeys) {
            dungeonAreasMap.putIfAbsent(chunkKey, new HashSet<>());

            Set<DungeonArea> dungeonAreas = dungeonAreasMap.get(chunkKey);
            dungeonAreas.add(dungeonArea);
        }
        return this;
    }

    public AreaControllerBuilder blockInteraction(DungeonAction<Position> blockInteraction) {
        blockInteractionMap.put(blockInteraction.getK(), blockInteraction.getAction());
        return this;
    }

    public AreaControllerBuilder dungeonTriggerCommand(DungeonAction<Integer> dungeonTriggerCommand) {
        dungeonTriggerCommandMap.put(dungeonTriggerCommand.getK(), dungeonTriggerCommand.getAction());
        return this;
    }

    public Map<Long, Set<DungeonArea>> getDungeonAreasMap() {
        return dungeonAreasMap;
    }

    public Map<Position, Consumer<World>> getBlockInteractionMap() {
        return blockInteractionMap;
    }

    public Map<Integer, Consumer<World>> getDungeonTriggerCommandMap() {
        return dungeonTriggerCommandMap;
    }
}