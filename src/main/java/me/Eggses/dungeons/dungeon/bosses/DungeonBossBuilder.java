package me.Eggses.dungeons.dungeon.bosses;

import me.Eggses.dungeons.dungeon.bosses.manager.Phase;
import me.Eggses.dungeons.entities.mobs.MobBuilder;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class DungeonBossBuilder {

    private Component bossName;
    private BossBarController.Style style;
    private String colourScheme;
    private double health;
    private List<Phase> phases = new ArrayList<>();
    private MobBuilder mobBuilder;

    public DungeonBossBuilder() {
    }

    public DungeonBossBuilder bossName(Component bossName) {
        this.bossName = bossName;
        return this;
    }

    public DungeonBossBuilder style(BossBarController.Style style) {
        this.style = style;
        return this;
    }

    public DungeonBossBuilder health(double health) {
        this.health = health;
        return this;
    }

    public DungeonBossBuilder phases(List<Phase> phases) {
        this.phases = phases;
        return this;
    }

    public DungeonBossBuilder addPhase(Phase phase) {
        this.phases.add(phase);
        return this;
    }

    public DungeonBossBuilder colourScheme(String colourScheme) {
        this.colourScheme = colourScheme;
        return this;
    }

    public DungeonBossBuilder mobBuilder(MobBuilder mobBuilder) {
        this.mobBuilder = mobBuilder;
        return this;
    }

    public Component getBossName() {
        return bossName;
    }

    public BossBarController.Style getStyle() {
        return style;
    }

    public String getColourScheme() {
        return colourScheme;
    }

    public double getHealth() {
        return health;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public MobBuilder getMobBuilder() {
        return mobBuilder;
    }
}



