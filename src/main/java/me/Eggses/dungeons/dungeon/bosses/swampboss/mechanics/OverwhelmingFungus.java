package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.tasks.Task;
import me.Eggses.dungeons.tasks.TaskProvider;
import me.Eggses.dungeons.utility.sound.DungeonSound;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;

public class OverwhelmingFungus implements TaskProvider<Boss> {

    private static final long FUNGUS_AFTER = 20L * 6L;
    private static final double DAMAGE = 200.0;
    private static final long POISON_MOSS_AFTER = 20L * 2L;

    private final MossController mossController;
    private final SoundPlayer soundPlayer;

    public OverwhelmingFungus(MossController mossController, SoundPlayer soundPlayer) {
        this.mossController = mossController;
        this.soundPlayer = soundPlayer;
    }

    @Override
    public Task<Boss> getTask() {

        return ctx -> {

            Boss boss = ctx.getOwner();
            Component message = boss.createMessage("<dark_red>Overwhelming Fungus!");
            Sound charging = soundPlayer.createSound(DungeonSound.GUARDIAN_AMBIENT.getMinecraftSound());
            Sound damageSound = soundPlayer.createSound(DungeonSound.GENERIC_EXPLODE.getMinecraftSound());
            DamageSource damageSource = DamageSource.builder(DamageType.EXPLOSION).build();
            mossController.setApply(false);

            for (Player player : boss.getPlayersInFight()) {
                player.sendMessage(message);
                soundPlayer.playSound(charging, player);
            }

            ctx.runTaskLaterAndRemove(() -> {
                for (Player player : boss.getPlayersInFight()) {
                    if (player.getLocation().getBlock().getType() == Material.MOSS_CARPET) continue;

                    player.damage(DAMAGE, damageSource);
                    soundPlayer.playSound(damageSound, player);
                }
                ctx.runTaskLaterAndRemove(() -> mossController.setApply(true), POISON_MOSS_AFTER);
            }, FUNGUS_AFTER);
        };
    }
}
