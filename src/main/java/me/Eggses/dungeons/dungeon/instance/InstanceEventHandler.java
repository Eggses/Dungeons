package me.Eggses.dungeons.dungeon.instance;

import me.Eggses.dungeons.dungeon.areas.AreaController;
import me.Eggses.dungeons.dungeon.areas.EntityManager;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.eventhandler.EventManager;
import org.bukkit.event.Event;

public class InstanceEventHandler {

    private final EventManager eventManager = new EventManager();

    private final DungeonInstance dungeonInstance;
    private final AreaController areaController;
    private final EntityManager entityManager;

    public InstanceEventHandler(DungeonInstance dungeonInstance,
                                AreaController areaController,
                                EntityManager entityManager) {

        this.dungeonInstance = dungeonInstance;
        this.areaController = areaController;
        this.entityManager = entityManager;
    }

    public <E extends Event> void handleEvent(E event) {
        eventManager.handleEvent(event, EventContext.EMPTY); // TODO: Fix the content here.
    }

    public void handleDungeonTriggerCommand(Position positionOfBlock) {
        areaController.handleDungeonTriggerCommand(positionOfBlock);
    }
}
