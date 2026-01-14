package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDungeonWorld implements Listener {

    private final DungeonRegistry dungeonRegistry;
    private final DungeonEventRouter dungeonEventRouter;

    public PlayerDungeonWorld(DungeonRegistry dungeonRegistry, DungeonEventRouter dungeonEventRouter) {
        this.dungeonRegistry = dungeonRegistry;
        this.dungeonEventRouter = dungeonEventRouter;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World worldEntered = player.getWorld();
        World worldLeft = event.getFrom();

        DungeonInstance dungeonInstanceEntered = dungeonRegistry.getDungeonInstance(worldEntered);
        if (dungeonInstanceEntered != null) {
            dungeonInstanceEntered.addPlayer(player);
            return;
        }

        DungeonInstance dungeonInstanceLeft = dungeonRegistry.getDungeonInstance(worldLeft);
        if (dungeonInstanceLeft != null) {
            dungeonInstanceLeft.removePlayer(player);
        }
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        dungeonEventRouter.handleEvent(event, event.getPlayer().getWorld());
    }
}