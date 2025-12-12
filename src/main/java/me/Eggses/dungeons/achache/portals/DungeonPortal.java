package me.Eggses.dungeons.achache.portals;

public interface DungeonPortal {

    int PORTAL_OPEN_DURATION_TICKS = 120 * 20;// Seconds * Ticks = Total Ticks

    WorldRegion getPortalWorldRegion();
    Position getSpawningLocation();

    void openPortal();
    void closePortal();

    int getOpenDurationTicks();
}