package me.Eggses.dungeons.dungeon.files.templates.builders;

import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.RotationPosition;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class PortalTemplate {

    private final Region entryPortalRegion;
    private final RotationPosition dungeonSpawnPosition;

    private final Region exitPortalRegion;
    private final RotationPosition worldExitLocation;

    private final int openDurationSeconds;

    private final List<String> onOpenCommands;
    private final List<String> onCloseCommands;

    public PortalTemplate(Region entryPortalRegion,
                          RotationPosition dungeonSpawnPosition,
                          Region exitPortalRegion,
                          RotationPosition worldExitLocation,
                          int openDurationSeconds,
                          List<String> onOpenCommands,
                          List<String> onCloseCommands) {

        this.entryPortalRegion = entryPortalRegion;
        this.dungeonSpawnPosition = dungeonSpawnPosition;
        this.exitPortalRegion = exitPortalRegion;
        this.worldExitLocation = worldExitLocation;
        this.openDurationSeconds = openDurationSeconds;
        this.onOpenCommands = onOpenCommands;
        this.onCloseCommands = onCloseCommands;
    }

    public Region getEntryPortalRegion() {
        return entryPortalRegion;
    }

    public RotationPosition getDungeonSpawnPosition() {
        return dungeonSpawnPosition;
    }

    public Region getExitPortalRegion() {
        return exitPortalRegion;
    }

    public RotationPosition getWorldExitLocation() {
        return worldExitLocation;
    }

    public int getOpenDurationSeconds() {
        return openDurationSeconds;
    }

    public List<String> getOnOpenCommands() {
        return onOpenCommands;
    }

    public List<String> getOnCloseCommands() {
        return onCloseCommands;
    }
}