package me.Eggses.dungeons.dungeon.instance;

import me.Eggses.dungeons.dungeon.areas.AreaController;
import me.Eggses.dungeons.dungeon.bosses.BossArenaController;
import me.Eggses.dungeons.entities.mobs.EntityManager;
import me.Eggses.dungeons.dungeon.events.core.EntityDamageEntity;
import me.Eggses.dungeons.dungeon.portals.PortalController;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.eventhandler.EventManager;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class InstanceEventHandler {

    private final EventManager eventManager = new EventManager();

    private final DungeonInstance dungeonInstance;
    private final AreaController areaController;
    private final EntityManager entityManager;
    private final BossArenaController bossArenaController;

    public InstanceEventHandler(DungeonInstance dungeonInstance,
                                AreaController areaController,
                                EntityManager entityManager,
                                BossArenaController bossArenaController) {

        this.dungeonInstance = dungeonInstance;
        this.areaController = areaController;
        this.entityManager = entityManager;
        this.bossArenaController = bossArenaController;

        registerEvents();
    }

    private void registerEvents() {

        eventManager.addEventBehaviour(PlayerMoveEvent.class, (event, eventContext) -> {

            Location to = event.getTo();

            PortalController portalController = dungeonInstance.getPortalController();
            if (portalController.isInPortalInDungeonWorld(to)) {
                portalController.leaveDungeon(event.getPlayer());
                return;
            }
            areaController.handlePlayerMovement(to);
            bossArenaController.handlePlayerMovement(event.getPlayer(), to);
        });

        eventManager.addEventBehaviour(ExplosionPrimeEvent.class, (event, eventContext)
                -> entityManager.passEventToMobIfExists(event.getEntity(), event, eventContext));

        eventManager.addEventBehaviour(PlayerRespawnEvent.class, (event, eventContext)
                -> event.setRespawnLocation(areaController.getGraveyardRespawnLocation()));

        eventManager.addEventBehaviour(PlayerQuitEvent.class, (event, eventContext) -> {
            dungeonInstance.removePlayer(event.getPlayer());
            bossArenaController.exitBossFight(event.getPlayer());
        });

        eventManager.addEventBehaviour(PlayerDeathEvent.class, ((event, eventContext)
                -> bossArenaController.enterBossFight(event.getPlayer())
        ));

        eventManager.addEventBehaviour(EntityRemoveEvent.class, (event, eventContext)
                -> areaController.handleEntityRemove(event.getEntity().getUniqueId()));

        eventManager.addEventBehaviour(EntityDamageByEntityEvent.class, new EntityDamageEntity(entityManager));
    }

    public <E extends Event> void addEventBehaviour(Class<E> eventClass, EventBehaviour<E> eventBehaviour) {
        eventManager.addEventBehaviour(eventClass, eventBehaviour);
    }

    public <E extends Event> void handleEvent(E event) {
        eventManager.handleEvent(event, EventContext.EMPTY);
    }
}
