package me.Eggses.dungeons.entities.equipment;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class WeaponEquipment {

    private final ItemStack mainHand;
    private final ItemStack offHand;

    public WeaponEquipment(Material mainHandMaterial, Material offHandMaterial) {
        this.mainHand = createItem(mainHandMaterial);
        this.offHand = createItem(offHandMaterial);
    }

    public WeaponEquipment(Material mainHandMaterial) {
        this(mainHandMaterial, null);
    }

    public WeaponEquipment() {
        this(null, null);
    }

    public ItemStack getMainHand() {
        return mainHand;
    }

    public ItemStack getOffHand() {
        return offHand;
    }

    private ItemStack createItem(Material material) {
        if (material == null) return null;
        return new ItemStack(material, 1);
    }

    public static WeaponEquipment createWeaponsFromString(String weaponConfiguration) {

        if (weaponConfiguration == null) return new WeaponEquipment();
        weaponConfiguration = weaponConfiguration.trim();

        String[] weaponConfig = weaponConfiguration.toUpperCase().split(",");

        Material mainHandMaterial = getMaterial(weaponConfig[0]);
        Material offhandMaterial = null;

        if (weaponConfig.length == 2) offhandMaterial = getMaterial(weaponConfig[1]);

        return new WeaponEquipment(mainHandMaterial, offhandMaterial);
    }

    private static Material getMaterial(String material) {
        if (material == null) return null;
        return Material.matchMaterial(material);
    }

    public void alterAllItems(Consumer<ItemMeta> itemMetaConsumer) {
        alter(mainHand, itemMetaConsumer);
        alter(offHand, itemMetaConsumer);
    }

    private void alter(ItemStack item, Consumer<ItemMeta> itemMetaConsumer) {
        if (item == null || itemMetaConsumer == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        itemMetaConsumer.accept(itemMeta);
        item.setItemMeta(itemMeta);
    }
}