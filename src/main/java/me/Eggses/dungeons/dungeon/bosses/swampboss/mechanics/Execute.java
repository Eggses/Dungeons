package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.utility.sound.DungeonSound;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Execute implements EventBehaviour<EntityDamageByEntityEvent> {

    private final SoundPlayer soundPlayer;

    public Execute(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    @Override
    public void handleEvent(EntityDamageByEntityEvent event, EventContext eventContext) {

        LivingEntity attacker = eventContext.getTrueAttacker();
        DungeonEntity owner = eventContext.getOwnerOfBehaviour();
        if (!DungeonEntity.equalsIgnoreNull(owner, attacker)) return;

        if (!(event.getEntity() instanceof Player player)) return;

        if (player.getHealth() <= 2.0) {
            Sound sound = soundPlayer.createSound(DungeonSound.GRINDSTONE_USE.getMinecraftSound());
            soundPlayer.playSound(sound, player);
            event.setDamage(10000);
        }
    }
}
