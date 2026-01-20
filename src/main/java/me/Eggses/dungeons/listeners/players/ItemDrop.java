package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dungeon.portalroom.DungeonEntranceRoomRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDrop implements Listener {

    private final DungeonEntranceRoomRegistry dungeonEntranceRoomRegistry;

    public ItemDrop(DungeonEntranceRoomRegistry dungeonEntranceRoomRegistry) {
        this.dungeonEntranceRoomRegistry = dungeonEntranceRoomRegistry;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (dungeonEntranceRoomRegistry.isInPortalRoom(event.getPlayer())) {
            event.setCancelled(true);
            System.out.println("Drop and cancled");
            return;
        }
        System.out.println("Drop and not canceld");
    }
}
