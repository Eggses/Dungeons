package me.Eggses.dungeons.particles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

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

        // Ensure angles are 0 to 360
        double start = ((startingAngleDegree % 360) + 360) % 360;
        double end = ((endingAngleDegree % 360) + 360) % 360;

        // Finds total distance of the arc.
        double distanceNormalised = (start - end + 360) % 360;

        this.startRad = Math.toRadians(start); // Starting position
        this.stepRad = Math.toRadians(-(distanceNormalised / (totalPoints - 1)));
        /*
        Size of jump to each point.
        N points means N-1 spaces between the points. Size of each gap is distance / count of gaps, So distance / N - 1
        Then *-1 as we move clockwise and Angles decrease clockwise.
         */
        this.yStep = maxY / (totalPoints - 1); // Y is treated like the other stepping.
    }

    public void spawnParticle(Location center) {

        final double viewDistSq = 64.0 * 64.0;

        var players = center.getWorld().getPlayers();
        boolean anyNearby = false;
        for (Player p : players) {
            if (p.getLocation().distanceSquared(center) <= viewDistSq) {
                anyNearby = true;
                break;
            }
        }
        if (!anyNearby) return;

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