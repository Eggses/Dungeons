package me.Eggses.dungeons.dungeon.items.management;

import io.papermc.paper.block.BlockPredicate;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAdventurePredicate;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.BlockTypeKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Consumer;

public enum DungeonTool {
    @SuppressWarnings("UnstableApiUsage")
    AXE("dungeon_axe",itemStack -> {
        RegistryKeySet<@NotNull BlockType> blocks = RegistrySet.keySet(RegistryKey.BLOCK, BlockTypeKeys.OAK_FENCE);
        BlockPredicate blockPredicate = BlockPredicate.predicate().blocks(blocks).build();
        ItemAdventurePredicate canBreak = ItemAdventurePredicate.itemAdventurePredicate().addPredicate(blockPredicate).build();

        itemStack.setData(DataComponentTypes.CAN_BREAK, canBreak);
    }),
    ;

    private final String configurationSectionName;
    private final Consumer<ItemStack> itemStackConsumer;

    DungeonTool(String configurationSectionName, Consumer<ItemStack> itemStackConsumer) {
        this.configurationSectionName = configurationSectionName;
        this.itemStackConsumer = itemStackConsumer;
    }

    public String getConfigurationSectionName() {
        return configurationSectionName;
    }

    public Consumer<ItemStack> getItemStackConsumer() {
        return itemStackConsumer;
    }

    public static DungeonTool getType(String type) {
        try {
            return DungeonTool.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
