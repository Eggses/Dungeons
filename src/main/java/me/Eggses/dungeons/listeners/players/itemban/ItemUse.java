package me.Eggses.dungeons.listeners.players.itemban;

import me.Eggses.dungeons.dungeon.DungeonManager;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ItemUse implements Listener {

    private static final Set<Material> BANNED_ITEM_INTERACTIONS = Set.of(
            Material.TRIDENT,

            Material.ENDER_EYE,

            Material.FISHING_ROD,
            Material.CARROT_ON_A_STICK,
            Material.WARPED_FUNGUS_ON_A_STICK,

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

    private final DungeonManager dungeonManager;

    public ItemUse(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (!dungeonManager.isInDungeon(event.getPlayer())) return;
        if (!event.getAction().isRightClick()) return;

        ItemStack itemHeld = event.getItem();
        if (itemHeld == null) return;

        if (BANNED_ITEM_INTERACTIONS.contains(itemHeld.getType())) {
            event.setCancelled(true);
            event.setUseItemInHand(Event.Result.DENY);
            event.setUseInteractedBlock(Event.Result.DENY);
        }
    }
}