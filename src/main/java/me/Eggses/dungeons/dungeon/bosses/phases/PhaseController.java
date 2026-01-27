package me.Eggses.dungeons.dungeon.bosses.phases;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.tasks.TaskContext;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PhaseController {

    private final Boss boss;
    private final Map<Double, Phase> phases = new TreeMap<>(Comparator.reverseOrder());
    private Phase activePhase;

    public PhaseController(Boss boss, List<Phase> phases) {
        this.boss = boss;
        for (Phase phase : phases) {
            this.phases.put(phase.getHealthPercentageToStartPhase(), phase);
        }
    }

    public void attemptToChangePhase(double remainingHealthPercentage, TaskContext<Boss> phaseTaskContext) {

        Phase phase = null;

        for (Map.Entry<Double, Phase> entry : phases.entrySet()) {
            if (remainingHealthPercentage > entry.getKey()) break;
            phase = entry.getValue();
        }

        if (phase != null && phase != activePhase) {
            if (activePhase != null) {
                activePhase.endPhase(boss);
            }
            activePhase = phase;
            activePhase.startPhase(phaseTaskContext, boss);
        }
    }
}
