package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dungeon.DungeonManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class PlayerLaunchProjectile implements Listener {

    private final DungeonManager dungeonManager;

    public PlayerLaunchProjectile(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {

        if (!(event.getEntity().getShooter() instanceof Player player)) return;
        if (!dungeonManager.isInDungeon(player)) return;

        event.setCancelled(true);
    }
}