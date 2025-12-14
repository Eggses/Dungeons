package me.Eggses.dungeons.dungeon.progress;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.entities.taskbehaviour.TaskManager;
import me.Eggses.dungeons.utility.MessageCreator;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;
import java.util.function.Consumer;

public class AreaController {

    private final Map<Long, Set<DungeonArea>> dungeonAreaMap = new HashMap<>();
    private final Map<Position, Consumer<World>> customBlocksMap = new HashMap<>();
    private final Map<Integer, Consumer<World>> dungeonTriggerCommandMap = new HashMap<>();

    private final AreaControllerBuilder areaControllerBuilder;
    private final EntityManager entityManager;
    private final World dungeonWorld;

    private DungeonArea activeDungeonArea = null;
    private boolean areaInProgress = false;

    public AreaController(AreaControllerBuilder areaControllerBuilder,
                          World dungeonWorld,
                          TaskManager taskManager,
                          MessageCreator messageCreator) {

        this.areaControllerBuilder = areaControllerBuilder;
        this.entityManager = new EntityManager(taskManager, messageCreator);
        this.dungeonWorld = dungeonWorld;
    }

    /* =========================================================
     * Event routing
     * ========================================================= */

    public void handleMovementEventInDungeon(Location location, long chunkKey) {

        if (areaInProgress) return;

        Set<DungeonArea> dungeonAreasAtChunk = dungeonAreaMap.get(chunkKey);
        if (dungeonAreasAtChunk == null) return;

        for (DungeonArea dungeonArea : dungeonAreasAtChunk) {
            if (dungeonArea.getEntryRegion().within(location)) {
                beginArea(dungeonArea);
                return;
            }
        }
    }

    public void handleInteractEvent(Position positionOfBlock) {
        Consumer<World> consumer = customBlocksMap.get(positionOfBlock);
        if (consumer == null) return;
        consumer.accept(dungeonWorld);
    }

    public void handleDungeonTriggerCommand(Integer argumentValue) {
        Consumer<World> consumer = dungeonTriggerCommandMap.get(argumentValue);
        if (consumer == null) return;
        consumer.accept(dungeonWorld);
    }

    public void handleEntityDeathEvent(UUID uuid) {
        entityManager.removeMob(uuid);
        if (entityManager.isEmpty()) endActiveArea();
    }

    /* =========================================================
     * Area Manager
     * ========================================================= */

    private void beginArea(DungeonArea dungeonArea) {
        activeDungeonArea = dungeonArea;
        areaInProgress = true;

        dungeonArea.onEnterFirstTime(dungeonWorld, entityManager);
        remove(dungeonArea);
    }


    private void endActiveArea() {
        if (activeDungeonArea == null || !areaInProgress) return;

        activeDungeonArea.onClearArea(dungeonWorld);
        activeDungeonArea = null;
        areaInProgress = false;
    }

    private void remove(DungeonArea dungeonArea) {

        Set<Long> encompassedChunkKeys = dungeonArea.getEntryRegion().getCoveredChunkKeys();

        for (Long chunkKey : encompassedChunkKeys) {

            Set<DungeonArea> dungeonAreasAtChunk = dungeonAreaMap.get(chunkKey);
            if (dungeonAreasAtChunk == null) continue;
            dungeonAreasAtChunk.remove(dungeonArea);

            if (dungeonAreasAtChunk.isEmpty()) dungeonAreaMap.remove(chunkKey);
        }
    }

    /* =========================================================
     * Map Manager
     * ========================================================= */

    public void defineAllAreasAndActions() {
        defineAllAreas(areaControllerBuilder.getDungeonAreas());
        defineAllBlockFunctions(areaControllerBuilder.getDungeonBlockActions());
        defineAllTriggerCommandFunctions(areaControllerBuilder.getDungeonTriggerCommandActions());
    }

    private void defineAllAreas(Set<DungeonArea> dungeonAreas) {

        for (DungeonArea dungeonArea : dungeonAreas) {
            defineArea(dungeonArea);
        }
    }

    private void defineArea(DungeonArea dungeonArea) {

        Set<Long> allChunkKeysCovered = dungeonArea.getEntryRegion().getCoveredChunkKeys();

        for (Long key : allChunkKeysCovered) {
            dungeonAreaMap.putIfAbsent(key, new HashSet<>());
            Set<DungeonArea> existingSetInAChunk = dungeonAreaMap.get(key);
            existingSetInAChunk.add(dungeonArea);
        }
    }

    public void defineAllBlockFunctions(Set<DungeonAction<Position>> customBlockActions) {
        for (DungeonAction<Position> dungeonAction : customBlockActions) {
            customBlocksMap.put(dungeonAction.getK(), dungeonAction.getAction());
        }
    }

    public void defineAllTriggerCommandFunctions(Set<DungeonAction<Integer>> dungeonTriggerActions) {
        for (DungeonAction<Integer> dungeonAction : dungeonTriggerActions) {
            dungeonTriggerCommandMap.put(dungeonAction.getK(), dungeonAction.getAction());
        }
    }
}