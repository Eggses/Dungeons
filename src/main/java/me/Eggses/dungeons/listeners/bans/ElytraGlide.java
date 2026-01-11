package me.Eggses.dungeons.listeners.bans;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class ElytraGlide implements Listener {

    private final DungeonRegistry dungeonRegistry;

    public ElytraGlide(DungeonRegistry dungeonRegistry) {
        this.dungeonRegistry = dungeonRegistry;
    }

    @EventHandler
    public void onGlide(EntityToggleGlideEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;
        if (!dungeonRegistry.isInDungeon(player)) return;

        if (event.isGliding()) {
            event.setCancelled(true);
        }
    }
}
