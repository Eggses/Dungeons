package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovement implements Listener {

    private final DungeonEventRouter dungeonEventRouter;

    public PlayerMovement(DungeonEventRouter dungeonEventRouter) {
        this.dungeonEventRouter = dungeonEventRouter;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        if (!event.hasExplicitlyChangedBlock()) return;

        Player player = event.getPlayer();

        Location destination = event.getTo();
        long chunkKeyOfDestination = event.getTo().getChunk().getChunkKey();

        if (player.getWorld().equals(Bukkit.getWorlds().getFirst())) {
            dungeonEventRouter.handleMovementEventInWorld(player, destination, chunkKeyOfDestination);
            return;
        }

        //This uses World as a key initially, not chunks; do not need to check a player is in the Dungeon World.
        dungeonEventRouter.handleMovementEventInDungeon(player, destination, chunkKeyOfDestination);
    }
}
