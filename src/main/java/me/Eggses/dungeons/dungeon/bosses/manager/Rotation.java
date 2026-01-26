package me.Eggses.dungeons.dungeon.bosses.manager;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.tasks.Task;
import me.Eggses.dungeons.tasks.TaskContext;
import me.Eggses.dungeons.utility.exceptions.MechanicRotationAlreadyStartedException;

import java.util.List;

public class Rotation {

    private final List<RotationStep> steps;
    private int index = 0;
    private boolean cancelled = false;

    private boolean started = false;

    public Rotation(List<RotationStep> steps) {
        this.steps = steps;
    }

    public void start(TaskContext<Boss> taskContext) {
        if (started) throw new MechanicRotationAlreadyStartedException();
        started = true;
        if (steps.isEmpty()) return;
        scheduleNext(taskContext);
    }

    public void end() {
        this.cancelled = true;
    }

    private void scheduleNext(TaskContext<Boss> taskContext) {

        RotationStep rotationStep = steps.get(index);

        taskContext.runTaskLaterAndRemove(() -> {

            if (cancelled) return;

            rotationStep.task.runTask(taskContext);

            index++;
            index = index % steps.size();

            scheduleNext(taskContext);

        }, rotationStep.after);
    }

    public record RotationStep(long after, Task<Boss> task) {}
}
