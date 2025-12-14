package me.Eggses.dungeons.listeners.players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDrop implements Listener {

    private final DungeonManager dungeonManager;

    public ItemDrop(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {

        if (dungeonManager.isInNormalWorldPortalRoom(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
