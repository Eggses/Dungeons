package me.Eggses.dungeons.dungeon.events;

import me.Eggses.dungeons.eventinvoker.EventContext;
import me.Eggses.dungeons.eventinvoker.EventInvoker;
import me.Eggses.dungeons.eventinvoker.Invoker;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

public class CancelUse implements Invoker {

    @EventInvoker
    public void handleEvent(PlayerInteractEvent event, EventContext eventContext) {

        Block block = event.getClickedBlock();
        if (block == null) return;

        Material material = block.getType();
        if (material != Material.VAULT) return;

        event.setCancelled(true);
    }
}
