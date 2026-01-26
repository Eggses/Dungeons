package me.Eggses.dungeons.dungeon.utility;

import org.bukkit.entity.Player;

import java.util.*;

public class DungeonPlayerStats {

    private final Map<UUID, Long> playerStartTimes = new HashMap<>();

    private final long startTime;

    public DungeonPlayerStats(long startTime) {
        this.startTime = startTime;
    }

    public void addPlayer(Player player) {
        playerStartTimes.putIfAbsent(player.getUniqueId(), startTime);
    }

    public void endTracking(Player player) {
        playerStartTimes.remove(player.getUniqueId());
    }

    public OptionalLong getTimeTakenInMS(UUID uuid, Long endTime) {
        Long start = playerStartTimes.remove(uuid);
        if (start == null) return OptionalLong.empty();

        return OptionalLong.of(endTime - start);
    }
}
