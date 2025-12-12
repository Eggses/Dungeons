package me.Eggses.dungeons.dungeon.portals;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.WorldRegion;

public interface DungeonPortal {

    int PORTAL_OPEN_DURATION_TICKS = 120 * 20;// Seconds * Ticks = Total Ticks

    WorldRegion getPortalWorldRegion();
    Position getSpawningLocation();

    void openPortal();
    void closePortal();

    default int getOpenDurationTicks() {
        return PORTAL_OPEN_DURATION_TICKS;
    }
}