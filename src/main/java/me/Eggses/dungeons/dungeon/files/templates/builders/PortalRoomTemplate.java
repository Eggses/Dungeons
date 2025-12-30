package me.Eggses.dungeons.dungeon.files.templates.builders;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;

@SuppressWarnings("ClassCanBeRecord")
public class PortalRoomTemplate {

    private final Region generalRoomRegion;
    private final Position positionOfKeyStone;
    private final String keystoneName;

    public PortalRoomTemplate(Region generalRoomRegion, Position positionOfKeyStone, String keystoneName) {
        this.generalRoomRegion = generalRoomRegion;
        this.positionOfKeyStone = positionOfKeyStone;
        this.keystoneName = keystoneName;
    }

    public Region getGeneralRoomRegion() {
        return generalRoomRegion;
    }

    public Position getPositionOfKeyStone() {
        return positionOfKeyStone;
    }

    public String getKeystoneName() {
        return keystoneName;
    }
}