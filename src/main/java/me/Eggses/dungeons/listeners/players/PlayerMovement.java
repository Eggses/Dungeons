package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dungeon.DungeonManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovement implements Listener {

    private final DungeonManager dungeonManager;

    public PlayerMovement(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        if (!event.hasExplicitlyChangedBlock()) return;

        Player player = event.getPlayer();

        if (player.getWorld().equals(Bukkit.getWorlds().getFirst())) {
            dungeonManager.handleMovementEventInWorld(player, event.getTo().getChunk().getChunkKey());
            return;
        }

        // This checks if the Player is in the Dungeon - do not need to check twice.
        dungeonManager.handleMovementEventInDungeon(player);
    }
}
