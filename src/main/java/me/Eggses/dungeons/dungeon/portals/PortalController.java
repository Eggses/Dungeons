package me.Eggses.dungeons.dungeon.portals;

import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.DungeonInstance;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PortalController {

    private final JavaPlugin plugin;
    private final DungeonInstance dungeonInstance;
    private final DungeonPortal dungeonPortal;
    private final BannedItems bannedItems;

    private boolean isOpen = false;

    public PortalController(JavaPlugin plugin,
                            DungeonInstance dungeonInstance,
                            DungeonPortal dungeonPortal,
                            BannedItems bannedItems) {

        this.plugin = plugin;
        this.dungeonInstance = dungeonInstance;
        this.dungeonPortal = dungeonPortal;
        this.bannedItems = bannedItems;
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

    public void enterDungeon(Player player, World world) {

        if (bannedItems.hasBannedItems(player)) return;

        Location spawningLocation = dungeonPortal.getSpawningLocation().toLocationCenterBlock(world);
        player.teleport(spawningLocation);
    }

    public boolean isInPortal(@NotNull Player player) {
        return dungeonPortal.getPortalWorldRegion().within(player.getLocation());
    }
}