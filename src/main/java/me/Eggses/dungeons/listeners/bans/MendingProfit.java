package me.Eggses.dungeons.listeners.bans;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemMendEvent;

public class MendingProfit implements Listener {

    private static final double MENDING_EFFECTIVENESS = 0.8;

    private final DungeonRegistry dungeonRegistry;

    public MendingProfit(DungeonRegistry dungeonRegistry) {
        this.dungeonRegistry = dungeonRegistry;
    }

    @EventHandler
    public void handleMendingGain(PlayerItemMendEvent event) {

        if (!dungeonRegistry.isInDungeon(event.getPlayer())) return;

        int originalRepairAmount = event.getRepairAmount();
        int finalRepairAmount = (int) (originalRepairAmount * MENDING_EFFECTIVENESS);

        if (finalRepairAmount == 0) {
            event.setCancelled(true);
            return;
        }

        event.setRepairAmount(finalRepairAmount);
    }
}