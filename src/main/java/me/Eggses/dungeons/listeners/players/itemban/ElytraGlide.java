package me.Eggses.dungeons.listeners.players.itemban;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class ElytraGlide implements Listener {

    private final DungeonManager dungeonManager;

    public ElytraGlide(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onGlide(EntityToggleGlideEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;
        if (!dungeonManager.isInDungeon(player)) return;

        if (event.isGliding()) {
            event.setCancelled(true);
        }
    }
}
