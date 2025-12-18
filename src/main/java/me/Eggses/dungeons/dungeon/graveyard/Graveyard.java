package me.Eggses.dungeons.dungeon.graveyard;

import me.Eggses.dungeons.dungeon.regions.Position;
import org.bukkit.Location;
import org.bukkit.World;

public class Graveyard {

    private Position activeGraveyard;

    public void setActiveGraveyard(Position position) {
        activeGraveyard = position;
    }

    public Location getActiveGraveyardLocation(World world) {
        return activeGraveyard.toLocationCenterBlock(world);
    }
}