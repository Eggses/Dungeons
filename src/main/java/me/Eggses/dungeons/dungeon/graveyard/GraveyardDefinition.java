package me.Eggses.dungeons.dungeon.graveyard;

import me.Eggses.dungeons.dungeon.regions.Position;

@SuppressWarnings("ClassCanBeRecord")
public class GraveyardDefinition {

    private final Integer graveyardNumber;
    private final Position graveyardLocation;

    public GraveyardDefinition(Integer graveyardNumber, Position graveyardLocation) {
        this.graveyardNumber = graveyardNumber;
        this.graveyardLocation = graveyardLocation;
    }

    public Integer getGraveyardNumber() {
        return graveyardNumber;
    }

    public Position getGraveyardLocation() {
        return graveyardLocation;
    }
}
