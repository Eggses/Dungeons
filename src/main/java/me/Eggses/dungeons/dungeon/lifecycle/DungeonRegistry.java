package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DungeonRegistry {

    private final Map<World, DungeonInstance> dungeonInstances = new HashMap<>();

    public DungeonRegistry() {
    }

    public boolean isInDungeon(Player player) {
        DungeonInstance dungeonInstance = dungeonInstances.get(player.getWorld());
        if (dungeonInstance == null) return false;

        return dungeonInstance.isInDungeon(player);
    }

    public boolean isDungeonWorld(World world) {
        return dungeonInstances.containsKey(world);
    }

    public void addDungeonInstance(DungeonInstance dungeonInstance) {
        World world = dungeonInstance.getDungeonWorld();
        if (world == null) return;
        dungeonInstances.put(world, dungeonInstance);
    }

    public void removeDungeonInstance(World world) {
        dungeonInstances.remove(world);
    }

    public DungeonInstance getDungeonInstance(World world) {
        return dungeonInstances.get(world);
    }

    public Set<World> getDungeonWorlds() {
        return Set.copyOf(dungeonInstances.keySet());
    }

    public DungeonInstance getDungeonWithWorldName(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) return null;

        return getDungeonInstance(world);
    }

    public void endDungeonInstance(DungeonInstance dungeonInstance, boolean destroyWorldFolder) {
        dungeonInstance.forceEndDungeonInstance(destroyWorldFolder);
    }

    public void endAllInstances(boolean destroyWorldFolder) {
        for (DungeonInstance dungeonInstance : Set.copyOf(dungeonInstances.values())) {
            endDungeonInstance(dungeonInstance, destroyWorldFolder);
        }
    }
}