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

    /*
    This does not handle rejoining in a Dungeon...
    you need to see what happens if A you logout in world then delete that world...
    what happens on login

    then also if someone logs out in a Dungeon.. do you TP them back in? maybe if it still exists
    though you could complex behaviour:

    just kick: on login... if world is NULL or world is a Dungeon... TP them to main world?
    but if world is NULL... is there error?

    entity combat can now be handled as you can pull an instance and therefore an entity manager with a world
    as each dungeon maintains its own map!
     */

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