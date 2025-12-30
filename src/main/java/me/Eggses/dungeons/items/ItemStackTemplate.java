package me.Eggses.dungeons.items;

import me.Eggses.dungeons.dungeon.files.templates.builders.ItemTemplate;

import java.util.List;

public class ItemStackTemplate {

    private final String name;
    private final String material;
    private final List<String> lore;
    private final boolean glow;

    public ItemStackTemplate(String name, String material, List<String> lore, boolean glow) {
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.glow = glow;
    }

    public ItemStackTemplate(ItemTemplate itemTemplate) {
        this.name = itemTemplate.getItemName();
        this.material = itemTemplate.getItemMaterial();
        this.lore = itemTemplate.getItemLore();
        this.glow = itemTemplate.isItemGlow();
    }

    public String getName() {
        return name;
    }

    public String getMaterial() {
        return material;
    }

    public List<String> getLore() {
        return lore;
    }

    public boolean isGlow() {
        return glow;
    }
}