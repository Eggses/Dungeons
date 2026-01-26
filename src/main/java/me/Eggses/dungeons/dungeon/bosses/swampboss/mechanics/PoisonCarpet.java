package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.dungeon.events.extra.PoisonTick;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.event.player.PlayerMoveEvent;

public class PoisonCarpet implements EventBehaviour<PlayerMoveEvent> {

    private final PoisonTick poisonTick = new PoisonTick(8, 2000);
    private boolean apply = true;

    @Override
    public void handleEvent(PlayerMoveEvent event, EventContext eventContext) {
        if (!apply) return;
        poisonTick.applyDamageIfApplicable(event.getPlayer());
    }

    public void setApply(boolean apply) {
        this.apply = apply;
    }
}
