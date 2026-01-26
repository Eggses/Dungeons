package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.blocks.BlockRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;

public class MossController {

    private final BlockRegistry blockRegistry;
    private final Set<Location> locationsOfMoss = new HashSet<>();
    private final PoisonCarpet poisonCarpet = new PoisonCarpet();

    public MossController(BlockRegistry blockRegistry) {
        this.blockRegistry = blockRegistry;
    }

    public void placeMoss(Block block) {
        block.setType(Material.MOSS_CARPET);
        Location location = block.getLocation();
        blockRegistry.addBlockAndEvent(location, PlayerMoveEvent.class, poisonCarpet);
        locationsOfMoss.add(location);
    }

    public void removeAllMoss() {

        for (Location location : locationsOfMoss) {
            blockRegistry.remove(location);

            Block block = location.getBlock();
            if (block.getType() == Material.MOSS_CARPET) {
                block.setType(Material.AIR);
            }
        }
        locationsOfMoss.clear();
    }

    public void setApply(boolean apply) {
        poisonCarpet.setApply(apply);
    }
}
