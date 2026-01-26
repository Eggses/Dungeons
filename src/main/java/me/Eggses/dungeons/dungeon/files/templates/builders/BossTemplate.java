package me.Eggses.dungeons.dungeon.files.templates.builders;

import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.RotationPosition;

import java.util.List;

public record BossTemplate(String bossPreset,
                           Region entryRegion,
                           RotationPosition playerSpawningRotationPosition,
                           List<String> onBossDefeat) { }
