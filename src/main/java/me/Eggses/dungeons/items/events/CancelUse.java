package me.Eggses.dungeons.items.events;

import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

public class CancelUse implements EventBehaviour<PlayerInteractEvent> {

    @Override
    public void handleEvent(PlayerInteractEvent event, EventContext eventContext) {

        Block block = event.getClickedBlock();
        if (block == null) return;

        Material material = block.getType();
        if (material != Material.VAULT ) return;

        event.setCancelled(true);
    }
}