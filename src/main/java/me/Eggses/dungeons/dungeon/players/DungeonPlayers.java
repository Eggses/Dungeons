package me.Eggses.dungeons.dungeon.players;

import me.Eggses.dungeons.dungeon.DungeonInstance;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DungeonPlayers {

    private final Set<UUID> playersInWorld = new HashSet<>();

    public DungeonPlayers() {
    }

    public void add(Player player) {
        playersInWorld.add(player.getUniqueId());
    }

    public void remove(Player player) {
        playersInWorld.remove(player.getUniqueId());
    }

    public boolean contains(Player player) {
        return playersInWorld.contains(player.getUniqueId());
    }

    public boolean isEmpty() {
        return playersInWorld.isEmpty();
    }
}