package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class InfectedHit implements EventBehaviour<EntityDamageByEntityEvent> {

    private static final double APPLY_CHANCE = 0.60;

    private static final List<PotionEffect> POTION_EFFECTS = List.of(
            new PotionEffect(
                    PotionEffectType.POISON,
                    20 * 5,
                    0,
                    false,
                    true,
                    true
            ),
            new PotionEffect(
                    PotionEffectType.SLOWNESS,
                    20 * 5,
                    0,
                    false,
                    true,
                    true
            )
    );

    @Override
    public void handleEvent(EntityDamageByEntityEvent event, EventContext eventContext) {

        LivingEntity attacker = eventContext.getTrueAttacker();
        DungeonEntity owner = eventContext.getOwnerOfBehaviour();
        if (!DungeonEntity.equalsIgnoreNull(owner, attacker)) return;

        if (!(event.getEntity() instanceof Player player)) return;

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        if (rng.nextDouble() >= APPLY_CHANCE) return;

        PotionEffect effect = POTION_EFFECTS.get(rng.nextInt(POTION_EFFECTS.size()));
        player.addPotionEffect(effect);
    }
}
