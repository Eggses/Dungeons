package me.Eggses.dungeons.dungeon.portals;

import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.RotationPosition;
import me.Eggses.dungeons.dungeon.regions.WorldRegion;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.function.Consumer;

public class DungeonPortal {

    private final World worldWithPortal;

    private final Region entryPortalRegion;
    private final RotationPosition dungeonSpawnPosition;

    private final Region exitPortalRegion;
    private final RotationPosition worldExitLocation;

    private final int openDurationSeconds;

    private final Consumer<DungeonContext> onOpen;
    private final Consumer<DungeonContext> onClose;

    private static final DungeonContext CONTEXT = DungeonContext.builder()
            .players(Bukkit::getOnlinePlayers)
            .build();

    public DungeonPortal(World worldWithPortal,
                         Region entryPortalRegion,
                         RotationPosition dungeonSpawnPosition,
                         Region exitPortalRegion,
                         RotationPosition worldExitLocation,
                         int openDurationSeconds,
                         Consumer<DungeonContext> onOpen,
                         Consumer<DungeonContext> onClose) {

        this.worldWithPortal = worldWithPortal;
        this.entryPortalRegion = entryPortalRegion;
        this.dungeonSpawnPosition = dungeonSpawnPosition;
        this.exitPortalRegion = exitPortalRegion;
        this.worldExitLocation = worldExitLocation;
        this.openDurationSeconds = openDurationSeconds;
        this.onOpen = onOpen;
        this.onClose = onClose;
    }


    // Main World -> Dungeon
    public WorldRegion getEntryPortalWorldRegion() {
        return new WorldRegion(worldWithPortal, entryPortalRegion);
    }

    public RotationPosition getDungeonSpawnPosition() {
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
        if (onOpen != null) onOpen.accept(CONTEXT);
    }

    public void closePortal() {
        if (onClose != null) onClose.accept(CONTEXT);
    }

    public int getOpenDurationTicks() {
        return openDurationSeconds * 20;
    }
}