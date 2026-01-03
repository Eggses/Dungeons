package me.Eggses.dungeons.items;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemKeyManager {

    private static final String KEY = "eggses_dungeons_custom_item";
    private final NamespacedKey namespacedKey;

    public ItemKeyManager(JavaPlugin plugin) {
        namespacedKey = new NamespacedKey(plugin, KEY);
    }

    public NamespacedKey getKey() {
        return namespacedKey;
    }
}