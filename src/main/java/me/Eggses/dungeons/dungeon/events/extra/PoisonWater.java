package me.Eggses.dungeons.dungeon.events.extra;

import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PoisonWater implements EventBehaviour<PlayerMoveEvent> {

    private static final PotionEffect POISON = new PotionEffect(
            PotionEffectType.POISON,
            7 * 20,
            6,
            false,
            true,
            true
    );

    private static final long COOLDOWN = 5000;

    Map<UUID, Long> lastEffectApply = new HashMap<>();

    @Override
    public void handleEvent(PlayerMoveEvent event, EventContext eventContext) {

        if (event.getTo().getBlock().getType() != Material.WATER) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Long lastApplyTime = lastEffectApply.get(uuid);
        if (lastApplyTime != null && System.currentTimeMillis() < lastApplyTime + COOLDOWN) return;

        lastEffectApply.put(uuid, System.currentTimeMillis());
        player.addPotionEffect(POISON);
    }
}
