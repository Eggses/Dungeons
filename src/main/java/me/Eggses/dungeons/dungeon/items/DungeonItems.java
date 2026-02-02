package me.Eggses.dungeons.dungeon.items;

import me.Eggses.dungeons.items.ItemHandler;
import me.Eggses.dungeons.items.ItemTemplate;
import me.Eggses.dungeons.utility.text.Placeholders;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DungeonItems<T extends Enum<T>> {

    public static final Consumer<ItemMeta> DUNGEON_GLOW_META_CONSUMER = itemMeta
            -> itemMeta.setEnchantmentGlintOverride(true);

    public static final Consumer<ItemMeta> DUNGEON_KEY_META_CONSUMER = itemMeta -> {
        itemMeta.setMaxStackSize(1);
        itemMeta.setEnchantmentGlintOverride(true);
    };


    private final Map<T, Item> dungeonsItems;
    private final ItemHandler itemHandler;
    private final TextFormatter textFormatter;

    public DungeonItems(Class<T> classOfEnum,
                        ItemHandler itemHandler,
                        TextFormatter textFormatter) {

        this.dungeonsItems = new EnumMap<>(classOfEnum);
        this.itemHandler = itemHandler;
        this.textFormatter = textFormatter;
    }

    public void addItem(T constant, ItemTemplate itemTemplate, Placeholders placeholders, String uniqueKey) {
        dungeonsItems.put(constant, new Item(itemTemplate, placeholders, uniqueKey));
    }

    public void addItem(T constant, ItemTemplate itemTemplate) {
        dungeonsItems.put(constant, new Item(itemTemplate, null, null));
    }

    public void removeItem(T constant) {
        dungeonsItems.remove(constant);
    }

    public ItemStack createItemMetaConsumer(T constant, Placeholders placeholders, Consumer<ItemMeta> itemMetaConsumer) {
        return createItem(constant, placeholders, null, itemMetaConsumer);
    }

    public ItemStack createItem(T constant,
                                Placeholders placeholders,
                                Supplier<Consumer<ItemStack>> itemStackConsumerSupplier,
                                Consumer<ItemMeta> itemMetaConsumer) {

        Item item = dungeonsItems.get(constant);
        if (item == null) return null;

        if (item.placeholders != null) placeholders.addAll(item.placeholders);

        ItemStack itemStack = itemHandler.createItem(item.itemTemplate, placeholders, itemMetaConsumer);

        if (item.uniqueKey != null) itemHandler.applyUniqueKey(itemStack, item.uniqueKey);

        var consumer = itemStackConsumerSupplier.get();
        if (consumer != null) consumer.accept(itemStack);

        return itemStack;
    }

    public List<String> getFormattedKeyNames() {

        List<String> formatted = new ArrayList<>();
        for (T constant : dungeonsItems.keySet()) {

            formatted.add(textFormatter.formatName(
                    constant.name(),
                    TextFormatter.SPLITTER_UNDERSCORE,
                    TextFormatter.SEPARATOR_UNDERSCORE
            ));
        }
        return formatted;
    }

    public void clear() {
        dungeonsItems.clear();
    }

    private record Item(
            ItemTemplate itemTemplate,
            @Nullable Placeholders placeholders,
            @Nullable String  uniqueKey) {}
}
