package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dungeon.DungeonInstance;
import me.Eggses.dungeons.dungeon.DungeonManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class PlayerDungeonWorld implements Listener {

    private final DungeonManager dungeonManager;

    public PlayerDungeonWorld(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {

        Player player = event.getPlayer();

        World originalWorld = event.getFrom();
        World currentWorld = player.getWorld();

        // Enter Dungeon
        Optional<DungeonInstance> enteredInstance = dungeonManager.getDungeonInstance(currentWorld);
        enteredInstance.ifPresent(dungeonInstance -> dungeonInstance.addPlayer(player));

        // Leave Dungeon
        Optional<DungeonInstance> leftInstance = dungeonManager.getDungeonInstance(originalWorld);
        leftInstance.ifPresent(dungeonInstance -> dungeonInstance.removePlayer(player));
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        Optional<DungeonInstance> maybeDungeon = dungeonManager.getDungeonInstance(world);
        maybeDungeon.ifPresent(dungeonInstance -> dungeonInstance.removePlayer(player));
    }
}