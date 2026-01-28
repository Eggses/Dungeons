package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.blocks.BlockRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityCombustByBlockEvent;

import java.util.HashSet;
import java.util.Set;

public class LightningFireController {

    private final BlockRegistry blockRegistry;
    private final Set<Location> locationsOfFire = new HashSet<>();
    private final FireBurstExplosion fireBurstExplosion;

    public LightningFireController(BlockRegistry blockRegistry, FireBurstExplosion fireBurstExplosion) {
        this.blockRegistry = blockRegistry;
        this.fireBurstExplosion = fireBurstExplosion;
    }

    public void placeFire(Block block) {
        if (block == null) return;
        block.setType(Material.FIRE);
        Location location = block.getLocation();
        blockRegistry.addBlockAndEvent(location, EntityCombustByBlockEvent.class, fireBurstExplosion);
        locationsOfFire.add(location);
    }

    public void removeAllFire() {
        for (Location location : locationsOfFire) {
            location.getBlock().setType(Material.AIR);
            blockRegistry.remove(location);
        }
        locationsOfFire.clear();
    }
}
