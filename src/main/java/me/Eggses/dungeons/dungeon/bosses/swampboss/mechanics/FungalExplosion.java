package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.tasks.Task;
import me.Eggses.dungeons.tasks.TaskProvider;
import me.Eggses.dungeons.utility.sound.DungeonSound;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import me.Eggses.dungeons.utility.text.MessageCreator;
import net.kyori.adventure.sound.Sound;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class FungalExplosion implements TaskProvider<Boss> {

    private static final int Y_HEIGHT = 67;

    private final MossController mossController;
    private final MessageCreator messageCreator;
    private final SoundPlayer soundPlayer;

    public FungalExplosion(MossController mossController, MessageCreator messageCreator, SoundPlayer soundPlayer) {
        this.mossController = mossController;
        this.messageCreator = messageCreator;
        this.soundPlayer = soundPlayer;
    }

    @Override
    public Task<Boss> getTask() {
        return ctx -> {

            Boss boss = ctx.getOwner();

            final int numberOfTargets = 2;
            final long explosionAfter = 20 * 5;
            final double damage = 10.0;

            DamageSource damageSource = DamageSource.builder(DamageType.EXPLOSION).build();
            Sound sound = soundPlayer.createSound(DungeonSound.GENERIC_EXPLODE.getMinecraftSound());

            List<Player> nearbyPlayers = new ArrayList<>(boss.getPlayersInFight());
            Collections.shuffle(nearbyPlayers);

            int targetsHit = 0;

            for (Player target : nearbyPlayers) {
                if (targetsHit >= numberOfTargets) break;
                if (target.getGameMode() == GameMode.CREATIVE || target.getGameMode() == GameMode.SPECTATOR) continue;
                targetsHit++;
                target.sendMessage(messageCreator.createMessage("<red>You are targeted by Fungal Explosion."));

                ctx.runTaskLaterAndRemove(() -> {
                    if (!boss.isInFight(target)) return;
                    Set<Player> nearbyPlayersToPlayer = target.getNearbyEntities(5, 5, 5)
                            .stream()
                            .filter(entity -> entity instanceof Player)
                            .map(entity -> (Player) entity)
                            .filter(boss::isInFight)
                            .collect(Collectors.toCollection(HashSet::new));

                    nearbyPlayersToPlayer.add(target);

                    nearbyPlayersToPlayer.forEach(player -> {
                        player.damage(damage, damageSource);
                        soundPlayer.playSound(sound, player);
                    });

                    placeMossCarpetShape(target);

                }, explosionAfter);
            }
        };
    }

    private void placeMossCarpetShape(Player player) {

        World world = player.getWorld();
        Location centre = player.getLocation();

        int baseX = centre.getBlockX();
        int baseZ = centre.getBlockZ();

        int[][] offsets = {
                { 0,  0},

                { 0,  1},
                { 0, -1},
                { 1,  0},
                {-1,  0},

                { 1,  1},
                {-1,  1},
                { 1, -1},
                {-1, -1},

                { 0,  2},
                { 0, -2},
                { 2,  0},
                {-2,  0}
        };

        for (int[] offset : offsets) {

            int x = baseX + offset[0];
            int z = baseZ + offset[1];

            Block ground = world.getBlockAt(x, Y_HEIGHT - 1, z);
            Block target = world.getBlockAt(x, Y_HEIGHT, z);

            if (ground.getType().isSolid() && target.getType().isAir()) {
                mossController.placeMoss(target);
            }
        }
    }
}
