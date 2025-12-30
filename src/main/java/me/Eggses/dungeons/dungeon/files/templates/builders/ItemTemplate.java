package me.Eggses.dungeons.dungeon.files.templates.builders;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class ItemTemplate {

    private final String itemName;
    private final String itemMaterial;
    private final List<String> itemLore;
    private final boolean itemGlow;

    public ItemTemplate(String itemName, String itemMaterial, List<String> itemLore, boolean itemGlow) {
        this.itemName = itemName;
        this.itemMaterial = itemMaterial;
        this.itemLore = itemLore;
        this.itemGlow = itemGlow;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemMaterial() {
        return itemMaterial;
    }

    public List<String> getItemLore() {
        return itemLore;
    }

    public boolean isItemGlow() {
        return itemGlow;
    }
}