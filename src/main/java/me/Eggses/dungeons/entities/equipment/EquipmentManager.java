package me.Eggses.dungeons.entities.equipment;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class EquipmentManager {

    private final EntityEquipment entityEquipment;

    public EquipmentManager(LivingEntity entity) {
        this.entityEquipment = entity.getEquipment();
    }

    public void setEquipment(WeaponEquipment weaponEquipment, ArmourEquipment armourEquipment) {

        if (entityEquipment == null) return;

        setItem(weaponEquipment.getMainHand(), entityEquipment::setItemInMainHand);
        setItem(weaponEquipment.getOffHand(), entityEquipment::setItemInOffHand);

        setItem(armourEquipment.getHelmet(), entityEquipment::setHelmet);
        setItem(armourEquipment.getChestplate(), entityEquipment::setChestplate);
        setItem(armourEquipment.getLeggings(), entityEquipment::setLeggings);
        setItem(armourEquipment.getBoots(), entityEquipment::setBoots);

        zeroDropChances();
    }

    private void setItem(ItemStack item, Consumer<ItemStack> consumer) {
        if (item == null) return;

        item = item.clone();

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