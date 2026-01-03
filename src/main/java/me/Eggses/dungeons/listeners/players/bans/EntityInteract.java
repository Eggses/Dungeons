package me.Eggses.dungeons.listeners.players.bans;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Set;

public class EntityInteract implements Listener {

    private static final Set<EntityType> BANNED_ENTITIES = Set.of(
            EntityType.ARMOR_STAND,
            EntityType.HORSE,
            EntityType.DONKEY,
            EntityType.MULE,
            EntityType.SKELETON_HORSE,
            EntityType.ZOMBIE_HORSE,
            EntityType.ZOMBIE_NAUTILUS,
            EntityType.NAUTILUS,
            EntityType.CAMEL,
            EntityType.CAMEL_HUSK,
            EntityType.PIG,
            EntityType.STRIDER,
            EntityType.LLAMA,
            EntityType.TRADER_LLAMA
    );

    private final DungeonRegistry dungeonRegistry;

    public EntityInteract(DungeonRegistry dungeonRegistry) {
        this.dungeonRegistry = dungeonRegistry;
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (!dungeonRegistry.isInDungeon(event.getPlayer())) return;
        if (event.getPlayer().isOp()) return;

        Entity entity = event.getRightClicked();

        if (BANNED_ENTITIES.contains(entity.getType())) {
            event.setCancelled(true);
        }
    }
}
