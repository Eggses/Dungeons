package me.Eggses.dungeons.dungeon.graveyard;

import me.Eggses.dungeons.dungeon.regions.RotationPosition;
import org.bukkit.Location;
import org.bukkit.World;

public class Graveyard {

    private RotationPosition activeGraveyard;

    public Graveyard(RotationPosition rotationPosition) {
        this.activeGraveyard = rotationPosition;
    }

    public void setActiveGraveyard(RotationPosition rotationPosition) {
        activeGraveyard = rotationPosition;
    }

    public Location getActiveGraveyardLocation(World world) {
        return activeGraveyard.toLocation(world);
    }
}