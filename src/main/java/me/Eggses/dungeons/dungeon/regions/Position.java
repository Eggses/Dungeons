package me.Eggses.dungeons.dungeon.regions;

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

    public Location toLocationCenterBlock(World world) {
        return new Location(world, x + 0.5, y, z + 0.5);
    }
}