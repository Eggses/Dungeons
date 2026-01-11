package me.Eggses.dungeons.dungeon.events;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;
import me.Eggses.dungeons.eventinvoker.EventContext;
import me.Eggses.dungeons.eventinvoker.EventInvoker;
import me.Eggses.dungeons.eventinvoker.Invoker;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class DungeonInteraction implements Invoker {

    private final BlockRegistry blockRegistry;
    private final Consumer<DungeonContext> onInteractConsumer;
    private final DungeonContext dungeonContext;
    private final BooleanSupplier canInteract;

    public DungeonInteraction(BlockRegistry blockRegistry,
                              Consumer<DungeonContext> onInteractConsumer,
                              DungeonContext dungeonContext,
                              BooleanSupplier canInteract) {

        this.onInteractConsumer = onInteractConsumer;
        this.blockRegistry = blockRegistry;
        this.dungeonContext = dungeonContext;
        this.canInteract = canInteract;
    }

    @EventInvoker
    public void handleEvent(PlayerInteractEvent event, EventContext eventContext) {

        Block block = event.getClickedBlock();
        if (block == null) return;

        Action action = event.getAction();
        Material type = block.getType();
        String name = type.name();

        boolean trigger = (action == Action.RIGHT_CLICK_BLOCK && (type == Material.LEVER || name.endsWith("_BUTTON")))
                || (action == Action.PHYSICAL && name.endsWith("_PRESSURE_PLATE"));

        if (!trigger) {
            return;
        }

        if (!canInteract.getAsBoolean()) {
            event.setCancelled(true);
            return;
        }
        onInteractConsumer.accept(dungeonContext);
        blockRegistry.remove(block.getLocation());
    }
}
