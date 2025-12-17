package me.Eggses.dungeons.listeners.players.dungeonchanges;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import me.Eggses.dungeons.dungeon.regions.Position;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    private final DungeonEventRouter dungeonEventRouter;

    public PlayerInteract(DungeonEventRouter dungeonEventRouter) {
        this.dungeonEventRouter = dungeonEventRouter;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Block block = event.getClickedBlock();

        if (block == null) return;

        Action action = event.getAction();
        Material type = block.getType();
        World world = block.getWorld();

        Position positionOfBlock = new Position(block.getX(), block.getY(), block.getZ());

        if (action == Action.RIGHT_CLICK_BLOCK) {
            if (type == Material.LEVER || type.name().endsWith("_BUTTON")) {
                dungeonEventRouter.handleInteractEventInDungeon(world, positionOfBlock);
            }
            return;
        }

        if (action == Action.PHYSICAL) {
            if (type.name().endsWith("_PRESSURE_PLATE")) {
                dungeonEventRouter.handleInteractEventInDungeon(world, positionOfBlock);
            }
        }
    }
}