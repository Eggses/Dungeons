package me.Eggses.dungeons.listeners.players.dungeonchanges;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeathController implements Listener {

    private final DungeonRegistry dungeonRegistry;
    private final DungeonEventRouter dungeonEventRouter;
    private final Map<UUID, World> worldsPlayerDiedIn = new HashMap<>();

    public DeathController(DungeonRegistry dungeonRegistry, DungeonEventRouter dungeonEventRouter) {
        this.dungeonRegistry = dungeonRegistry;
        this.dungeonEventRouter = dungeonEventRouter;
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        World dungeonWorld = worldsPlayerDiedIn.remove(player.getUniqueId());
        if (dungeonWorld == null) return;

        // Where Dungeon World is the World that they died in.
        dungeonEventRouter.handlePlayerRespawnEvent(event);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (!dungeonRegistry.isInDungeon(player)) return;
        worldsPlayerDiedIn.put(player.getUniqueId(), player.getWorld());
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        worldsPlayerDiedIn.remove(event.getPlayer().getUniqueId());
    }
}