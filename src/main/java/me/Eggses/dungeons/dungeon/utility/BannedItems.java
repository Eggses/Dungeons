package me.Eggses.dungeons.dungeon.utility;

import me.Eggses.dungeons.utility.MessageCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BannedItems {

    private static final Set<Material> BANNED_ITEMS = Set.of(
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
            Material.LIGHT_GRAY_BUNDLE
    );

    private static final String ALLOWED_TO_ENTER = "<green>You are allowed to enter this Dungeon.</green>";
    private static final String DENIED_TO_ENTER = "<red>You are unable to enter this Dungeon with these items: </red>";
    private static final String ITEM_COLOUR = "<dark_red>";

    private static final Map<Material, String> MATERIAL_NAME_CACHE = new HashMap<>();

    private final MessageCreator messageCreator;

    public BannedItems(MessageCreator messageCreator) {
        this.messageCreator = messageCreator;
    }

    public boolean hasBannedItems(Player player) {

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;
            if (BANNED_ITEMS.contains(item.getType())) return true;
        }
        return false;
    }

    public void createAndSendBannedItemsMessage(Player player) {
        Set<Material> bannedMaterials = new HashSet<>();

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;
            if (BANNED_ITEMS.contains(item.getType())) bannedMaterials.add(item.getType());
        }

        if (bannedMaterials.isEmpty()) {
            player.sendMessage(messageCreator.createMessage(ALLOWED_TO_ENTER));
            return;
        }

        String bannedItems = String.join(", ", convertMaterialSetToNames(bannedMaterials));
        bannedItems = bannedItems + ".";
        player.sendMessage(messageCreator.createMessage(DENIED_TO_ENTER + ITEM_COLOUR + bannedItems));
    }

    private List<String> convertMaterialSetToNames(Set<Material> bannedMaterials) {
        List<String> names = new ArrayList<>();

        for (Material material : bannedMaterials) {
            String name = MATERIAL_NAME_CACHE.get(material);

            if (name == null) {
                name = cleanEnumMaterialName(material);
                MATERIAL_NAME_CACHE.put(material, name);
            }
            names.add(name);
        }
        return names;
    }

    private String cleanEnumMaterialName(Material material) {

        StringBuilder cleanedName = new StringBuilder();

        String[] splitName = material.name().split("_");

        for (String part : splitName) {
            if (part.length() == 1) {
                cleanedName.append(part).append(" ");
                continue;
            }

            String restInLowerCase = part.substring(1).toLowerCase();

            String full = part.charAt(0) + restInLowerCase + " ";

            cleanedName.append(full);
        }
        return cleanedName.toString().strip();
    }
}