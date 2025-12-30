package me.Eggses.dungeons.particles;

import org.bukkit.Location;
import org.bukkit.Particle;

public class OrbitParticleEffect {

    private final double radius;
    private final Particle particle;
    private final int totalPoints;
    private final double startRad;
    private final double stepRad;
    private final double yStep;

    private int index = 0;

    public OrbitParticleEffect(double radius,
                               double startingAngleDegree,
                               double endingAngleDegree,
                               Particle particle,
                               int countOfPointsInBetween,
                               double maxY) {

        this.radius = radius;
        this.particle = particle;
        this.totalPoints = countOfPointsInBetween + 2;

        double start = ((startingAngleDegree % 360) + 360) % 360;
        double end = ((endingAngleDegree % 360) + 360) % 360;

        double clockwiseDelta = (start - end + 360) % 360;

        this.startRad = Math.toRadians(start);
        this.stepRad = Math.toRadians(-(clockwiseDelta / (totalPoints - 1)));
        this.yStep = maxY / (totalPoints - 1);
    }

    public void spawnParticle(Location center) {

        double radians = startRad + stepRad * index;

        double x = center.getX() + radius * Math.cos(radians);
        double y = center.getY() + yStep * index;
        double z = center.getZ() + radius * Math.sin(radians);

        center.getWorld().spawnParticle(
                particle,
                x, y, z,
                1,
                0, 0, 0,
                0
        );
        index = (index + 1) % totalPoints;
    }
}