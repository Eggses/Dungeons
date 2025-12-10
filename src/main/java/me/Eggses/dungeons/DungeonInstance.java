package me.Eggses.dungeons;

import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class DungeonInstance {

    private final DungeonInstance dungeonInstance;
    private World world;

    protected DungeonInstance(DungeonInstance dungeonInstance) {
        this.dungeonInstance = dungeonInstance;
        // maybe create world from the child class? No parent class has a world builder method...
    }

    public boolean contains(Player player) {
        return world.getPlayers().contains(player);
    }

    public World getWorld() {
        return world;
    }

    public void createWorld(String name) {

    }
}
