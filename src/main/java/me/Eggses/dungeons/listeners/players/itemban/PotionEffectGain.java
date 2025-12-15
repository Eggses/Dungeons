package me.Eggses.dungeons.listeners.players.itemban;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

public class PotionEffectGain implements Listener {

    private static final Set<PotionEffectType> BANNED_EFFECTS = Set.of(
            PotionEffectType.JUMP_BOOST,
            PotionEffectType.LEVITATION,
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.SLOW_FALLING,
            PotionEffectType.SPEED,
            PotionEffectType.WEAVING,
            PotionEffectType.WIND_CHARGED,
            PotionEffectType.INVISIBILITY
    );

    private final DungeonRegistry dungeonRegistry;

    public PotionEffectGain(DungeonRegistry dungeonRegistry) {
        this.dungeonRegistry = dungeonRegistry;
    }

    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent event) {

        if (!(event.getEntity() instanceof Player player)) return;
        if (!dungeonRegistry.isInDungeon(player)) return;

        PotionEffect effect = event.getNewEffect();
        if (effect == null) return;

        if (BANNED_EFFECTS.contains(effect.getType())) {
            event.setCancelled(true);
        }
    }
}