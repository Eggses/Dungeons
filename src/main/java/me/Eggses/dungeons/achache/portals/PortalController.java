package me.Eggses.dungeons.achache.portals;

import me.Eggses.dungeons.achache.BannedItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PortalController {

    private final JavaPlugin plugin;
    private final DungeonPortal dungeonPortal;
    private final World dungeonWorld;
    private final BannedItems bannedItems;

    private boolean isOpen = false;

    public PortalController(JavaPlugin plugin,
                            DungeonPortal dungeonPortal,
                            World dungeonWorld,
                            BannedItems bannedItems) {

        this.plugin = plugin;
        this.dungeonPortal = dungeonPortal;
        this.dungeonWorld = dungeonWorld;
        this.bannedItems = bannedItems;
    }

    public void openDungeonPortal() {
        dungeonPortal.openPortal();
        isOpen = true;

        Bukkit.getScheduler().runTaskLater(plugin, this::closeDungeonPortal, dungeonPortal.getOpenDurationTicks());
    }

    public void closeDungeonPortal() {
        dungeonPortal.closePortal();
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void onEntry(Player player) {

        if (bannedItems.hasBannedItems(player)) return;

        Location spawningLocation = dungeonPortal.getSpawningLocation().toLocation(dungeonWorld);
        player.teleport(spawningLocation);
    }

    public boolean isInRegion(@NotNull Player player) {
        return dungeonPortal.getPortalWorldRegion().within(player.getLocation());
    }
}