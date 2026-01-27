package me.Eggses.dungeons.entities.equipment;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class ArmourEquipment {

    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;

    public ArmourEquipment(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public ArmourEquipment() {
        this(null, null, null, null);
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void alterAllArmour(Consumer<ItemMeta> itemMetaConsumer) {
        alter(helmet, itemMetaConsumer);
        alter(chestplate, itemMetaConsumer);
        alter(leggings, itemMetaConsumer);
        alter(boots, itemMetaConsumer);
    }

    private void alter(ItemStack item, Consumer<ItemMeta> itemMetaConsumer) {
        if (item == null || itemMetaConsumer == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        itemMetaConsumer.accept(itemMeta);
        item.setItemMeta(itemMeta);
    }
}