package me.Eggses.dungeons.listeners.players.itemban;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

public class TotemUse implements Listener {

    private final DungeonRegistry dungeonRegistry;

    public TotemUse(DungeonRegistry dungeonRegistry) {
        this.dungeonRegistry = dungeonRegistry;
    }

    @EventHandler
    public void onTotemUse(EntityResurrectEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;
        if (!dungeonRegistry.isInDungeon(player)) return;

        event.setCancelled(true);

    }
}