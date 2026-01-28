package me.Eggses.dungeons.dungeon.bosses.mechanics;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

public class BossCustomHealth implements EventBehaviour<EntityDamageEvent> {

    @Override
    public void handleEvent(EntityDamageEvent event, EventContext eventContext) {

        DungeonEntity owner = eventContext.getOwnerOfBehaviour();
        if (!(owner instanceof Boss boss)) return;

        Entity entity = event.getEntity();
        if (!DungeonEntity.equalsIgnoreNull(owner, entity)) return;

        boss.takeDamage(event.getFinalDamage());
        event.setDamage(0.0);
        boss.tryEndBossFight();
    }
}
