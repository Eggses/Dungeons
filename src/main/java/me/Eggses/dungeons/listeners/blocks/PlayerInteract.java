package me.Eggses.dungeons.listeners.blocks;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    private final BlockRegistry blockRegistry;

    public PlayerInteract(BlockRegistry blockRegistry) {
        this.blockRegistry = blockRegistry;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Block block = event.getClickedBlock();
        if (block == null) return;

        blockRegistry.handleEvent(block.getLocation(), event, EventContext.EMPTY);
    }
}