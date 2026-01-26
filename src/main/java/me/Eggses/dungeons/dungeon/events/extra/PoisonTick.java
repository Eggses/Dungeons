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

    private final double damagePerApply;
    private final long cooldown;
    private final Map<UUID, Long> lastEffectApply = new HashMap<>();

    public PoisonTick(double damagePerApply, long cooldown) {
        this.damagePerApply = damagePerApply;
        this.cooldown = cooldown;
    }

    public void applyDamageIfApplicable(Player player) {

        UUID uuid = player.getUniqueId();

        Long lastApplyTime = lastEffectApply.get(uuid);
        if (lastApplyTime != null && System.currentTimeMillis() < lastApplyTime +  cooldown) return;

        lastEffectApply.put(uuid, System.currentTimeMillis());
        player.addPotionEffect(POISON);
        player.damage(damagePerApply);
    }
}
