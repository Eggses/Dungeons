package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.particles.NormalEffectStyle;
import me.Eggses.dungeons.utility.sound.DungeonSound;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Particle;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityCombustByBlockEvent;

public class FireBurstExplosion implements EventBehaviour<EntityCombustByBlockEvent> {

    private static final long COOLDOWN_MS = 50000L;

    private long lastRun = Long.MIN_VALUE;

    private final Harvest harvest;
    private final MossController mossController;
    private final SoundPlayer soundPlayer;

    private static final int BURSTS = 3;
    private static final long BURST_SPACE_TICKS = 50L;

    private final NormalEffectStyle fireParticles = new NormalEffectStyle(
            null, //TODO: fix this.
            Particle.FLAME,
            200
    );

    public FireBurstExplosion(Harvest harvest, MossController mossController, SoundPlayer soundPlayer) {
        this.harvest = harvest;
        this.mossController = mossController;
        this.soundPlayer = soundPlayer;
    }

    @Override
    public void handleEvent(EntityCombustByBlockEvent event, EventContext eventContext) {

        long currentTime = System.currentTimeMillis();
        if (currentTime < COOLDOWN_MS + lastRun) return;
        lastRun = currentTime;

        DungeonEntity dungeonEntity = eventContext.getOwnerOfBehaviour();
        if (!(dungeonEntity instanceof Boss boss)) return;

        Entity entity = dungeonEntity.getEntity();
        entity.setFireTicks(20 * 20);

        for (int i = 0; i < BURSTS; i++) {
            long delay = i * BURST_SPACE_TICKS;

            boss.addOneOffTask(ctx ->
                    ctx.runTaskLaterAndRemove(() -> fireBurstAtBoss(boss), delay)
            );
        }
        harvest.reset();
        mossController.removeAllMoss();
    }

    private void fireBurstAtBoss(Boss boss) {

        DamageSource damageSource = DamageSource.builder(DamageType.IN_FIRE).build();
        Sound sound = soundPlayer.createSound(DungeonSound.ILLUSIONER_PREPARE_BLINDNESS.getMinecraftSound());

        fireParticles.spawn();

        final double damagePerBurst = 25.0;

        boss.getPlayersInFight().forEach(player -> {
            soundPlayer.playSound(sound, player);
            player.damage(damagePerBurst, damageSource);
            player.setFireTicks(20 * 5);
        });
    }
}
