package me.Eggses.dungeons.dungeon.events.extra;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PoisonTick {

    private static final PotionEffect POISON = new PotionEffect(
            PotionEffectType.POISON,
            7 * 20,
            1,
            false,
            true,
            true
    );

    private static final long COOLDOWN = 5000;

    private static final double DAMAGE_PER_APPLY = 4.0;

    private final Map<UUID, Long> lastEffectApply = new HashMap<>();

    public void applyDamageIfApplicable(Player player) {

        UUID uuid = player.getUniqueId();

        Long lastApplyTime = lastEffectApply.get(uuid);
        if (lastApplyTime != null && System.currentTimeMillis() < lastApplyTime + COOLDOWN) return;

        lastEffectApply.put(uuid, System.currentTimeMillis());
        player.addPotionEffect(POISON);
        player.damage(DAMAGE_PER_APPLY);
    }
}
