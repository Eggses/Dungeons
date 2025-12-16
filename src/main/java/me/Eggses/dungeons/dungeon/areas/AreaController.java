package me.Eggses.dungeons.dungeon.areas;

import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import me.Eggses.dungeons.dungeon.graveyard.GraveyardDefinition;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.areas.utility.DungeonArea;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class AreaController {

    private final EntityManager entityManager;
    private final Graveyard graveyard;
    private final World dungeonWorld;
    private final Map<Long, Set<DungeonArea>> dungeonAreasMap;
    private final Map<Position, Consumer<World>> blockInteractionMap;
    private final Map<Integer, Consumer<World>> dungeonTriggerCommandMap;

    private DungeonArea dungeonAreaInProgress;
    private boolean areaInProgress = false;

    public AreaController(EntityManager entityManager,
                          World dungeonWorld,
                          AreaControllerBuilder areaControllerBuilder,
                          List<GraveyardDefinition> graveyardDefinitions) {

        this.entityManager = entityManager;
        this.graveyard = new Graveyard(graveyardDefinitions);
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

        Consumer<World> consumer = blockInteractionMap.get(positionOfBlock);
        if (consumer == null) return;
        consumer.accept(dungeonWorld);
    }

    public void handleDungeonTriggerCommand(Integer argumentValue) {
        if (areaInProgress) return;

        Consumer<World> consumer = dungeonTriggerCommandMap.get(argumentValue);
        if (consumer == null) return;
        consumer.accept(dungeonWorld);
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

        dungeonArea.onEnterFirstTime(dungeonWorld, entityManager);
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

        dungeonAreaInProgress.onClearArea(dungeonWorld, graveyard);
        dungeonAreaInProgress = null;
        areaInProgress = false;
    }

    public void endAllTasks() {
        entityManager.removeAll();
    }
}