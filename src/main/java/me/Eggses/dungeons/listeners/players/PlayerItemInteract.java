package me.Eggses.dungeons.listeners.players;

import me.Eggses.dungeons.dispatch.EventManagerRegistry;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.items.ItemKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import java.util.Optional;

public class PlayerItemInteract implements Listener {

    private final EventManagerRegistry<String> itemRegistry;
    private final ItemKey itemKey;

    public PlayerItemInteract(EventManagerRegistry<String> itemRegistry, ItemKey itemKey) {
        this.itemRegistry = itemRegistry;
        this.itemKey = itemKey;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType().isAir()) return;

        Optional<String> maybeStringMetaData = itemKey.getMetaData(itemStack);
        if (maybeStringMetaData.isEmpty()) return;

        String meta = maybeStringMetaData.get();

        itemRegistry.handleEvent(meta, event, EventContext.EMPTY);
    }
}
