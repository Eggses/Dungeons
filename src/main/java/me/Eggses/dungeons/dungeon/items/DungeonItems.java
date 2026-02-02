package me.Eggses.dungeons.dungeon.items;

import io.papermc.paper.block.BlockPredicate;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAdventurePredicate;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.BlockTypeKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import me.Eggses.dungeons.items.ItemHandler;
import me.Eggses.dungeons.items.ItemTemplate;
import me.Eggses.dungeons.utility.text.Placeholders;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class DungeonItems<T extends Enum<T>> {

    public static final Consumer<ItemMeta> DUNGEON_GLOW_META_CONSUMER = itemMeta
            -> itemMeta.setEnchantmentGlintOverride(true);

    public static final Consumer<ItemMeta> DUNGEON_KEY_META_CONSUMER = itemMeta -> {
        itemMeta.setMaxStackSize(1);
        itemMeta.setEnchantmentGlintOverride(true);
    };

    @SuppressWarnings("UnstableApiUsage")
    public static final Consumer<ItemStack> DUNGEON_TOOLS_AXE_CONSUMER = itemStack -> {

        RegistryKeySet<@org.jetbrains.annotations.NotNull BlockType> blocks = RegistrySet.keySet(RegistryKey.BLOCK, BlockTypeKeys.OAK_FENCE);
        BlockPredicate blockPredicate = BlockPredicate.predicate().blocks(blocks).build();
        ItemAdventurePredicate canBreak = ItemAdventurePredicate.itemAdventurePredicate().addPredicate(blockPredicate).build();

        itemStack.setData(DataComponentTypes.CAN_BREAK, canBreak);
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
                                Consumer<ItemStack> itemStackConsumer,
                                Consumer<ItemMeta> itemMetaConsumer) {

        Item item = dungeonsItems.get(constant);
        if (item == null) return null;

        if (item.placeholders != null) placeholders.addAll(item.placeholders);

        ItemStack itemStack = itemHandler.createItem(item.itemTemplate, placeholders, itemMetaConsumer);

        if (item.uniqueKey != null) itemHandler.applyUniqueKey(itemStack, item.uniqueKey);

        if (itemStackConsumer != null) itemStackConsumer.accept(itemStack);

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
