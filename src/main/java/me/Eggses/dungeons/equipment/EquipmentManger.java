package me.Eggses.dungeons.equipment;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class EquipmentManger {

    private final EntityEquipment entityEquipment;

    public EquipmentManger(LivingEntity entity) {
        this.entityEquipment = entity.getEquipment();
    }

    public void setEquipment(WeaponEquipment weaponEquipment, ArmourEquipment armourEquipment) {

        if (entityEquipment == null) return;

        if (weaponEquipment != null) {
            setItem(weaponEquipment.getMainHand(), entityEquipment::setItemInMainHand);
            setItem(weaponEquipment.getOffHand(), entityEquipment::setItemInOffHand);
        }

        if (armourEquipment != null) {
            setItem(armourEquipment.getHelmet(), entityEquipment::setHelmet);
            setItem(armourEquipment.getChestplate(), entityEquipment::setChestplate);
            setItem(armourEquipment.getLeggings(), entityEquipment::setLeggings);
            setItem(armourEquipment.getBoots(), entityEquipment::setBoots);
        }

        zeroDropChances();
    }

    public void setEquipment(WeaponEquipment weaponEquipment) {
        setEquipment(weaponEquipment, null);
    }

    public void setEquipment(ArmourEquipment armourEquipment) {
        setEquipment(null, armourEquipment);
    }

    private void setItem(ItemStack item, Consumer<ItemStack> consumer) {
        if (item == null) return;

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setUnbreakable(true);
        item.setItemMeta(itemMeta);

        consumer.accept(item);
    }

    private void zeroDropChances() {
        entityEquipment.setItemInMainHandDropChance(0.0f);
        entityEquipment.setItemInOffHandDropChance(0.0f);
        entityEquipment.setHelmetDropChance(0.0f);
        entityEquipment.setChestplateDropChance(0.0f);
        entityEquipment.setLeggingsDropChance(0.0f);
        entityEquipment.setBootsDropChance(0.0f);
    }
}