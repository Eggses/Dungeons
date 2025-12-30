package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dungeon.utility.InstanceNameManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Login implements Listener {

    private World mainWorld;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (mainWorld == null) {
            mainWorld = Bukkit.getWorld("world");
            if (mainWorld == null) mainWorld = Bukkit.getWorlds().getFirst();
        }

        Player player = event.getPlayer();
        if (player.getWorld().getName().startsWith(InstanceNameManager.getInstancePrefix())) {
            player.teleport(mainWorld.getSpawnLocation());
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}