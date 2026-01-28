package me.Eggses.dungeons.dungeon.regions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class WorldPosition {

    private final World world;
    private final Position position;

    public WorldPosition(Location location) {
        this.world = location.getWorld();
        this.position = new Position(location);
    }

    public WorldPosition(World world, Position position) {
        this.world = world;
        this.position = position;
    }

    public WorldPosition(Block block) {
        this.world = block.getWorld();
        this.position = new Position(block.getX(), block.getY(), block.getZ());
    }

    public World getWorld() {
        return world;
    }

    public Position getPosition() {
        return position;
    }

    public Location toLocation() {
        return new Location(world, position.getX(), position.getY(), position.getZ());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WorldPosition other)) return false;

        if (world == null || other.world == null) return false;

        return world.equals(other.world) && position.equals(other.position);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + world.hashCode();
        result = 31 * result + position.hashCode();
        return result;
    }
}
