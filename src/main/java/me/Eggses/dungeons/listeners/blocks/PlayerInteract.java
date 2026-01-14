package me.Eggses.dungeons.listeners.blocks;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.dispatch.EventManagerRegistry;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.items.ItemKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerInteract implements Listener {

    private final BlockRegistry blockRegistry;
    private final EventManagerRegistry<String> itemRegistry;
    private final ItemKey itemKey;

    public PlayerInteract(BlockRegistry blockRegistry,
                          EventManagerRegistry<String> itemRegistry,
                          ItemKey itemKey) {

        this.blockRegistry = blockRegistry;
        this.itemRegistry = itemRegistry;
        this.itemKey = itemKey;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        handleBlockInteract(event);
        handleItemInteract(event);
    }

    private void handleBlockInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) return;

        blockRegistry.handleEvent(block.getLocation(), event, EventContext.EMPTY);
    }

    private void handleItemInteract(PlayerInteractEvent event) {

        ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType().isAir()) return;

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;

        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();

        String value = pdc.get(itemKey.getKey(), PersistentDataType.STRING);
        if (value == null) return;

        itemRegistry.handleEvent(value, event, EventContext.EMPTY);
    }
}
