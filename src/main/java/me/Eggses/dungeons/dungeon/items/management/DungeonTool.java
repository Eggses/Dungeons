package me.Eggses.dungeons.dungeon.items.management;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;

public enum DungeonTool {

    DUNGEON_AXE("dungeon_axe",itemMeta -> {
        itemMeta.setCanDestroy(Set.of(Material.OAK_FENCE));
        itemMeta.setEnchantmentGlintOverride(true);
    }),
    ;

    private final String configurationSectionName;
    private final Consumer<ItemMeta> itemMetaConsumer;

    DungeonTool(String configurationSectionName, Consumer<ItemMeta> itemMetaConsumer) {
        this.configurationSectionName = configurationSectionName;
        this.itemMetaConsumer = itemMetaConsumer;
    }

    public String getConfigurationSectionName() {
        return configurationSectionName;
    }

    public Consumer<ItemMeta> getItemMetaConsumer() {
        return itemMetaConsumer;
    }

    public static DungeonTool getType(String type) {
        try {
            return DungeonTool.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
