package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dungeon.DungeonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class DurabilityLoss implements Listener {

    private final DungeonManager dungeonManager;

    public DurabilityLoss(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onDurabilityLoss(PlayerItemDamageEvent event) {

        if (dungeonManager.isInDungeon(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}