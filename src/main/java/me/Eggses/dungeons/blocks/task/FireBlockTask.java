package me.Eggses.dungeons.blocks.task;

import me.Eggses.dungeons.dungeon.regions.WorldPosition;
import me.Eggses.dungeons.dungeon.regions.WorldRegion;
import me.Eggses.dungeons.particles.NormalEffectStyle;
import me.Eggses.dungeons.tasks.Task;
import me.Eggses.dungeons.tasks.TaskProvider;
import org.bukkit.Particle;

public class FireBlockTask implements TaskProvider<WorldPosition> {

    private static final long DELAY_TICKS = 0;
    private static final long REPEATING_PERIOD_TICKS = 1;

    @Override
    public Task<WorldPosition> getTask() {

        return taskContext -> {
            WorldRegion bounds = taskContext.getOwner().toWorldRegion();
            NormalEffectStyle normalEffectStyle = new NormalEffectStyle(bounds, Particle.ELECTRIC_SPARK, 10);
            taskContext.runTaskRepeatedly(normalEffectStyle::spawn, DELAY_TICKS, REPEATING_PERIOD_TICKS);
        };
    }
}
