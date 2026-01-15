package me.Eggses.dungeons.listeners.bans;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CauldronLevelChangeEvent;

public class CauldronLevel implements Listener {

    private final DungeonRegistry dungeonRegistry;

    public CauldronLevel(DungeonRegistry dungeonRegistry) {
        this.dungeonRegistry = dungeonRegistry;
    }

    @EventHandler
    public void onCauldronLevel(CauldronLevelChangeEvent event) {

        if (!dungeonRegistry.isDungeonWorld(event.getBlock().getWorld())) return;
        if (event.getReason() != CauldronLevelChangeEvent.ChangeReason.NATURAL_FILL) return;

        event.setCancelled(true);
    }
}
