package me.Eggses.dungeons.equipment;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class ArmourBuilder {

    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;

    private final TrimPattern trimPattern;
    private final TrimMaterial trimMaterial;

    public ArmourBuilder(ArmorMaterial armorMaterial,
                         TrimPattern trimPattern,
                         TrimMaterial trimMaterial) {

        this.trimPattern = trimPattern;
        this.trimMaterial = trimMaterial;

        helmet = createArmourPiece(armorMaterial.getHelmetMaterial());
        chestplate = createArmourPiece(armorMaterial.getChestplateMaterial());
        leggings = createArmourPiece(armorMaterial.getLeggingsMaterial());
        boots = createArmourPiece((armorMaterial.getBootsMaterial()));
    }

    public ArmourEquipment generateFullSet() {
        return new ArmourEquipment(helmet, chestplate, leggings, boots);
    }

    public ArmourEquipment generateTopHalfOfSet() {
        return new ArmourEquipment(helmet, chestplate, null, null);
    }

    public ArmourEquipment generateHelmetOnly() {
        return new ArmourEquipment(helmet, null, null, null);
    }

    private ItemStack createArmourPiece(Material armourMaterial) {

        ItemStack armourPiece = new ItemStack(armourMaterial, 1);
        ItemMeta itemMeta = armourPiece.getItemMeta();

        if (!(itemMeta instanceof ArmorMeta armorMeta)) {
            return armourPiece;
        }

        ArmorTrim armorTrim = new ArmorTrim(trimMaterial, trimPattern);
        armorMeta.setTrim(armorTrim);

        armourPiece.setItemMeta(armorMeta);

        return armourPiece;
    }


    public enum ArmorMaterial {
        LEATHER(
                Material.LEATHER_HELMET,
                Material.LEATHER_CHESTPLATE,
                Material.LEATHER_LEGGINGS,
                Material.LEATHER_BOOTS
        ),
        CHAINMAIL(
                Material.CHAINMAIL_HELMET,
                Material.CHAINMAIL_CHESTPLATE,
                Material.CHAINMAIL_LEGGINGS,
                Material.CHAINMAIL_BOOTS
        ),
        IRON(
                Material.IRON_HELMET,
                Material.IRON_CHESTPLATE,
                Material.IRON_LEGGINGS,
                Material.IRON_BOOTS
        ),
        GOLD(
                Material.GOLDEN_HELMET,
                Material.GOLDEN_CHESTPLATE,
                Material.GOLDEN_LEGGINGS,
                Material.GOLDEN_BOOTS
        ),
        DIAMOND(
                Material.DIAMOND_HELMET,
                Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_BOOTS
        ),
        NETHERITE(
                Material.NETHERITE_HELMET,
                Material.NETHERITE_CHESTPLATE,
                Material.NETHERITE_LEGGINGS,
                Material.NETHERITE_BOOTS
        );

        private final Material helmetMaterial;
        private final Material chestplateMaterial;
        private final Material leggingsMaterial;
        private final Material bootsMaterial;

        ArmorMaterial(Material helmetMaterial,
                      Material chestplateMaterial,
                      Material leggingsMaterial,
                      Material bootsMaterial) {
            this.helmetMaterial = helmetMaterial;
            this.chestplateMaterial = chestplateMaterial;
            this.leggingsMaterial = leggingsMaterial;
            this.bootsMaterial = bootsMaterial;
        }

        public Material getHelmetMaterial() {
            return helmetMaterial;
        }

        public Material getChestplateMaterial() {
            return chestplateMaterial;
        }

        public Material getLeggingsMaterial() {
            return leggingsMaterial;
        }

        public Material getBootsMaterial() {
            return bootsMaterial;
        }
    }
}