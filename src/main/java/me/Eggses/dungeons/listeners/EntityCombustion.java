package me.Eggses.dungeons.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;

public class EntityCombustion implements Listener {

    @EventHandler
    public void onEntityCombustion(EntityCombustEvent event) {

        /*
        add an if in the dungeon world check at the very top!
        do that for all of these stupid things
         */

        if (event.getEntity() instanceof Player) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;

        if (event instanceof EntityCombustByEntityEvent) return;
        if (event instanceof EntityCombustByBlockEvent) return;

        event.setCancelled(true);
    }
}