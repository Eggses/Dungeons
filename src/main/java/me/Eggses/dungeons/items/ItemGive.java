package me.Eggses.dungeons.items;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ItemGive {

    public ItemGive() {
    }

    public void giveOrDrop(Player player, ItemStack... items) {

        if (items == null || items.length == 0) return;

        Map<Integer, ItemStack> leftOverItems = player.getInventory().addItem(items);

        if (!leftOverItems.isEmpty()) {
            leftOverItems.values().forEach(item -> {
                Item droppedItem = player.getWorld().dropItemNaturally(player.getLocation(), item);
                droppedItem.setOwner(player.getUniqueId());
            });
        }
    }
}
