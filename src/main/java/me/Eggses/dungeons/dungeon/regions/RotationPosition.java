package me.Eggses.dungeons.dungeon.regions;

import org.bukkit.Location;
import org.bukkit.World;

public class RotationPosition {

    private final Position position;
    private final float rotation;

    public RotationPosition(Position position, float rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public RotationPosition(Position position) {
        this.position = position;
        this.rotation = 0.0f;
    }

    public Location toLocation(World world) {
        Location location = position.toLocationCenterBlock(world);
        location.setYaw(rotation);
        location.setPitch(0.0f);
        return location;
    }

    public Position getPosition() {
        return position;
    }
}