package me.Eggses.dungeons.listeners.players.itemban;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class ShieldCooldown implements Listener {

    private static final int COOLDOWN = 20 * 10;

    private final JavaPlugin plugin;
    private final DungeonRegistry dungeonRegistry;

    public ShieldCooldown(JavaPlugin plugin, DungeonRegistry dungeonRegistry) {
        this.plugin = plugin;
        this.dungeonRegistry = dungeonRegistry;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;
        if (!dungeonRegistry.isInDungeon(player)) return;

        if (!(event.getFinalDamage() == 0 && player.isBlocking())) return;

        ShieldSlot shieldSlot = removeAndGetShieldSlot(player);
        if (shieldSlot == null) return;

        player.setCooldown(Material.SHIELD, COOLDOWN);

        Bukkit.getScheduler().runTaskLater(plugin, () -> returnShield(player, shieldSlot), 1);
    }

    private ShieldSlot removeAndGetShieldSlot(Player player) {

        PlayerInventory playerInventory = player.getInventory();

        ItemStack mainHand = playerInventory.getItemInMainHand();
        if (mainHand.getType() == Material.SHIELD) {
            ItemStack shield = mainHand.clone();
            playerInventory.setItemInMainHand(null);
            return new ShieldSlot(shield, ShieldSlot.Hand.MAIN_HAND);
        }

        ItemStack offHand = playerInventory.getItemInOffHand();
        if (offHand.getType() == Material.SHIELD) {
            ItemStack shield = offHand.clone();
            playerInventory.setItemInOffHand(null);
            return new ShieldSlot(shield, ShieldSlot.Hand.OFF_HAND);
        }

        return null;
    }

    private void returnShield(Player player, ShieldSlot shieldSlot) {

        PlayerInventory playerInventory = player.getInventory();
        if (shieldSlot == null) return;

        if (shieldSlot.hand == ShieldSlot.Hand.MAIN_HAND && playerInventory.getItemInMainHand().getType().isAir()) {
            playerInventory.setItemInMainHand(shieldSlot.shield);
            return;
        }

        if (shieldSlot.hand == ShieldSlot.Hand.OFF_HAND && playerInventory.getItemInOffHand().getType().isAir()) {
            playerInventory.setItemInOffHand(shieldSlot.shield);
            return;
        }

        player.getWorld().dropItemNaturally(player.getLocation(), shieldSlot.shield);
    }

    @SuppressWarnings("ClassCanBeRecord")
    private static class ShieldSlot {

        private final ItemStack shield;
        private final Hand hand;

        private ShieldSlot(ItemStack shield, Hand hand) {
            this.shield = shield;
            this.hand = hand;
        }

        private enum Hand {
            MAIN_HAND(),
            OFF_HAND()
        }
    }
}