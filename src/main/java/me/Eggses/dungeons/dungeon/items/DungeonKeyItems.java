package me.Eggses.dungeons.dungeon.items;

import me.Eggses.dungeons.dungeon.types.DungeonType;
import me.Eggses.dungeons.items.ItemHandler;
import me.Eggses.dungeons.items.ItemTemplate;
import me.Eggses.dungeons.utility.text.Placeholders;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.function.Consumer;

public class DungeonKeyItems {

    private static final Consumer<ItemMeta> DUNGEON_KEY_META_CONSUMER = (itemMeta -> {
        itemMeta.setMaxStackSize(1);
        itemMeta.setEnchantmentGlintOverride(true);
    });

    private final Map<DungeonType, KeyItem> dungeonKeys = new HashMap<>();

    private final ItemHandler itemHandler;
    private final TextFormatter textFormatter;

    public DungeonKeyItems(ItemHandler itemHandler, TextFormatter textFormatter) {
        this.itemHandler = itemHandler;
        this.textFormatter = textFormatter;
    }

    public void addKey(DungeonType dungeonType, KeyItem keyItem) {
        dungeonKeys.put(dungeonType, keyItem);

        System.out.println("Name of Key: " + keyItem.itemTemplate.name());
        System.out.println("Material of Key: " + keyItem.itemTemplate.material());
        //TODO
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

    public List<String> getDungeonKeyNames() {

        Set<DungeonType> dungeonTypes = dungeonKeys.keySet();
        List<String> formatted = new ArrayList<>();
        for (DungeonType dungeonType : dungeonTypes) {
            var name = textFormatter.formatName(dungeonType.name(), TextFormatter.SPLITTER_UNDERSCORE, TextFormatter.SEPARATOR_UNDERSCORE);
            formatted.add(name);
        }
        return formatted;
    }

    public record KeyItem(ItemTemplate itemTemplate, Placeholders placeholders, String uniqueKey) {}
}
