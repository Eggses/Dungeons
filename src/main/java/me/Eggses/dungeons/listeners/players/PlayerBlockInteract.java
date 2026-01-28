package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.dungeon.regions.WorldPosition;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerBlockInteract implements Listener {

    private final BlockRegistry blockRegistry;

    public PlayerBlockInteract(BlockRegistry blockRegistry) {
        this.blockRegistry = blockRegistry;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) return;

        blockRegistry.handleEvent(new WorldPosition(block), event, EventContext.EMPTY);
    }
}
