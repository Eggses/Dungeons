package me.Eggses.dungeons.dungeon.areas;

import me.Eggses.dungeons.dungeon.files.reading.TriConsumer;
import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.areas.utility.DungeonArea;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AreaController {

    private final EntityManager entityManager;
    private final Graveyard graveyard;
    private final World dungeonWorld;
    private final Map<Long, Set<DungeonArea>> dungeonAreasMap;
    private final Map<Position, TriConsumer<World, EntityManager, Graveyard>> blockInteractionMap;
    private final Map<String, TriConsumer<World, EntityManager, Graveyard>> dungeonTriggerCommandMap;

    private DungeonArea dungeonAreaInProgress;
    private boolean areaInProgress = false;

    public AreaController(EntityManager entityManager,
                          Graveyard graveyard,
                          World dungeonWorld,
                          AreaControllerBuilder areaControllerBuilder) {

        this.entityManager = entityManager;
        this.graveyard = graveyard;
        this.dungeonWorld = dungeonWorld;
        this.dungeonAreasMap = areaControllerBuilder.getDungeonAreasMap();
        this.blockInteractionMap = areaControllerBuilder.getBlockInteractionMap();
        this.dungeonTriggerCommandMap = areaControllerBuilder.getDungeonTriggerCommandMap();
    }

    public void handlePlayerMoveEvent(Location location, long chunkKey) {

        if (areaInProgress) return;

        Set<DungeonArea> dungeonAreasAtChunk = dungeonAreasMap.get(chunkKey);
        if (dungeonAreasAtChunk == null) return;

        for (DungeonArea dungeonArea : dungeonAreasAtChunk) {
            if (dungeonArea.getEntryRegion().within(location)) {
                startDungeonArea(dungeonArea);
                return;
            }
        }
    }

    public void handleInteractEvent(Position positionOfBlock) {
        if (areaInProgress) return;

        TriConsumer<World, EntityManager, Graveyard> triConsumer = blockInteractionMap.remove(positionOfBlock);
        if (triConsumer == null) return;
        triConsumer.accept(dungeonWorld, entityManager, graveyard);
    }

    public void handleDungeonTriggerCommand(String argument) {
        if (areaInProgress) return;

        TriConsumer<World, EntityManager, Graveyard> triConsumer = dungeonTriggerCommandMap.remove(argument);
        if (triConsumer == null) return;
        triConsumer.accept(dungeonWorld, entityManager, graveyard);
    }

    public void handleEntityDeathEvent(UUID uuid) {
        entityManager.removeMob(uuid);
        tryEndActiveDungeonArea();
    }

    public void handlePlayerRespawnEvent(PlayerRespawnEvent event) {
        event.setRespawnLocation(graveyard.getActiveGraveyardLocation(dungeonWorld));
    }

    private void startDungeonArea(DungeonArea dungeonArea) {
        dungeonAreaInProgress = dungeonArea;
        areaInProgress = true;

        dungeonArea.onEnterFirstTime(dungeonWorld, entityManager, graveyard);
        removeDungeonAreaFromMap(dungeonArea);
    }

    private void removeDungeonAreaFromMap(DungeonArea dungeonArea) {

        Set<Long> encompassedChunkKeys = dungeonArea.getEntryRegion().getCoveredChunkKeys();

        for (Long chunkKey : encompassedChunkKeys) {

            Set<DungeonArea> dungeonAreasAtChunk = dungeonAreasMap.get(chunkKey);
            if (dungeonAreasAtChunk == null) continue;

            dungeonAreasAtChunk.remove(dungeonArea);
            if (dungeonAreasAtChunk.isEmpty()) dungeonAreasMap.remove(chunkKey);
        }
    }

    private void tryEndActiveDungeonArea() {
        if (!entityManager.isEmpty() || !areaInProgress) return;

        dungeonAreaInProgress.onClearArea(dungeonWorld, entityManager, graveyard);
        dungeonAreaInProgress = null;
        areaInProgress = false;
    }

    public void endAllTasks() {
        entityManager.removeAll();
    }
}