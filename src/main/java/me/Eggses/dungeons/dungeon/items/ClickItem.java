package me.Eggses.dungeons.dungeon.items;

import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.menu.KeystoneMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ClickItem implements EventBehaviour<InventoryClickEvent> {

    @Override
    public void handleEvent(InventoryClickEvent event, EventContext eventContext) {

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (clickedInventory != player.getInventory()) return;

        Inventory top = event.getView().getTopInventory();
        if (!(top.getHolder() instanceof KeystoneMenu keyMenu)) return;

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;

        ClickType click = event.getClick();
        if (click != ClickType.LEFT && click != ClickType.RIGHT) return;

        event.setCancelled(true);

        keyMenu.insertDungeonKey(player, event.getSlot(), clickedItem);
    }
}
