package me.Eggses.dungeons.listeners.players.itemban;

import me.Eggses.dungeons.dungeon.DungeonManager;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FrostWalkerFreeze implements Listener {

    private final DungeonManager dungeonManager;

    public FrostWalkerFreeze(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onFrostWalker(EntityBlockFormEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;
        if (!dungeonManager.isInDungeon(player)) return;

        ItemStack boots = player.getInventory().getBoots();
        if (boots == null) return;
        ItemMeta itemMeta = boots.getItemMeta();
        if (itemMeta == null) return;

        if (itemMeta.hasEnchant(Enchantment.FROST_WALKER)) {
            event.setCancelled(true);
        }
    }
}
