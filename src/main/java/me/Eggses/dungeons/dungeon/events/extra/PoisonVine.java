package me.Eggses.dungeons.dungeon.events.extra;

import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class PoisonVine implements EventBehaviour<PlayerMoveEvent>  {

    private final PoisonTick poisonTick = new PoisonTick(2, 5000);

    @Override
    public void handleEvent(PlayerMoveEvent event, EventContext eventContext) {
        Player player = event.getPlayer();
        if (!player.isClimbing()) return;
        if (player.getLocation().getBlock().getType() != Material.VINE) return;

        poisonTick.applyDamageIfApplicable(player);
    }
}
