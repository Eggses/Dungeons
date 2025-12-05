package me.Eggses.dungeons.equipment;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
}