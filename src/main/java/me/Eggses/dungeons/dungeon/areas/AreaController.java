package me.Eggses.dungeons.dungeon.areas;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.dispatch.ChunkMappingRegistry;
import me.Eggses.dungeons.dungeon.events.DungeonInteraction;
import me.Eggses.dungeons.dungeon.graveyard.Graveyard;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.areas.utility.DungeonArea;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class AreaController {

    private final EntityManager entityManager;
    private final Graveyard graveyard;
    private final World dungeonWorld;
    private final BlockRegistry blockRegistry;
    private final ChunkMappingRegistry<DungeonArea> dungeonAreaChunkMappingRegistry;
    private final Map<Position, Consumer<DungeonContext>> dungeonTriggerCommandMap;

    private DungeonArea dungeonAreaInProgress;
    private boolean areaInProgress = false;

    private final DungeonContext dungeonContext;

    public AreaController(EntityManager entityManager,
                          Graveyard graveyard,
                          World dungeonWorld,
                          BlockRegistry blockRegistry,
                          AreaControllerBuilder areaControllerBuilder) {

        this.entityManager = entityManager;
        this.graveyard = graveyard;
        this.dungeonWorld = dungeonWorld;
        this.blockRegistry = blockRegistry;
        this.dungeonAreaChunkMappingRegistry = areaControllerBuilder.getDungeonAreaChunkMapping();
        this.dungeonTriggerCommandMap = areaControllerBuilder.getDungeonTriggerCommandMap();
        this.dungeonContext = new DungeonContext(dungeonWorld, entityManager, graveyard, dungeonWorld::getPlayers);

        addAllCustomBlocks(areaControllerBuilder.getBlockInteractionMap());
    }

    private void addAllCustomBlocks(Map<Position, Consumer<DungeonContext>> blockInteractionMap) {

        for (Map.Entry<Position, Consumer<DungeonContext>> entry : blockInteractionMap.entrySet()) {
            Location location = entry.getKey().toLocation(dungeonWorld);

            blockRegistry.addBlockAndEvent(location, PlayerInteractEvent.class, new DungeonInteraction(
                    blockRegistry,
                    entry.getValue(),
                    dungeonContext,
                    () -> !areaInProgress
            ));
        }
    }

    public void handlePlayerMovement(Location to) {

        if (areaInProgress) return;

        Set<DungeonArea> dungeonAreasAtChunk = dungeonAreaChunkMappingRegistry.get(to.getChunk().getChunkKey());
        if (dungeonAreasAtChunk == null) return;

        for (DungeonArea dungeonArea : dungeonAreasAtChunk) {
            if (dungeonArea.getEntryRegion().within(to)) {
                startDungeonArea(dungeonArea);
                return;
            }
        }
    }

    public void handleDungeonTriggerCommand(Position positionOfBlock) {
        Consumer<DungeonContext> consumer = dungeonTriggerCommandMap.remove(positionOfBlock);
        if (consumer == null) return;
        consumer.accept(dungeonContext);
    }

    public void handleEntityDeath(UUID uuid) {
        entityManager.removeMob(uuid);
        tryEndActiveDungeonArea();
    }

    public Location getGraveyardRespawnLocation() {
        return graveyard.getActiveGraveyardLocation(dungeonWorld);
    }

    private void startDungeonArea(DungeonArea dungeonArea) {
        dungeonAreaInProgress = dungeonArea;
        areaInProgress = true;

        dungeonArea.onEnterFirstTime(dungeonContext);
        dungeonAreaChunkMappingRegistry.remove(dungeonArea, dungeonArea.getEntryRegion().getCoveredChunkKeys());
    }

    private void tryEndActiveDungeonArea() {
        if (!entityManager.isEmpty() || !areaInProgress) return;

        dungeonAreaInProgress.onClearArea(dungeonContext);
        dungeonAreaInProgress = null;
        areaInProgress = false;
    }

    public void endAllTasks() {
        entityManager.removeAll();
    }
}