package me.Eggses.dungeons.listeners.bans;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ItemUse implements Listener {

    private static final Set<Material> BANNED_RIGHT_CLICK_ITEM_INTERACTIONS = Set.of(
            Material.WOODEN_SPEAR,
            Material.STONE_SPEAR,
            Material.COPPER_SPEAR,
            Material.IRON_SPEAR,
            Material.GOLDEN_SPEAR,
            Material.DIAMOND_SPEAR,
            Material.NETHERITE_SPEAR,

            Material.TRIDENT,
            Material.BOW,
            Material.CROSSBOW,

            Material.ENDER_EYE,

            Material.FISHING_ROD,
            Material.CARROT_ON_A_STICK,
            Material.WARPED_FUNGUS_ON_A_STICK,

            Material.ENCHANTED_GOLDEN_APPLE
    );

    private static final Set<Material> BANNED_RIGHT_CLICK_BLOCK_INTERACTIONS = Set.of(
            Material.DECORATED_POT,

            Material.OAK_TRAPDOOR,
            Material.SPRUCE_TRAPDOOR,
            Material.BIRCH_TRAPDOOR,
            Material.JUNGLE_TRAPDOOR,
            Material.ACACIA_TRAPDOOR,
            Material.DARK_OAK_TRAPDOOR,
            Material.MANGROVE_TRAPDOOR,
            Material.BAMBOO_TRAPDOOR,
            Material.CHERRY_TRAPDOOR,
            Material.PALE_OAK_TRAPDOOR,

            Material.OAK_FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.MANGROVE_FENCE_GATE,
            Material.BAMBOO_FENCE_GATE,
            Material.CHERRY_FENCE_GATE,
            Material.PALE_OAK_FENCE_GATE,

            Material.OAK_SHELF,
            Material.SPRUCE_SHELF,
            Material.BIRCH_SHELF,
            Material.JUNGLE_SHELF,
            Material.ACACIA_SHELF,
            Material.DARK_OAK_SHELF,
            Material.MANGROVE_SHELF,
            Material.BAMBOO_SHELF,
            Material.CHERRY_SHELF,
            Material.PALE_OAK_SHELF
    );

    private final DungeonRegistry dungeonRegistry;

    public ItemUse(DungeonRegistry dungeonRegistry) {
        this.dungeonRegistry = dungeonRegistry;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (!dungeonRegistry.isInDungeon(event.getPlayer())) return;
        if (!event.getAction().isRightClick()) return;

        Block clicked = event.getClickedBlock();
        if (clicked != null && BANNED_RIGHT_CLICK_BLOCK_INTERACTIONS.contains(clicked.getType())) {
            event.setCancelled(true);
            return;
        }

        ItemStack itemHeld = event.getItem();
        if (itemHeld != null && BANNED_RIGHT_CLICK_ITEM_INTERACTIONS.contains(itemHeld.getType())) {
            event.setCancelled(true);
        }
    }
}
