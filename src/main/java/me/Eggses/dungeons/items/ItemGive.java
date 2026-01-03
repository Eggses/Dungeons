package me.Eggses.dungeons.items;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ItemGive {

    public ItemGive() {
    }

    public void giveOrDrop(Player player, ItemAmount... itemAmounts) {

        if (itemAmounts == null) return;

        for (ItemAmount itemAmount : itemAmounts) {

            ItemStack itemToGive = itemAmount.itemStack;
            int amountToGive = itemAmount.quantity;
            int maxStackSize = itemToGive.getMaxStackSize();

            while (amountToGive > 0) {

                int batchStackAmount = Math.min(maxStackSize, amountToGive);

                ItemStack item = itemToGive.clone();
                item.setAmount(batchStackAmount);

                giveOrDrop(player, item);
                amountToGive -= batchStackAmount;
            }
        }
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

    public record ItemAmount(@NotNull ItemStack itemStack, int quantity){}
}
