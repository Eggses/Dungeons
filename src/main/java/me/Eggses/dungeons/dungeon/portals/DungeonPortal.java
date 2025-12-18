package me.Eggses.dungeons.dungeon.portals;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.WorldRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class DungeonPortal {

    private final World worldWithPortal;

    private final Region entryPortalRegion;
    private final Position dungeonSpawnPosition;

    private final Region exitPortalRegion;
    private final Position worldExitLocation;

    private final int openDurationSeconds;

    private final Runnable onOpen;
    private final Runnable onClose;

    public DungeonPortal(String worldWithPortal,
                         Region entryPortalRegion,
                         Position dungeonSpawnPosition,
                         Region exitPortalRegion,
                         Position worldExitLocation,
                         int openDurationSeconds,
                         Runnable onOpen,
                         Runnable onClose) {

        this.entryPortalRegion = entryPortalRegion;
        this.dungeonSpawnPosition = dungeonSpawnPosition;
        this.exitPortalRegion = exitPortalRegion;
        this.worldExitLocation = worldExitLocation;
        this.openDurationSeconds = openDurationSeconds;
        this.onOpen = onOpen;
        this.onClose = onClose;

        World world = null;
        if (worldWithPortal != null) {
            world = Bukkit.getWorld(worldWithPortal);
        }
        if (world == null) {
            world = Bukkit.getWorlds().getFirst();
        }
        this.worldWithPortal = world;
    }

    // Main World -> Dungeon
    public WorldRegion getEntryPortalWorldRegion() {
        return new WorldRegion(worldWithPortal, entryPortalRegion);
    }

    public Position getDungeonSpawnPosition() {
        return dungeonSpawnPosition;
    }

    // Dungeon -> Main World
    public Region getExitPortalRegion() {
        return exitPortalRegion;
    }

    public Location getWorldExitLocation() {
        return worldExitLocation.toLocation(worldWithPortal);
    }

    public void openPortal() {
        if (onOpen != null) onOpen.run();
    }

    public void closePortal() {
        if (onClose != null) onClose.run();
    }

    public int getOpenDurationTicks() {
        return openDurationSeconds * 20;
    }
}