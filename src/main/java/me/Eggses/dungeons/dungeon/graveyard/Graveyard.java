package me.Eggses.dungeons.dungeon.graveyard;

import me.Eggses.dungeons.dungeon.regions.RotationPosition;
import org.bukkit.Location;
import org.bukkit.World;

public class Graveyard {

    private RotationPosition activeGraveyard;

    public Graveyard() {
    }

    public void setActiveGraveyard(RotationPosition rotationPosition) {
        activeGraveyard = rotationPosition;
    }

    public Location getActiveGraveyardLocation(World world) {
        if (activeGraveyard == null) throw new RuntimeException("Graveyard has no position defined");
        return activeGraveyard.toLocation(world);
    }
}