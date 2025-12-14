package me.Eggses.dungeons.dungeon;

import me.Eggses.dungeons.configuration.DungeonLog;
import me.Eggses.dungeons.dungeon.baseinstance.MalignantMarsh;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.utility.InstanceNameManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Consumer;

public class DungeonManager {

    private final Map<World, DungeonInstance> dungeonInstances = new HashMap<>();
    private final Map<Long, DungeonInstance> instancesWithOpenPortals = new HashMap<>();

    private final JavaPlugin plugin;
    private final InstanceNameManager instanceNameManager;

    public DungeonManager(JavaPlugin plugin, DungeonLog dungeonLog) {
        this.plugin = plugin;
        instanceNameManager = new InstanceNameManager();
    }

    /* =========================================================
     * Queries
     * ========================================================= */

    public boolean isInDungeon(Player player) {
        DungeonInstance dungeonInstance = dungeonInstances.get(player.getWorld());
        if (dungeonInstance == null) return false;

        return dungeonInstance.isInDungeon(player);
    }

    public boolean isDungeonWorld(World world) {
        return dungeonInstances.get(world) != null;
    }

    /* =========================================================
     * Event routing
     * ========================================================= */

    public void handleMovementEventInWorld(Player player, Location destination, long chunkKey) {
        DungeonInstance dungeonInstance = instancesWithOpenPortals.get(chunkKey);
        if (dungeonInstance == null) return;

        dungeonInstance.handleMovementEventInWorld(player, destination);
    }

    public void handleMovementEventInDungeon(Player player, Location destination, long chunkKey) {
        runIfInstanceExists(destination.getWorld(),
                (instance) ->
                        instance.handleMovementEventInDungeon(player, destination, chunkKey));
    }

    public void handleInteractEventInDungeon(World world, Position positionOfBlock) {
        runIfInstanceExists(world, (instance) -> instance.handlePlayerInteractEvent(positionOfBlock));
    }

    public void handleDungeonTriggerCommand(World world, int argumentValue) {
        runIfInstanceExists(world, (instance) -> instance.handleDungeonTriggerCommand(argumentValue));
    }

    public void handleEntityDeathEventInDungeon(World world, UUID uuid) {
        runIfInstanceExists(world, (instance) -> instance.handleEntityDeathEvent(uuid));
    }

    private void runIfInstanceExists(World world, Consumer<DungeonInstance> action) {
        DungeonInstance dungeonInstance = dungeonInstances.get(world);
        if (dungeonInstance == null) return;

        action.accept(dungeonInstance);
    }

    /* =========================================================
     * Portal Management
     * ========================================================= */

    public void addToOpenPortals(DungeonInstance dungeonInstance, Set<Long> portalChunkKeys) {

        for (Long chunkKey : portalChunkKeys) {
            instancesWithOpenPortals.put(chunkKey, dungeonInstance);
        }
    }

    public void removeFromOpenPortals(Set<Long> portalChunkKeys) {

        for (Long chunkKey : portalChunkKeys) {
            instancesWithOpenPortals.remove(chunkKey);
        }
    }

    /* =========================================================
     * Instance Management
     * ========================================================= */

    public Optional<DungeonInstance> getDungeonInstance(World world) {
        DungeonInstance dungeonInstance = dungeonInstances.get(world);
        return Optional.ofNullable(dungeonInstance);
    }

    public void addDungeonInstance(DungeonInstance dungeonInstance) {
        World world = dungeonInstance.getDungeonWorld();
        if (world == null) return;
        dungeonInstances.put(world, dungeonInstance);
    }

    public void removeDungeonInstance(World world) {
        DungeonInstance dungeonInstance = dungeonInstances.remove(world);
        if (dungeonInstance == null) return;

        freeFolderName(dungeonInstance.getInstanceFileName());
    }

    public void freeFolderName(String instanceFolderName) {
        instanceNameManager.freeFolderName(instanceFolderName);
    }

    /* =========================================================
     * Dungeon creation
     * ========================================================= */

    public void createDungeon(DungeonType dungeonType) {
        switch (dungeonType) {

            case MALIGNANT_MARSH -> new MalignantMarsh();

            default -> {
            }
        }
    }

    public enum DungeonType {
        MALIGNANT_MARSH(),
    }


    @Deprecated
    public boolean isInNormalWorldPortalRoom(Player player) {
        /*
        THis wont work.... nothing to do with being in the wolrd...
        will always return false!
         */

        /*
        Okay this is used to prevent item drop: this is a very indpeedant thing
        from the dungeon instance... as it can exist without an instance right?

        portal room + the keystone is awlays there... so this isnt related to a
        Dungeon instance and probably should not be in this class...

         */
        return false;
    }
}