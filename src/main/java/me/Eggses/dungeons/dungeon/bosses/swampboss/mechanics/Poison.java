package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.tasks.Task;
import me.Eggses.dungeons.tasks.TaskProvider;
import me.Eggses.dungeons.utility.sound.DungeonSound;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import me.Eggses.dungeons.utility.text.MessageCreator;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Poison implements TaskProvider<Boss> {

      private static final PotionEffect POISON = new PotionEffect(
              PotionEffectType.POISON,
              20 * 5,
              2,
              false,
              true,
              true
      );

    private final MessageCreator messageCreator;
    private final SoundPlayer soundPlayer;

    public Poison(MessageCreator messageCreator, SoundPlayer soundPlayer) {
        this.messageCreator = messageCreator;
        this.soundPlayer = soundPlayer;
    }

    @Override
    public Task<Boss> getTask() {
        return ctx -> {

            Boss boss = ctx.getOwner();
            Component message = messageCreator.createMessage("<green>Rot blooms!");
            Sound sound = soundPlayer.createSound(DungeonSound.MUD_STEP.getMinecraftSound());

            for (Player player : boss.getPlayersInFight()) {
                soundPlayer.playSound(sound, player);
                player.sendMessage(message);
                player.addPotionEffect(POISON);
            }
        };
    }
}
