package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.WorldRegion;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.particles.NormalEffectStyle;
import me.Eggses.dungeons.utility.sound.DungeonSound;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Particle;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;

public class FireBurstExplosion implements EventBehaviour<EntityMoveEvent> {

    private static final long COOLDOWN_MS = 10000L;
    private static final int FIRE_TICKS = 20 * 20;

    private static final int BURSTS = 3;
    private static final long BURST_SPACE_TICKS = (long) (20L * 1.5);

    private static final Position POSITION_A = new Position(-1167, 74, 84);
    private static final Position POSITION_B = new Position(-1197, 66, 114);

    private final Boss boss;
    private final Harvest harvest;
    private final MossController mossController;
    private final SoundPlayer soundPlayer;
    private final NormalEffectStyle fireParticles;

    private long lastRanTime = 0L;

    public FireBurstExplosion(Boss boss,
                              Harvest harvest,
                              MossController mossController,
                              SoundPlayer soundPlayer) {

        this.boss = boss;
        this.harvest = harvest;
        this.mossController = mossController;
        this.soundPlayer = soundPlayer;

        var worldRegion = new WorldRegion(boss.getBossWorld(), new Region(POSITION_A, POSITION_B));
        this.fireParticles = new NormalEffectStyle(worldRegion, Particle.FLAME, 1000);
    }

    @Override
    public void handleEvent(EntityMoveEvent event, EventContext eventContext) {

        if (!event.getEntity().getUniqueId().equals(boss.getUUID())) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime < COOLDOWN_MS + lastRanTime) return;
        lastRanTime = currentTime;

        boss.getEntity().setFireTicks(FIRE_TICKS);
        for (int i = 0; i < BURSTS; i++) {
            long delay = i * BURST_SPACE_TICKS;
            boss.addOneOffTask(taskContext
                    -> taskContext.runTaskLaterAndRemove(this::fireBurstAtBoss, delay));
        }
        harvest.reset();
        mossController.removeAllMoss();
    }

    private void fireBurstAtBoss() {
        DamageSource damageSource = DamageSource.builder(DamageType.LAVA).build();
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
