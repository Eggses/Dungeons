package me.Eggses.dungeons.dungeon.portals;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.WorldRegion;
import org.bukkit.Location;

public interface DungeonPortal {

    int PORTAL_OPEN_DURATION_TICKS = 120 * 20; // Seconds * Ticks = Total Ticks

    // Enter Portal -> Spawn In Dungeon
    WorldRegion getInWorldPortalWorldRegion();
    Position getSpawningLocationInsideDungeon();

    // Exit Portal -> Spawn in Main World.
    Region getPortalInDungeonRegion();
    Location getExitLocationInMainWorld();

    void openPortal();
    void closePortal();

    default int getOpenDurationTicks() {
        return PORTAL_OPEN_DURATION_TICKS;
    }
}