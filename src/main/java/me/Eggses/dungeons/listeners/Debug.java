package me.Eggses.dungeons.listeners;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Debug implements Listener {

    @EventHandler
    public void onEntity(EntityRemoveFromWorldEvent event) {

        if (!event.getWorld().getName().contains("malignant_marsh")) return;

        if (event.getEntity().isDead()) {
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("Entity Removed from world but was dead"));
            return;
        }

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("Entity Removed from world but was NOT DEAD!!!"));

    }
}
