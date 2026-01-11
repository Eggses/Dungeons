package me.Eggses.dungeons.listeners.bans;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class DurabilityLoss implements Listener {

    private static final double CHANCE_TO_AVOID_DURABILITY_DAMAGE = 0.5;

    private final DungeonRegistry dungeonRegistry;

    public DurabilityLoss(DungeonRegistry dungeonRegistry) {
        this.dungeonRegistry = dungeonRegistry;
    }

    @EventHandler
    public void onDurabilityLoss(PlayerItemDamageEvent event) {

        if (!dungeonRegistry.isInDungeon(event.getPlayer())) return;

        if (Math.random() < CHANCE_TO_AVOID_DURABILITY_DAMAGE) event.setCancelled(true);
    }
}