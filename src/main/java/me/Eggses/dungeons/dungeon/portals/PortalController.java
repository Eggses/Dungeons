package me.Eggses.dungeons.dungeon.portals;

import me.Eggses.dungeons.dispatch.ChunkMappingRegistry;
import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Set;

public class PortalController {

    private final JavaPlugin plugin;
    private final DungeonInstance dungeonInstance;
    private final DungeonPortal dungeonPortal;
    private final BannedItems bannedItems;

    private final ChunkMappingRegistry<Region> exitPortals = new ChunkMappingRegistry<>();

    private final Set<Long> chunkKeysOfEntry;
    private boolean isOpen = false;

    public PortalController(JavaPlugin plugin,
                            DungeonInstance dungeonInstance,
                            DungeonPortal dungeonPortal,
                            BannedItems bannedItems) {

        this.plugin = plugin;
        this.dungeonInstance = dungeonInstance;
        this.dungeonPortal = dungeonPortal;
        this.bannedItems = bannedItems;

        this.chunkKeysOfEntry = dungeonPortal.getEntryPortalWorldRegion().getRegion().getCoveredChunkKeys();

        Region exitPortalRegion = dungeonPortal.getExitPortalRegion();
        this.exitPortals.add(exitPortalRegion, exitPortalRegion.getCoveredChunkKeys());
    }

    public void openDungeonPortal() {
        dungeonPortal.openPortal();
        isOpen = true;

        Bukkit.getScheduler().runTaskLater(plugin, dungeonInstance::closeDungeonPortal, dungeonPortal.getOpenDurationTicks());
    }

    public void closeDungeonPortal() {
        dungeonPortal.closePortal();
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isInPortalOutsideDungeon(Location location) {
        return dungeonPortal.getEntryPortalWorldRegion().within(location);
    }

    public void enterDungeon(Player player) {

        if (bannedItems.hasBannedItems(player)) {
            bannedItems.createAndSendBannedItemsMessage(player);
            return;
        }

        Location spawningLocation = dungeonPortal.getDungeonSpawnPosition().toLocation(dungeonInstance.getDungeonWorld());
        player.teleport(spawningLocation);
    }

    public boolean isInPortalInDungeonWorld(Location location) {

        Set<Region> exitPortalsAtChunk = exitPortals.get(location.getChunk().getChunkKey());
        for (Region region : exitPortalsAtChunk) {
            if (region.within(location)) return true;
        }
        return false;
    }

    public void addDungeonExitPortalRegion(Region region) {
        exitPortals.add(region, region.getCoveredChunkKeys());
    }

    public void leaveDungeon(Player player) {
        Location spawningLocation = dungeonPortal.getWorldExitLocation();
        player.teleport(spawningLocation);
    }

    public Set<Long> getChunkKeysEncompassedOfEntryPortal() {
        return chunkKeysOfEntry;
    }
}