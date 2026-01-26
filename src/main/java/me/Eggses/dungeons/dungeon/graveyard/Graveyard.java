package me.Eggses.dungeons.dungeon.graveyard;

import me.Eggses.dungeons.dungeon.regions.RotationPosition;
import me.Eggses.dungeons.utility.exceptions.GraveyardNoDefinedLocationException;
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
        if (activeGraveyard == null) throw new GraveyardNoDefinedLocationException();
        return activeGraveyard.toLocation(world);
    }
}