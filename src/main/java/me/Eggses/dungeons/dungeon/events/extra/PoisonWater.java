package me.Eggses.dungeons.dungeon.events.extra;

import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.event.player.PlayerMoveEvent;

public class PoisonWater implements EventBehaviour<PlayerMoveEvent> {

    private final PoisonTick poisonTick = new PoisonTick(5, 4000);

    @Override
    public void handleEvent(PlayerMoveEvent event, EventContext eventContext) {
        if (event.getTo().getBlock().getType() != Material.WATER) return;
        if (event.getPlayer().getVehicle() instanceof Boat) return;
        poisonTick.applyDamageIfApplicable(event.getPlayer());
    }
}
