package me.Eggses.dungeons.listeners.players.itemban;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class DurabilityLoss implements Listener {

    private static final double CHANCE_TO_AVOID_DURABILITY_DAMAGE = 0.5;

    private final DungeonManager dungeonManager;

    public DurabilityLoss(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onDurabilityLoss(PlayerItemDamageEvent event) {

        if (!dungeonManager.isInDungeon(event.getPlayer())) return;

        if (Math.random() < CHANCE_TO_AVOID_DURABILITY_DAMAGE) event.setCancelled(true);
    }
}