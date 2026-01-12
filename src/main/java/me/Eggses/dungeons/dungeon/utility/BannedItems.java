package me.Eggses.dungeons.dungeon.utility;

import me.Eggses.dungeons.utility.text.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BannedItems {

    private static final Set<Material> BANNED_ITEMS = Set.of(

            Material.MACE,
            Material.WOODEN_SPEAR,
            Material.STONE_SPEAR,
            Material.COPPER_SPEAR,
            Material.IRON_SPEAR,
            Material.GOLDEN_SPEAR,
            Material.DIAMOND_SPEAR,
            Material.NETHERITE_SPEAR,

            Material.SADDLE,
            Material.FEATHER,

            Material.BUNDLE,
            Material.BLUE_BUNDLE,
            Material.BLACK_BUNDLE,
            Material.BROWN_BUNDLE,
            Material.CYAN_BUNDLE,
            Material.GRAY_BUNDLE,
            Material.GREEN_BUNDLE,
            Material.LIME_BUNDLE,
            Material.MAGENTA_BUNDLE,
            Material.ORANGE_BUNDLE,
            Material.PINK_BUNDLE,
            Material.PURPLE_BUNDLE,
            Material.RED_BUNDLE,
            Material.WHITE_BUNDLE,
            Material.YELLOW_BUNDLE,
            Material.LIGHT_BLUE_BUNDLE,
            Material.LIGHT_GRAY_BUNDLE,

            Material.OAK_BOAT,
            Material.SPRUCE_BOAT,
            Material.BIRCH_BOAT,
            Material.JUNGLE_BOAT,
            Material.ACACIA_BOAT,
            Material.DARK_OAK_BOAT,
            Material.MANGROVE_BOAT,
            Material.BAMBOO_RAFT,
            Material.CHERRY_BOAT,
            Material.PALE_OAK_BOAT,

            Material.OAK_CHEST_BOAT,
            Material.SPRUCE_CHEST_BOAT,
            Material.BIRCH_CHEST_BOAT,
            Material.JUNGLE_CHEST_BOAT,
            Material.ACACIA_CHEST_BOAT,
            Material.DARK_OAK_CHEST_BOAT,
            Material.MANGROVE_CHEST_BOAT,
            Material.BAMBOO_CHEST_RAFT,
            Material.CHERRY_CHEST_BOAT,
            Material.PALE_OAK_CHEST_BOAT,

            Material.MINECART,
            Material.CHEST_MINECART,
            Material.FURNACE_MINECART,
            Material.HOPPER_MINECART,
            Material.TNT_MINECART,
            Material.COMMAND_BLOCK_MINECART
    );

    private static final Map<Material, String> MATERIAL_NAME_CACHE = new HashMap<>();

    private final MessageCreator messageCreator;
    private final TextFormatter textFormatter;

    public BannedItems(MessageCreator messageCreator, TextFormatter textFormatter) {
        this.messageCreator = messageCreator;
        this.textFormatter = textFormatter;
    }

    public boolean hasBannedItems(Player player) {

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;
            if (BANNED_ITEMS.contains(item.getType())) return true;
        }
        return false;
    }

    public void createAndSendBannedItemsMessage(Player player) {

        Placeholders placeholders = messageCreator.placeholders();
        placeholders.addPlaceholder(Placeholder.PLAYER, player.getName());

        Set<Material> bannedMaterials = new HashSet<>();

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;
            if (BANNED_ITEMS.contains(item.getType())) bannedMaterials.add(item.getType());
        }

        if (bannedMaterials.isEmpty()) {
            player.sendMessage(messageCreator.createMessage(Messages.BANNED_ITEMS_ALLOWED_TO_ENTER, placeholders));
            return;
        }

        String bannedItems = String.join(", ", convertMaterialSetToNames(bannedMaterials));
        bannedItems = bannedItems + ".";

        placeholders.addPlaceholder(Placeholder.BANNED_ITEMS, bannedItems);
        player.sendMessage(messageCreator.createMessage(Messages.BANNED_ITEMS_DENIED_TO_ENTER, placeholders));
    }

    private List<String> convertMaterialSetToNames(Set<Material> bannedMaterials) {
        List<String> names = new ArrayList<>();

        for (Material material : bannedMaterials) {
            String name = MATERIAL_NAME_CACHE.get(material);

            if (name == null) {
                name = textFormatter.formatName(material.name(), TextFormatter.SPLITTER_UNDERSCORE, TextFormatter.SEPARATOR_SPACE);
                MATERIAL_NAME_CACHE.put(material, name);
            }
            names.add(name);
        }
        return names;
    }
}
