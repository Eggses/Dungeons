package me.Eggses.dungeons.dungeon.items;

import me.Eggses.dungeons.dungeon.types.DungeonType;
import me.Eggses.dungeons.items.ItemHandler;
import me.Eggses.dungeons.items.ItemTemplate;
import me.Eggses.dungeons.utility.text.Placeholders;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DungeonKeyItems {

    private static final Consumer<ItemMeta> DUNGEON_KEY_META_CONSUMER = (itemMeta -> {
        itemMeta.setMaxStackSize(1);
        itemMeta.setEnchantmentGlintOverride(true);
    });

    private final Map<DungeonType, KeyItem> dungeonKeys = new HashMap<>();

    private final ItemHandler itemHandler;

    public DungeonKeyItems(ItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    public void addKey(DungeonType dungeonType, KeyItem keyItem) {
        dungeonKeys.put(dungeonType, keyItem);
    }

    public void removeKey(DungeonType dungeonType) {
        dungeonKeys.remove(dungeonType);
    }

    public ItemStack getDungeonKey(DungeonType dungeonType, Placeholders placeholders) {

        KeyItem keyItem = dungeonKeys.get(dungeonType);
        if (keyItem == null) return null;

        keyItem.placeholders.addAll(placeholders);

        ItemStack dungeonKey = itemHandler.createItem(keyItem.itemTemplate, keyItem.placeholders, DUNGEON_KEY_META_CONSUMER);
        itemHandler.applyUniqueKey(dungeonKey, keyItem.uniqueKey);

        return dungeonKey;
    }

    public record KeyItem(ItemTemplate itemTemplate, Placeholders placeholders, String uniqueKey) {}
}
