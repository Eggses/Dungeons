package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dungeon.portalroom.DungeonPortalRoomRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDrop implements Listener {

    private final DungeonPortalRoomRegistry dungeonPortalRoomRegistry;

    public ItemDrop(DungeonPortalRoomRegistry dungeonPortalRoomRegistry) {
        this.dungeonPortalRoomRegistry = dungeonPortalRoomRegistry;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (dungeonPortalRoomRegistry.isInPortalRoom(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}