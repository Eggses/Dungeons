package me.Eggses.dungeons.items;

import me.Eggses.dungeons.utility.text.Placeholders;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public record ItemRecord(ItemStackTemplate itemStackTemplate,
                         Consumer<ItemMeta> itemMetaConsumer,
                         String uniqueKey,
                         Placeholders placeholders) {
}
