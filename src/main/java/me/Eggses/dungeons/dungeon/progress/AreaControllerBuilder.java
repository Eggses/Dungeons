package me.Eggses.dungeons.dungeon.progress;

import me.Eggses.dungeons.dungeon.regions.Position;

import java.util.HashSet;
import java.util.Set;

public class AreaControllerBuilder {

    private final Set<DungeonArea> dungeonAreas = new HashSet<>();
    private final Set<DungeonAction<Position>> dungeonBlockActions = new HashSet<>();
    private final Set<DungeonAction<Integer>> dungeonTriggerCommandActions = new HashSet<>();

    public AreaControllerBuilder() {

    }

    public AreaControllerBuilder addDungeonArea(DungeonArea dungeonArea) {
        dungeonAreas.add(dungeonArea);
        return this;
    }

    public AreaControllerBuilder addBlockAction(DungeonAction<Position> blockAction) {
        dungeonBlockActions.add(blockAction);
        return this;
    }

    public AreaControllerBuilder addDungeonTriggerAction(DungeonAction<Integer> dungeonTriggerAction) {
        dungeonTriggerCommandActions.add(dungeonTriggerAction);
        return this;
    }

    public Set<DungeonArea> getDungeonAreas() {
        return dungeonAreas;
    }

    public Set<DungeonAction<Position>> getDungeonBlockActions() {
        return dungeonBlockActions;
    }

    public Set<DungeonAction<Integer>> getDungeonTriggerCommandActions() {
        return dungeonTriggerCommandActions;
    }
}