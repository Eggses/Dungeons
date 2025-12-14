package me.Eggses.dungeons.dungeon.regions;

import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

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

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return x <= maxX && x >= minX
                && y <= maxY && y >= minY
                && z <= maxZ && z >= minZ;
    }

    public Set<Long> getCoveredChunkKeys() {

        Set<Long> chunkKeys = new HashSet<>();

        int minChunkX = minX >> 4;
        int maxChunkX = maxX >> 4;

        int minChunkZ = minZ >> 4;
        int maxChunkZ = maxZ >> 4;

        for (int x = minChunkX; x <= maxChunkX; x++) {
            for (int z = minChunkZ; z <= maxChunkZ; z++) {
                chunkKeys.add(Chunk.getChunkKey(x, z));
            }
        }

        return chunkKeys;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Region other)) return false;

        return minX == other.minX
                && maxX == other.maxX
                && minY == other.minY
                && maxY == other.maxY
                && minZ == other.minZ
                && maxZ == other.maxZ;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + minX;
        result = 31 * result + maxX;
        result = 31 * result + minY;
        result = 31 * result + maxY;
        result = 31 * result + minZ;
        result = 31 * result + maxZ;
        return result;
    }
}