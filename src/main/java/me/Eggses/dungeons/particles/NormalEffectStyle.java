package me.Eggses.dungeons.particles;

import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.WorldRegion;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.concurrent.ThreadLocalRandom;

public class NormalEffectStyle {

    private final World world;
    private final Region region;
    private final Particle particle;
    private final int countOfParticlesToSpawn;

    public NormalEffectStyle(WorldRegion worldRegion, Particle particle, int countOfParticlesToSpawn) {
        this.world = worldRegion.getWorld();
        this.region = worldRegion.getRegion();
        this.particle = particle;
        this.countOfParticlesToSpawn = countOfParticlesToSpawn;
    }

    public void spawn() {

        ThreadLocalRandom rng = ThreadLocalRandom.current();

        for (int i = 0; i < countOfParticlesToSpawn; i++) {
            double x = rng.nextDouble(region.getMinX(), region.getMaxX() + 1);
            double y = rng.nextDouble(region.getMinY(), region.getMaxY() + 1);
            double z = rng.nextDouble(region.getMinZ(), region.getMaxZ() + 1);

            world.spawnParticle(particle, x, y, z, 1, 0, 0, 0, 0);
        }
    }
}
