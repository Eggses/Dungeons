package me.Eggses.dungeons.listeners.players.itemban;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

public class TotemUse implements Listener {

    private final DungeonManager dungeonManager;

    public TotemUse(DungeonManager dungeonManager) {
        this.dungeonManager = dungeonManager;
    }

    @EventHandler
    public void onTotemUse(EntityResurrectEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;
        if (!dungeonManager.isInDungeon(player)) return;

        event.setCancelled(true);

    }
}
