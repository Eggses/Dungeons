package me.Eggses.dungeons.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

public class Test implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void debugPlayerFireDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player p)) return;

        var c = event.getCause();
        if (c != EntityDamageEvent.DamageCause.FIRE && c != EntityDamageEvent.DamageCause.FIRE_TICK) return;

        Bukkit.getLogger().info("[FireDebug] cause=" + c
                + " cancelled=" + event.isCancelled()
                + " dmg=" + event.getDamage()
                + " final=" + event.getFinalDamage()
                + " fireTicks=" + p.getFireTicks()
                + " hasFireRes=" + p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE));
    }

}
