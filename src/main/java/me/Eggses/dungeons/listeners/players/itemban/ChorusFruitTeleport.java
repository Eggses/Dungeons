package me.Eggses.dungeons.listeners.players.itemban;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ChorusFruitTeleport implements Listener {

    private final DungeonRegistry dungeonRegistry;

    public ChorusFruitTeleport(DungeonRegistry dungeonRegistry) {
        this.dungeonRegistry = dungeonRegistry;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {

        if (!dungeonRegistry.isInDungeon(event.getPlayer())) return;

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.CONSUMABLE_EFFECT) {
            event.setCancelled(true);
        }
    }
}