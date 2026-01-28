package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.tasks.Task;
import me.Eggses.dungeons.tasks.TaskProvider;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class LightningFire implements TaskProvider<Boss> {

    private static final List<Position> FIRE_SPAWNING_LOCATIONS = List.of(
            new Position(1, 2, 3),
            new Position(4, 2, 3),
            new Position(7, 2, 3)
    );

    private static final int NUMBER_OF_STRIKES = 2;
    private static final long FIRE_LIFETIME_TICKS = 10L * 20L;
    private static final long REPEATING_PERIOD_TICKS = 26L * 20L;

    private static final int[][] FIRE_OFFSETS = {
            { 0, 0, 0 },
            { 1, 0, 0 },
            { -1, 0, 0 },
            { 0, 0, 1 },
            { 0, 0, -1 }
    };

    private final LightningFireController lightningFireController;
    private World world;

    public LightningFire(LightningFireController lightningFireController) {
        this.lightningFireController = lightningFireController;
    }

    @Override
    public Task<Boss> getTask() {
        return taskContext -> {
            Boss boss = taskContext.getOwner();
            if (world == null) {
                world = boss.getBossWorld();
            }

            if (FIRE_SPAWNING_LOCATIONS.isEmpty()) return;

            taskContext.runTaskRepeatedly(() -> {

                for (int i = 0; i < NUMBER_OF_STRIKES; i++) {

                    Location strikeLocation = pickLightningStrikeLocationAvoidingPlayers();

                    world.strikeLightningEffect(strikeLocation);

                    for (int[] offset : FIRE_OFFSETS) {
                        Location fireSpawningLocation = strikeLocation.clone().add(
                                offset[0],
                                offset[1],
                                offset[2]
                        );
                        lightningFireController.placeFire(fireSpawningLocation.getBlock());
                    }
                    taskContext.runTaskLaterAndRemove(lightningFireController::removeAllFire, FIRE_LIFETIME_TICKS);
                }
            }, 0L, REPEATING_PERIOD_TICKS);
        };
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
