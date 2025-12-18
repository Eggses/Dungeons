package me.Eggses.dungeons.dungeon.portals;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Set;

public class PortalController {

    private final JavaPlugin plugin;
    private final DungeonInstance dungeonInstance;
    private final DungeonPortal dungeonPortal;
    private final BannedItems bannedItems;

    private final Set<Long> chunkKeys;
    private boolean isOpen = false;

    public PortalController(JavaPlugin plugin,
                            DungeonInstance dungeonInstance,
                            DungeonPortal dungeonPortal,
                            BannedItems bannedItems) {

        this.plugin = plugin;
        this.dungeonInstance = dungeonInstance;
        this.dungeonPortal = dungeonPortal;
        this.bannedItems = bannedItems;

        this.chunkKeys = dungeonPortal.getEntryPortalWorldRegion().getRegion().getCoveredChunkKeys();
    }

    public void openDungeonPortal() {
        dungeonPortal.openPortal();
        isOpen = true;

        Bukkit.getScheduler().runTaskLater(plugin, dungeonInstance::closeDungeonPortal, dungeonPortal.getOpenDurationTicks());
    }

    public void closeDungeonPortal() {
        if (!isOpen) return;
        dungeonPortal.closePortal();
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isInPortalInMainWorld(Location location) {
        return dungeonPortal.getEntryPortalWorldRegion().within(location);
    }

    public void enterDungeon(Player player, World dungeonWorld) {

        if (bannedItems.hasBannedItems(player)) {
            bannedItems.createAndSendBannedItemsMessage(player);
            return;
        }

        Location spawningLocation = dungeonPortal.getDungeonSpawnPosition().toLocationCenterBlock(dungeonWorld);
        player.teleport(spawningLocation);
    }

    public boolean isInPortalInDungeonWorld(Location location) {
        return dungeonPortal.getExitPortalRegion().within(location);
    }

    public void leaveDungeon(Player player) {
        Location spawningLocation = dungeonPortal.getWorldExitLocation();
        player.teleport(spawningLocation);
    }

    public Set<Long> getChunkKeysEncompassed() {
        return chunkKeys;
    }
}