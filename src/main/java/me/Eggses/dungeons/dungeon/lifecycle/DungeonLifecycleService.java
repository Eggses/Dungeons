package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.files.DungeonLog;
import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class DungeonLifecycleService implements DungeonInstanceCoordinator {

    private final JavaPlugin plugin;
    private final DungeonRegistry dungeonRegistry;
    private final DungeonOpenPortalRegistry dungeonOpenPortalRegistry;
    private final DungeonWorldManager dungeonWorldManager;
    private final DungeonLog dungeonLog;

    public DungeonLifecycleService(JavaPlugin plugin,
                                   DungeonRegistry dungeonRegistry,
                                   DungeonOpenPortalRegistry dungeonOpenPortalRegistry,
                                   DungeonWorldManager dungeonWorldManager,
                                   DungeonLog dungeonLog) {

        this.plugin = plugin;
        this.dungeonRegistry = dungeonRegistry;
        this.dungeonOpenPortalRegistry = dungeonOpenPortalRegistry;
        this.dungeonWorldManager = dungeonWorldManager;
        this.dungeonLog = dungeonLog;
    }

    @Override
    public void openPortal(DungeonInstance dungeonInstance, Set<Long> portalChunkKeys) {
        dungeonOpenPortalRegistry.addToOpenPortals(dungeonInstance, portalChunkKeys);
    }

    @Override
    public void closePortal(Set<Long> portalChunkKeys) {
        dungeonOpenPortalRegistry.removeFromOpenPortals(portalChunkKeys);
    }

    @Override
    public void destroyInstanceRuntime(DungeonInstance dungeonInstance) {
        World world = dungeonInstance.getDungeonWorld();
        Bukkit.unloadWorld(world, false);

        dungeonOpenPortalRegistry.removeFromOpenPortals(dungeonInstance.getPortalChunkKeys());
        dungeonRegistry.removeDungeonInstance(world);
    }

    @Override
    public void destroyWorld(String fileName) {

        dungeonWorldManager.attemptToDeleteInstance(
                fileName,
                e -> {
                    plugin.getLogger().log(Level.SEVERE, "Could not delete instance " + fileName, e);
                    dungeonLog.addError("Could not delete instance " + fileName);
                }
        );
    }

    @Override
    public void endAllInstances(boolean destroyWorldFolder) {
        dungeonRegistry.getDungeonInstances().forEach(
                dungeonInstance -> dungeonInstance.forceEndDungeonInstance(destroyWorldFolder));
    }


    @Override
    public void destroyLeftAllInstanceWorlds() {
        List<String> fileNames = dungeonLog.getActiveNameList();
        fileNames.forEach(this::destroyWorld);
    }
}