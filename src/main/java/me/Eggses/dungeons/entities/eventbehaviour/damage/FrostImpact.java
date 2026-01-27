package me.Eggses.dungeons.entities.eventbehaviour.damage;

import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FrostImpact implements EventBehaviour<EntityDamageByEntityEvent> {

    private static final int FREEZE_DURATION = 10 * 20;

    @Override
    public void handleEvent(EntityDamageByEntityEvent event, EventContext eventContext) {

        LivingEntity attacker = eventContext.getTrueAttacker();
        DungeonEntity owner = eventContext.getOwnerOfBehaviour();
        if (!DungeonEntity.equalsIgnoreNull(owner, attacker)) return;

        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;

        livingEntity.setFreezeTicks(Math.max(livingEntity.getFreezeTicks(), FREEZE_DURATION));
    }
}