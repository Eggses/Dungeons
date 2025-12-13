package me.Eggses.dungeons.dungeon.portals;

import me.Eggses.dungeons.dungeon.DungeonInstance;
import me.Eggses.dungeons.dungeon.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PortalController {

    private final JavaPlugin plugin;
    private final DungeonInstance dungeonInstance;
    private final DungeonPortal dungeonPortal;

    private boolean isOpen = false;

    public PortalController(JavaPlugin plugin,
                            DungeonInstance dungeonInstance,
                            DungeonPortal dungeonPortal) {

        this.plugin = plugin;
        this.dungeonInstance = dungeonInstance;
        this.dungeonPortal = dungeonPortal;
    }

    public void openDungeonPortal() {
        dungeonPortal.openPortal();
        isOpen = true;

        Bukkit.getScheduler().runTaskLater(plugin, dungeonInstance::closeDungeonPortal, dungeonPortal.getOpenDurationTicks());
    }

    public void closeDungeonPortal() {
        dungeonPortal.closePortal();
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void enterDungeon(Player player, World dungeonWorld) {
        Location spawningLocation = dungeonPortal.getSpawningLocationInsideDungeon().toLocationCenterBlock(dungeonWorld);
        player.teleport(spawningLocation);
    }

    public boolean isInPortalInMainWorld(@NotNull Player player) {
        return dungeonPortal.getPortalWorldRegion().within(player.getLocation());
    }

    public void leaveDungeon(Player player) {
        Location spawningLocation = dungeonPortal.getExitLocationInMainWorld();
        player.teleport(spawningLocation);
    }

    public boolean isInPortalInDungeonWorld(Player player) {
        return dungeonPortal.getPortalInDungeonRegion().within(player.getLocation());
    }

    // PRE CONDITION: Portal is WITHIN a SINGLE chunk.
    public long getChunkKeyOfPortal() {

        Region region = dungeonPortal.getPortalWorldRegion().getRegion();

        int chunkX = region.getMinX() >> 4;
        int chunkZ = region.getMinZ() >> 4;

        return Chunk.getChunkKey(chunkX, chunkZ);

        /*
        -1 / 16 = -0.0625. When doing as an int, the result is truncated to 0.
        But, the chunk coordinate is not 0, it's meant to be -1.
        floor(-0.0625) = -1 which is the value.

        Its /2^4 = /16 which is what Chuck coordinates are.
        However, it acts like it uses floor( / 16) which is why
        it works.
         */
    }
}