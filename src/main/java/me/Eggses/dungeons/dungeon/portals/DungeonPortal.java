package me.Eggses.dungeons.dungeon.portals;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.WorldRegion;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.function.Consumer;

public class DungeonPortal {

    private final World worldWithPortal;

    private final Region entryPortalRegion;
    private final Position dungeonSpawnPosition;

    private final Region exitPortalRegion;
    private final Position worldExitLocation;

    private final int openDurationSeconds;

    private final Consumer<DungeonContext> onOpen;
    private final Consumer<DungeonContext> onClose;

    public DungeonPortal(String worldWithPortal,
                         Region entryPortalRegion,
                         Position dungeonSpawnPosition,
                         Region exitPortalRegion,
                         Position worldExitLocation,
                         int openDurationSeconds,
                         Consumer<DungeonContext> onOpen,
                         Consumer<DungeonContext> onClose) {

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
        if (onOpen != null) onOpen.accept(new DungeonContext());
    }

    public void closePortal() {
        if (onClose != null) onClose.accept(new DungeonContext());
    }

    public int getOpenDurationTicks() {
        return openDurationSeconds * 20;
    }
}