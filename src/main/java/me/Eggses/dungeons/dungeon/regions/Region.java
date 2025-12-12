package me.Eggses.dungeons.dungeon.regions;

import org.bukkit.Location;

public class Region {

    private final int minX;
    private final int maxX;

    private final int minY;
    private final int maxY;

    private final int minZ;
    private final int maxZ;

    public Region(Position positionA, Position positionB) {

        int ax = positionA.getX();
        int ay = positionA.getY();
        int az = positionA.getZ();

        int bx = positionB.getX();
        int by = positionB.getY();
        int bz = positionB.getZ();

        this.minX = Math.min(ax, bx);
        this.maxX = Math.max(ax, bx);

        this.minY = Math.min(ay, by);
        this.maxY = Math.max(ay, by);

        this.minZ = Math.min(az, bz);
        this.maxZ = Math.max(az, bz);
    }

    public boolean within(Position position) {

        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        return x <= maxX && x >= minX
                && y <= maxY && y >= minY
                && z <= maxZ && z >= minZ;
    }

    public boolean within(Location location) {
        return within(new Position(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }
}