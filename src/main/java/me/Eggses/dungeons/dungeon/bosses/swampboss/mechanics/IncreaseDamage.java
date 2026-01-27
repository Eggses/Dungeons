package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class IncreaseDamage implements EventBehaviour<EntityDamageByEntityEvent> {

    private static final double HARVEST_PERCENT_PER_STACK = 1.5;

    private final Harvest harvest;

    public IncreaseDamage(Harvest harvest) {
        this.harvest = harvest;
    }

    @Override
    public void handleEvent(EntityDamageByEntityEvent event, EventContext eventContext) {

        LivingEntity attacker = eventContext.getTrueAttacker();
        DungeonEntity owner = eventContext.getOwnerOfBehaviour();
        if (!DungeonEntity.equalsIgnoreNull(owner, attacker)) return;

        if (!(event.getEntity() instanceof Player)) return;

        double finalDamage = event.getDamage() * calculateMultiplier();
        event.setDamage(finalDamage);
    }

    /**
     * Calculates the multiplier for damage based on the current stack count.
     * <p>
     * With 20 stacks, and a stack percent bonus of 0.5
     * 100 * x = 110
     * x = 1.1
     * If this is with 20 stacks, 20 * 0.5 = 10
     * = 1 + (10/100)
     * @return the Multiplier.
     */
    private double calculateMultiplier() {
        double bonus = harvest.getStackCount() * HARVEST_PERCENT_PER_STACK;
        return 1 + (bonus / 100.0);
    }
}
