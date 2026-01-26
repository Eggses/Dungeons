package me.Eggses.dungeons.dungeon.bosses.manager;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.eventhandler.EventDefinition;
import me.Eggses.dungeons.tasks.Task;
import me.Eggses.dungeons.tasks.TaskContext;
import me.Eggses.dungeons.utility.exceptions.PhaseAlreadyStartedException;

import java.util.ArrayList;
import java.util.List;

public class Phase {

    private final double healthPercentageToStartPhase;
    private final List<EventDefinition<?>> permanentEventsToAdd;
    private final List<EventDefinition<?>> phaseEvents;
    private final List<Task<Boss>> permanentTasksToAdd;
    private final Rotation phaseRotation;

    private boolean phaseStarted = false;

    private Phase(PhaseBuilder phaseBuilder) {
        this.healthPercentageToStartPhase = phaseBuilder.healthPercentageToStartPhase;
        this.permanentEventsToAdd = phaseBuilder.permanentEventsToAdd;
        this.phaseEvents = phaseBuilder.phaseEvents;
        this.permanentTasksToAdd = phaseBuilder.oneOffTasksToAdd;
        this.phaseRotation = new Rotation(phaseBuilder.rotationSteps);
    }

    public void startPhase(TaskContext<Boss> phaseTaskContext, Boss boss) {
        if (phaseStarted) throw new PhaseAlreadyStartedException();
        phaseStarted = true;

        permanentEventsToAdd.forEach(boss::addPermanentEvent);
        phaseEvents.forEach(boss::addPhaseEvent);

        permanentTasksToAdd.forEach(boss::addOneOffTask);
        phaseRotation.start(phaseTaskContext);
    }

    public void endPhase(Boss boss) {
        phaseRotation.end();
        boss.cleanUpOnPhaseEnd();
    }

    public double getHealthPercentageToStartPhase() {
        return healthPercentageToStartPhase;
    }

    public static class PhaseBuilder {

        private final double healthPercentageToStartPhase;
        private final List<EventDefinition<?>> permanentEventsToAdd = new ArrayList<>();
        private final List<EventDefinition<?>> phaseEvents = new ArrayList<>();
        private final List<Task<Boss>> oneOffTasksToAdd = new ArrayList<>();
        private final List<Rotation.RotationStep> rotationSteps = new ArrayList<>();

        public PhaseBuilder(double healthPercentageToStartPhase) {
            this.healthPercentageToStartPhase = healthPercentageToStartPhase;
        }

        public PhaseBuilder addPermanentEvent(EventDefinition<?> eventDefinition) {
            permanentEventsToAdd.add(eventDefinition);
            return this;
        }

        public PhaseBuilder addPhaseEvent(EventDefinition<?> eventDefinition) {
            phaseEvents.add(eventDefinition);
            return this;
        }

        public PhaseBuilder addOneOffTask(Task<Boss> task) {
            oneOffTasksToAdd.add(task);
            return this;
        }

        public PhaseBuilder addRotationStep(Rotation.RotationStep rotationStep) {
            rotationSteps.add(rotationStep);
            return this;
        }

        public PhaseBuilder addRotationStepList(List<Rotation.RotationStep> rotationSteps) {
            this.rotationSteps.addAll(rotationSteps);
            return this;
        }

        public Phase build() {
            return new Phase(this);
        }
    }
}
