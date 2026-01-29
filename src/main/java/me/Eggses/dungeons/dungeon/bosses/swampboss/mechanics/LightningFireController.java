package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.blocks.task.FireBlockTask;
import me.Eggses.dungeons.dungeon.regions.WorldPosition;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class LightningFireController {

    private final BlockRegistry blockRegistry;
    private final Set<WorldPosition> locationsOfFire = new HashSet<>();

    public LightningFireController(BlockRegistry blockRegistry) {
        this.blockRegistry = blockRegistry;
    }

    public void placeFire(Block block, FireBurstExplosion fireBurstExplosion) {
        if (block == null) return;
        WorldPosition worldPosition = new WorldPosition(block);
        blockRegistry.addBlockAndEvent(worldPosition, EntityMoveEvent.class, fireBurstExplosion);
        blockRegistry.addBlockAndTaskBehaviour(worldPosition, new FireBlockTask().getTask());
        locationsOfFire.add(worldPosition);
    }

    public void removeFire(Block block) {
        if (block == null) return;
        WorldPosition worldPosition = new WorldPosition(block);
        blockRegistry.remove(worldPosition);
        locationsOfFire.remove(worldPosition);
    }

    public void removeAllFire() {
        for (WorldPosition worldPosition : locationsOfFire) {
            blockRegistry.remove(worldPosition);
        }
        locationsOfFire.clear();
    }
}
