package me.Eggses.dungeons.dungeon.graveyard;

import me.Eggses.dungeons.dungeon.regions.Position;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graveyard {

    private final Map<Integer, Position> graveyards = new HashMap<>();
    private Position activeGraveyard;

    public Graveyard(List<GraveyardDefinition> graveyardDefinitions) {
        for (GraveyardDefinition graveyardDefinition : graveyardDefinitions) {
            graveyards.put(graveyardDefinition.getGraveyardNumber(), graveyardDefinition.getGraveyardLocation());
        }
        setActiveGraveyard(1);
    }

    public void setActiveGraveyard(int graveyardNumber) {
        Position graveyard = graveyards.get(graveyardNumber);
        if (graveyard == null) return;

        activeGraveyard = graveyard;
    }

    public Location getActiveGraveyardLocation(World world) {
        return activeGraveyard.toLocationCenterBlock(world);
    }
}