package me.Eggses.dungeons.dungeon.portals;

import me.Eggses.dungeons.dispatch.ChunkMappingRegistry;
import org.bukkit.Location;

import java.util.Set;

public class OpenPortalRegistry {

    private final ChunkMappingRegistry<PortalController> openPortals = new ChunkMappingRegistry<>();

    public void addOpenPortal(PortalController openPortalController) {
        openPortals.add(openPortalController, openPortalController.getChunkKeysEncompassedOfEntryPortal());
    }

    public void removeOpenPortal(PortalController portalControllerToClose) {
        openPortals.remove(portalControllerToClose, portalControllerToClose.getChunkKeysEncompassedOfEntryPortal());
    }

    public PortalController getPortalController(Long chunkKey, Location location) {

        Set<PortalController> portalControllers = openPortals.get(chunkKey);
        if (portalControllers == null) return null;

        for (PortalController portalController : portalControllers) {
            if (portalController.isInPortalOutsideDungeon(location)) {
                return portalController;
            }
        }

        return null;
    }
}
