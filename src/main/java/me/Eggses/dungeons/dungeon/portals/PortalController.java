package me.Eggses.dungeons.dungeon.portals;

import me.Eggses.dungeons.dungeon.DungeonInstance;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PortalController {

    private final JavaPlugin plugin;
    private final DungeonInstance dungeonInstance;
    private final DungeonPortal dungeonPortal;

    private final Set<Long> chunkKeys;
    private boolean isOpen = false;

    public PortalController(JavaPlugin plugin,
                            DungeonInstance dungeonInstance,
                            DungeonPortal dungeonPortal) {

        this.plugin = plugin;
        this.dungeonInstance = dungeonInstance;
        this.dungeonPortal = dungeonPortal;
        chunkKeys = dungeonPortal.getInWorldPortalWorldRegion().getRegion().getCoveredChunkKeys();
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

    public void enterDungeon(Player player, World dungeonWorld) {
        Location spawningLocation = dungeonPortal.getSpawningLocationInsideDungeon().toLocationCenterBlock(dungeonWorld);
        player.teleport(spawningLocation);
    }

    public boolean isInPortalInMainWorld(Location location) {
        return dungeonPortal.getInWorldPortalWorldRegion().within(location);
    }

    public void leaveDungeon(Player player) {
        Location spawningLocation = dungeonPortal.getExitLocationInMainWorld();
        player.teleport(spawningLocation);
    }

    public boolean isInPortalInDungeonWorld(Location location) {
        return dungeonPortal.getPortalInDungeonRegion().within(location);
    }

    public Set<Long> getChunkKeysEncompassed() {
        return chunkKeys;
    }
}