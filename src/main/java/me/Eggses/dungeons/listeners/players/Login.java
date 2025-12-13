package me.Eggses.dungeons.listeners.players;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Login implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        if (player.getName().startsWith("dungeon_instance_")) {
            World mainWorld = Bukkit.getWorlds().getFirst();
            player.teleport(mainWorld.getSpawnLocation());
        }

        player.setGameMode(GameMode.SURVIVAL);
    }
}
