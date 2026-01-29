package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.tasks.Task;
import me.Eggses.dungeons.tasks.TaskProvider;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LightningFire implements TaskProvider<Boss> {

    private static final List<Position> FIRE_SPAWNING_LOCATIONS = List.of(
            new Position(-1176, 67, 88),
            new Position(-1185, 67, 86),
            new Position(-1173, 67, 94),
            new Position(-1169, 67, 100),
            new Position(-1176, 67, 102),
            new Position(-1173, 67, 109),
            new Position(-1184, 67, 106),
            new Position(-1186, 67, 112),
            new Position(-1194, 67, 105),
            new Position(-1189, 67, 96),
            new Position(-1195, 67, 93)
    );

    private static final List<int[]> FIRE_OFFSETS = List.of(
            new int[]{-1, 0, -1},
            new int[]{0, 0, -1},
            new int[]{1, 0, -1},
            new int[]{-1, 0,  0},
            new int[]{1, 0,  0},
            new int[]{-1, 0,  1},
            new int[]{0, 0,  1},
            new int[]{1, 0,  1}
    );

    private static final int NUMBER_OF_STRIKES = 2;

    private static final int MINIMUM_FIRE = 3;
    private static final int MAXIMUM_FIRE = 7;

    private static final long MIN_FIRE_LIFETIME_TICKS = 30L * 20L;
    private static final long MAX_FIRE_LIFETIME_TICKS = 50L * 20L;
    private static final long REPEATING_PERIOD_TICKS = 60L * 20L;

    private final LightningFireController lightningFireController;
    private final Harvest harvest;
    private final MossController mossController;
    private final SoundPlayer soundPlayer;

    private FireBurstExplosion fireBurstExplosion;
    private World world;

    public LightningFire(LightningFireController lightningFireController,
                         Harvest harvest,
                         MossController mossController,
                         SoundPlayer soundPlayer) {

        this.lightningFireController = lightningFireController;
        this.harvest = harvest;
        this.mossController = mossController;
        this.soundPlayer = soundPlayer;
    }

    @Override
    public Task<Boss> getTask() {
        return taskContext -> {
            Boss boss = taskContext.getOwner();
            if (world == null || fireBurstExplosion == null) {
                world = boss.getBossWorld();
                fireBurstExplosion = new FireBurstExplosion(boss, harvest, mossController, soundPlayer);
            }

            if (FIRE_SPAWNING_LOCATIONS.isEmpty()) return;

            taskContext.runTaskRepeatedly(() -> {

                for (int i = 0; i < NUMBER_OF_STRIKES; i++) {

                    Location strikeLocation = pickLightningStrikeLocationAvoidingPlayers();
                    world.strikeLightningEffect(strikeLocation);

                    int[][] fireOffsets = getFireOffsets();

                    long fireLifeTime = ThreadLocalRandom.current().nextLong(
                            MIN_FIRE_LIFETIME_TICKS,
                            MAX_FIRE_LIFETIME_TICKS + 1
                    );

                    for (int[] offset : fireOffsets) {

                        Block fire = world.getBlockAt(
                                strikeLocation.getBlockX() + offset[0],
                                strikeLocation.getBlockY() + offset[1],
                                strikeLocation.getBlockZ() + offset[2]
                        );

                        lightningFireController.placeFire(fire, fireBurstExplosion);

                        taskContext.runTaskLaterAndRemove(()
                                -> lightningFireController.removeFire(fire), fireLifeTime
                        );
                    }
                }
            }, 0L, REPEATING_PERIOD_TICKS);
        };
    }

    private int[][] getFireOffsets() {

        int fireToSpawn = ThreadLocalRandom.current().nextInt(MINIMUM_FIRE, MAXIMUM_FIRE + 1);
        fireToSpawn = Math.min(fireToSpawn, FIRE_OFFSETS.size());
        int[][] newOffsets = new int[fireToSpawn][3];

        List<int[]> fireOffsets = new ArrayList<>(FIRE_OFFSETS);
        Collections.shuffle(fireOffsets);

        for (int i = 0; i < newOffsets.length; i++) {
            newOffsets[i] = fireOffsets.get(i);
        }

        return newOffsets;
    }

    private Location pickLightningStrikeLocationAvoidingPlayers() {

        int size = FIRE_SPAWNING_LOCATIONS.size();
        List<Integer> indexes = new ArrayList<>(size);
        for (int i = 0; i < size; i++) indexes.add(i);
        Collections.shuffle(indexes);

        for (int index : indexes) {
            Location location = FIRE_SPAWNING_LOCATIONS.get(index).toLocation(world);

            boolean containsPlayer = false;
            for (Entity entity : location.getNearbyEntities(3, 3, 3)) {
                if (entity instanceof Player) {
                    containsPlayer = true;
                    break;
                }
            }
            if (!containsPlayer) {
                return location;
            }
        }
        Position fallBack = FIRE_SPAWNING_LOCATIONS.get(indexes.getFirst());
        return fallBack.toLocation(world);
    }
}
