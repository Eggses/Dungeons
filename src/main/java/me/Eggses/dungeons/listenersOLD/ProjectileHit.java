package me.Eggses.dungeons.listenersOLD;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHit implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {

        // if something to ensure its a me.Eggses.dungeons.dungeon mob, either in the owlrd
        // or in the map

        event.getEntity().getLastDamageCause();
    }
}
