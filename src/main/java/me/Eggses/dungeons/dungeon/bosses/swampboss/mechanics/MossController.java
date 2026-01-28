package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.dungeon.regions.WorldPosition;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;

public class MossController {

    private final BlockRegistry blockRegistry;
    private final Set<WorldPosition> locationsOfMoss = new HashSet<>();
    private final PoisonCarpet poisonCarpet = new PoisonCarpet();

    public MossController(BlockRegistry blockRegistry) {
        this.blockRegistry = blockRegistry;
    }

    public void placeMoss(Block block) {
        block.setType(Material.MOSS_CARPET);
        WorldPosition worldPosition = new WorldPosition(block);
        blockRegistry.addBlockAndEvent(worldPosition, PlayerMoveEvent.class, poisonCarpet);
        locationsOfMoss.add(worldPosition);
    }

    public void removeAllMoss() {

        for (WorldPosition worldPosition : locationsOfMoss) {
            blockRegistry.remove(worldPosition);

            Block block = worldPosition.toLocation().getBlock();
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
