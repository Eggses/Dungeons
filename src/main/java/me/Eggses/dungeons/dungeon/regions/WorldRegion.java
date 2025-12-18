package me.Eggses.dungeons.dungeon.regions;

import org.bukkit.Location;
import org.bukkit.World;

public class WorldRegion {

    private final Region region;
    private final World world;

    public WorldRegion(Location locationA, Location locationB) {
        this(locationA.getWorld(), new Region(new Position(locationA), new Position(locationB)));
    }

    public WorldRegion(World world, Region region) {
        this.world = world;
        this.region = region;
    }

    public boolean within(Location location) {
        if (!location.getWorld().equals(world)) return false;
        return region.within(location);
    }

    public Region getRegion() {
        return region;
    }

    public World getWorld() {
        return world;
    }
}