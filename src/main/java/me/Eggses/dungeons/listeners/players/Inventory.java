package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dispatch.EventManagerRegistry;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.items.ItemKey;
import me.Eggses.dungeons.menu.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class Inventory implements Listener {

    private final EventManagerRegistry<String> itemRegistry;
    private final ItemKey itemKey;

    public Inventory(EventManagerRegistry<String> itemRegistry, ItemKey itemKey) {
        this.itemRegistry = itemRegistry;
        this.itemKey = itemKey;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getView().getTopInventory().getHolder() instanceof Menu) {
            event.setCancelled(true);
        }

        handleClickingAnItem(event);
        handleClickInMenu(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof Menu menu) menu.cleanUpOnClose();
    }

    private void handleClickingAnItem(InventoryClickEvent event) {

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        Optional<String> key = itemKey.getMetaData(item);
        if (key.isEmpty()) return;

        itemRegistry.handleEvent(key.get(), event, EventContext.EMPTY);
    }

    private void handleClickInMenu(InventoryClickEvent event) {

        org.bukkit.inventory.Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;

        if (!(clickedInventory.getHolder() instanceof Menu menu)) return;

        ClickType clickType = event.getClick();
        if (clickType != ClickType.LEFT && clickType != ClickType.RIGHT) return;

        int slot = event.getSlot();
        menu.click(slot);
    }
}
