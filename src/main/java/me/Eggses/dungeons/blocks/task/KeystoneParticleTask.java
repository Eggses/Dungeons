package me.Eggses.dungeons.blocks.task;

import me.Eggses.dungeons.particles.OrbitParticleEffect;
import me.Eggses.dungeons.tasks.TaskContext;
import me.Eggses.dungeons.tasks.TaskContextProvider;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.List;
import java.util.function.Consumer;

public class KeystoneParticleTask implements TaskContextProvider<Location> {

    private static final long DELAY_TICKS = 0;
    private static final long REPEATING_PERIOD_TICKS = 1;

    public KeystoneParticleTask() {
    }

    @Override
    public Consumer<TaskContext<Location>> getTaskContext() {

        double radius = 0.73;
        List<OrbitParticleEffect> particleEffectList = getOrbitParticleEffects(radius);

        return (taskContext -> {
            Location center = taskContext.getOwner().clone().add(0.5, 0, 0.5);

            taskContext.runTaskRepeatedly(
                    () -> particleEffectList.forEach(ope -> ope.spawnParticle(center))
                    , DELAY_TICKS, REPEATING_PERIOD_TICKS
            );
        });
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
}
