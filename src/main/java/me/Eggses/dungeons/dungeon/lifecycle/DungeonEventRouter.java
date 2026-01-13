package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.portals.OpenPortalRegistry;
import me.Eggses.dungeons.dungeon.portals.PortalController;
import me.Eggses.dungeons.dungeon.regions.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class DungeonEventRouter {

    private final DungeonRegistry dungeonRegistry;
    private final OpenPortalRegistry openPortalRegistry;

    public DungeonEventRouter(DungeonRegistry dungeonRegistry, OpenPortalRegistry openPortalRegistry) {
        this.dungeonRegistry = dungeonRegistry;
        this.openPortalRegistry = openPortalRegistry;
    }

    public <E extends Event> void handleEvent(E event, World world) {
        DungeonInstance dungeonInstance = dungeonRegistry.getDungeonInstance(world);
        if (dungeonInstance != null) dungeonInstance.handleEvent(event);
    }

    public void handlePlayerMovementEvent(PlayerMoveEvent event) {

        Location to = event.getTo();
        PortalController portalController = openPortalRegistry.getPortalController(to.getChunk().getChunkKey(), to);
        if (portalController != null) {
            portalController.enterDungeon(event.getPlayer());
            return;
        }
        DungeonInstance dungeonInstance = dungeonRegistry.getDungeonInstance(to.getWorld());
        if (dungeonInstance != null) dungeonInstance.handleEvent(event);
    }

    public void handlePlayerChangeWorldEvent(PlayerChangedWorldEvent event) {

        Player player = event.getPlayer();
        World worldEntered = player.getWorld();
        World worldLeft = event.getFrom();

        DungeonInstance dungeonInstanceEntered = dungeonRegistry.getDungeonInstance(worldEntered);
        if (dungeonInstanceEntered != null) {
            dungeonInstanceEntered.addPlayer(player);
            return;
        }

        DungeonInstance dungeonInstanceLeft = dungeonRegistry.getDungeonInstance(worldLeft);
        if (dungeonInstanceLeft != null) {
            dungeonInstanceLeft.removePlayer(player);
        }
    }

    public void handleDungeonTriggerCommand(Location locationOfBlock) {
        DungeonInstance dungeonInstance = dungeonRegistry.getDungeonInstance(locationOfBlock.getWorld());
        if (dungeonInstance != null) dungeonInstance.handleDungeonTriggerCommand(new Position(locationOfBlock));
    }

}
