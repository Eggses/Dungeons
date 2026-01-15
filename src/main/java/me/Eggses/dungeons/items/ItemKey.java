package me.Eggses.dungeons.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class ItemKey {

    private static final String KEY = "eggses_dungeons_custom_item";
    private final NamespacedKey namespacedKey;

    public ItemKey(JavaPlugin plugin) {
        namespacedKey = new NamespacedKey(plugin, KEY);
    }

    public NamespacedKey getKey() {
        return namespacedKey;
    }

    public Optional<String> getMetaData(ItemStack item) {

        if (item == null || item.getType().isAir()) return Optional.empty();

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return Optional.empty();

        String value = meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);

        return Optional.ofNullable(value);
    }

    @Deprecated
    public boolean hasMetaData(ItemStack item, String uniqueValue) {
        if (uniqueValue == null) return false;
        if (item == null || item.getType().isAir()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        String value = meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
        if (value == null) return false;

        return value.equals(uniqueValue);
    }
}
