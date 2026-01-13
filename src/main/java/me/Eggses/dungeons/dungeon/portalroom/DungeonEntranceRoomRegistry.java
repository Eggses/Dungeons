package me.Eggses.dungeons.dungeon.portalroom;

import me.Eggses.dungeons.dispatch.ChunkMappingRegistry;
import me.Eggses.dungeons.dungeon.regions.WorldRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;

public class DungeonEntranceRoomRegistry {

    private final ChunkMappingRegistry<WorldRegion> dungeonPortalRoomChunkMappingRegistry = new ChunkMappingRegistry<>();

    public void addPortalRoom(WorldRegion dungeonPortalBounds) {
        dungeonPortalRoomChunkMappingRegistry.add(dungeonPortalBounds, dungeonPortalBounds.getRegion().getCoveredChunkKeys());
    }

    public void remove(WorldRegion dungeonPortalBounds) {
        dungeonPortalRoomChunkMappingRegistry.remove(dungeonPortalBounds, dungeonPortalBounds.getRegion().getCoveredChunkKeys());
    }

    public boolean isInPortalRoom(Player player) {
        Location location = player.getLocation();
        Set<WorldRegion> dungeonPortalRoomSet = dungeonPortalRoomChunkMappingRegistry.get(location.getChunk().getChunkKey());
        if (dungeonPortalRoomSet == null) return false;

        for (WorldRegion portalRoom : dungeonPortalRoomSet) {
            if (portalRoom.within(location)) return true;
        }
        return false;
    }
}