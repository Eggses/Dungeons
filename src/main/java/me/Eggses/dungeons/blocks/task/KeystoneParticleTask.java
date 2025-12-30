package me.Eggses.dungeons.blocks.task;

import me.Eggses.dungeons.particles.OrbitParticleEffect;
import me.Eggses.dungeons.tasks.definitions.RepeatingTask;
import me.Eggses.dungeons.tasks.definitions.Task;
import me.Eggses.dungeons.tasks.definitions.TaskDefinition;
import me.Eggses.dungeons.tasks.running.TaskManager;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.List;
import java.util.function.BiConsumer;

public class KeystoneParticleTask implements Task<Location> {

    private static final long DELAY_TICKS = 0;
    private static final long REPEATING_PERIOD_TICKS = 2;

    private final RepeatingTask<Location> particleTask;

    public KeystoneParticleTask() {

        double radius = 0.73;
        List<OrbitParticleEffect> particleEffectList = getOrbitParticleEffects(radius);

        BiConsumer<Location, TaskManager> task = (location, taskManager) -> {
            Location center = location.clone().add(0.5, 0, 0.5);
            particleEffectList.forEach(ope -> ope.spawnParticle(center));
        };
        particleTask = new RepeatingTask<>(task, DELAY_TICKS, REPEATING_PERIOD_TICKS);
    }

    private List<OrbitParticleEffect> getOrbitParticleEffects(double radius) {

        int countOfPointsInBetween = 10;
        double maxY = 2.5;
        Particle particle = Particle.PORTAL;

        return List.of(
                new OrbitParticleEffect(radius, 135, 0, particle, countOfPointsInBetween, maxY),
                new OrbitParticleEffect(radius, 45, 270, particle, countOfPointsInBetween, maxY),
                new OrbitParticleEffect(radius, 315, 180, particle, countOfPointsInBetween, maxY),
                new OrbitParticleEffect(radius, 225, 90, particle, countOfPointsInBetween, maxY)
        );
    }

    @Override
    public TaskDefinition<Location> getTask() {
        return particleTask;
    }
}
