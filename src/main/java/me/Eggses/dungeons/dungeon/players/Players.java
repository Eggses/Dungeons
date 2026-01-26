package me.Eggses.dungeons.dungeon.players;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Players {

    private final Set<Player> players = new HashSet<>();

    public Players() {
    }

    public void add(Player player) {
        players.add(player);
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public boolean contains(Player player) {
        return players.contains(player);
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public void clear() {
        players.clear();
    }

    public Set<Player> getPlayers() {
        return new HashSet<>(players);
    }
}