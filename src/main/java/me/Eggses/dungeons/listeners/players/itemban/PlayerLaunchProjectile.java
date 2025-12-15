package me.Eggses.dungeons.listeners.players.itemban;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class PlayerLaunchProjectile implements Listener {

    private final DungeonRegistry dungeonRegistry;

    public PlayerLaunchProjectile(DungeonRegistry dungeonRegistry) {
        this.dungeonRegistry = dungeonRegistry;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {

        if (!(event.getEntity().getShooter() instanceof Player player)) return;
        if (!dungeonRegistry.isInDungeon(player)) return;

        event.setCancelled(true);
    }
}