package me.Eggses.dungeons.achache.portals;

import org.bukkit.Location;
import org.bukkit.World;

public class Position {

    private final int x;
    private final int y;
    private final int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(Location location) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getY() {
        return y;
    }

    public Location toLocation(World world) {
        return new Location(world, x, y, z);
    }
}