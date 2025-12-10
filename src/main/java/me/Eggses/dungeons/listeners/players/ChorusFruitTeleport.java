package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dungeon.DungeonManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ChorusFruitTeleport implements Listener {

    private final DungeonManager dungeonManager;

    public ChorusFruitTeleport(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {

        if (!dungeonManager.isInDungeon(event.getPlayer())) return;

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.CONSUMABLE_EFFECT) {
            event.setCancelled(true);
        }
    }
}
