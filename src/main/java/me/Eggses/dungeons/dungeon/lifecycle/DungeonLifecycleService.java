package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.files.DungeonLog;
import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

public class DungeonLifecycleService {

    private final JavaPlugin plugin;
    private final DungeonRegistry dungeonRegistry;
    private final DungeonOpenPortalRegistry dungeonOpenPortalRegistry;
    private final TemplateReservation templateReservation;
    private final DungeonWorldManager dungeonWorldManager;
    private final DungeonLog dungeonLog;

    public DungeonLifecycleService(JavaPlugin plugin,
                                   DungeonRegistry dungeonRegistry,
                                   DungeonOpenPortalRegistry dungeonOpenPortalRegistry,
                                   TemplateReservation templateReservation,
                                   DungeonWorldManager dungeonWorldManager,
                                   DungeonLog dungeonLog) {

        this.plugin = plugin;
        this.dungeonRegistry = dungeonRegistry;
        this.dungeonOpenPortalRegistry = dungeonOpenPortalRegistry;
        this.templateReservation = templateReservation;
        this.dungeonWorldManager = dungeonWorldManager;
        this.dungeonLog = dungeonLog;
    }

    public void openPortal(DungeonInstance dungeonInstance) {
        dungeonOpenPortalRegistry.addToOpenPortals(dungeonInstance, dungeonInstance.getPortalChunkKeys());
    }

    public void closePortal(DungeonInstance dungeonInstance) {
        dungeonOpenPortalRegistry.removeFromOpenPortals(dungeonInstance, dungeonInstance.getPortalChunkKeys());
        templateReservation.free(dungeonInstance.getDungeonType());
    }

    public void destroyInstanceRuntime(DungeonInstance dungeonInstance) {
        World world = dungeonInstance.getDungeonWorld();
        Bukkit.unloadWorld(world, false);

        dungeonRegistry.removeDungeonInstance(world);
    }

    public void destroyWorld(String fileName) {

        dungeonWorldManager.attemptToDeleteInstance(
                fileName,
                e -> {
                    plugin.getLogger().log(Level.SEVERE, "Could not delete instance " + fileName, e);
                    dungeonLog.addError("Could not delete instance " + fileName);
                }
        );
    }

    public void destroyLeftAllInstanceWorlds() {
        List<String> fileNames = dungeonLog.getActiveNameList();
        fileNames.forEach(this::destroyWorld);
    }
}