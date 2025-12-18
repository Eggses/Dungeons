package me.Eggses.dungeons.entities.equipment;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class ArmourCreator {

    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;

    private final TrimPattern trimPattern;
    private final TrimMaterial trimMaterial;

    public ArmourCreator(ArmourSetMaterial armorSetMaterial,
                         TrimPattern trimPattern,
                         TrimMaterial trimMaterial) {

        this.trimPattern = trimPattern;
        this.trimMaterial = trimMaterial;

        helmet = createArmourPiece(armorSetMaterial.getHelmetMaterial());
        chestplate = createArmourPiece(armorSetMaterial.getChestplateMaterial());
        leggings = createArmourPiece(armorSetMaterial.getLeggingsMaterial());
        boots = createArmourPiece((armorSetMaterial.getBootsMaterial()));
    }

    public ArmourCreator(ArmourSetMaterial armourSetMaterial) {
        this(armourSetMaterial, null, null);
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

        if (trimPattern == null || trimMaterial == null) return armourPiece;

        ItemMeta itemMeta = armourPiece.getItemMeta();

        if (!(itemMeta instanceof ArmorMeta armorMeta)) {
            return armourPiece;
        }

        ArmorTrim armorTrim = new ArmorTrim(trimMaterial, trimPattern);
        armorMeta.setTrim(armorTrim);

        armourPiece.setItemMeta(armorMeta);

        return armourPiece;
    }

    public ArmourEquipment generateSet(int style) {
        return switch (style) {
            case 1 -> generateHelmetOnly();
            case 2 -> generateTopHalfOfSet();
            case 4 -> generateFullSet();
            default -> new ArmourEquipment();
        };
    }

    public static ArmourEquipment createArmourFromString(String armourConfiguration) {

        if (armourConfiguration == null) return new ArmourEquipment();

        armourConfiguration = armourConfiguration.trim();
        String[] armourConfig = armourConfiguration.toUpperCase().split("_");
        if (armourConfig.length < 2) return new ArmourEquipment();

        ArmourSetMaterial armourSetMaterial = ArmourSetMaterial.getArmourSetMaterial(armourConfig[0]);
        if (armourSetMaterial == null) return new ArmourEquipment();

        int coverage;
        try {
            coverage = Integer.parseInt(armourConfig[armourConfig.length - 1]);
        } catch (NumberFormatException e) {
            return new ArmourEquipment();
        }
        if (coverage != 1 && coverage != 2 && coverage != 4) return new ArmourEquipment();

        if (armourConfig.length == 2) {
            ArmourCreator armourCreator = new ArmourCreator(armourSetMaterial);
            return armourCreator.generateSet(coverage);
        }
        if (armourConfig.length == 4) {
            TrimPattern trimPattern = ArmourTrimPattern.getTrimPattern(armourConfig[1]);
            TrimMaterial trimMaterial = ArmourTrimMaterial.getTrimMaterial(armourConfig[2]);
            if (trimPattern == null || trimMaterial == null) return new ArmourEquipment();
            ArmourCreator armourCreator = new ArmourCreator(armourSetMaterial, trimPattern, trimMaterial);
            return armourCreator.generateSet(coverage);
        }
        return new ArmourEquipment();
    }

    public enum ArmourSetMaterial {
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
        COPPER(
                Material.COPPER_HELMET,
                Material.COPPER_CHESTPLATE,
                Material.COPPER_LEGGINGS,
                Material.COPPER_BOOTS
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

        ArmourSetMaterial(Material helmetMaterial,
                      Material chestplateMaterial,
                      Material leggingsMaterial,
                      Material bootsMaterial) {
            this.helmetMaterial = helmetMaterial;
            this.chestplateMaterial = chestplateMaterial;
            this.leggingsMaterial = leggingsMaterial;
            this.bootsMaterial = bootsMaterial;
        }

        private Material getHelmetMaterial() {
            return helmetMaterial;
        }

        private Material getChestplateMaterial() {
            return chestplateMaterial;
        }

        private Material getLeggingsMaterial() {
            return leggingsMaterial;
        }

        private Material getBootsMaterial() {
            return bootsMaterial;
        }

        private static ArmourSetMaterial getArmourSetMaterial(String armourMaterial) {
            if (armourMaterial == null) return null;
            try {
                return ArmourSetMaterial.valueOf(armourMaterial);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    private enum ArmourTrimPattern {

        BOLT(TrimPattern.BOLT),
        COAST(TrimPattern.COAST),
        DUNE(TrimPattern.DUNE),
        EYE(TrimPattern.EYE),
        FLOW(TrimPattern.FLOW),
        HOST(TrimPattern.HOST),
        RAISER(TrimPattern.RAISER),
        RIB(TrimPattern.RIB),
        SENTRY(TrimPattern.SENTRY),
        SHAPER(TrimPattern.SHAPER),
        SILENCE(TrimPattern.SILENCE),
        SNOUT(TrimPattern.SNOUT),
        SPIRE(TrimPattern.SPIRE),
        TIDE(TrimPattern.TIDE),
        VEX(TrimPattern.VEX),
        WARD(TrimPattern.WARD),
        WAYFINDER(TrimPattern.WAYFINDER),
        WILD(TrimPattern.WILD);

        private final TrimPattern trimPattern;

        ArmourTrimPattern(TrimPattern trimPattern) {
            this.trimPattern = trimPattern;
        }

        private TrimPattern getTrimPattern() {
            return trimPattern;
        }

        private static TrimPattern getTrimPattern(String pattern) {
            if (pattern == null) return null;
            try {
                return ArmourTrimPattern.valueOf(pattern).getTrimPattern();
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    private enum ArmourTrimMaterial {

        AMETHYST(TrimMaterial.AMETHYST),
        COPPER(TrimMaterial.COPPER),
        DIAMOND(TrimMaterial.DIAMOND),
        EMERALD(TrimMaterial.EMERALD),
        GOLD(TrimMaterial.GOLD),
        IRON(TrimMaterial.IRON),
        LAPIS(TrimMaterial.LAPIS),
        NETHERITE(TrimMaterial.NETHERITE),
        QUARTZ(TrimMaterial.QUARTZ),
        REDSTONE(TrimMaterial.REDSTONE),
        RESIN(TrimMaterial.RESIN);

        private final TrimMaterial trimMaterial;

        ArmourTrimMaterial(TrimMaterial trimMaterial) {
            this.trimMaterial = trimMaterial;
        }

        private TrimMaterial getTrimMaterial() {
            return trimMaterial;
        }

        private static TrimMaterial getTrimMaterial(String material) {
            if (material == null) return null;
            try {
                return ArmourTrimMaterial.valueOf(material).getTrimMaterial();
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}