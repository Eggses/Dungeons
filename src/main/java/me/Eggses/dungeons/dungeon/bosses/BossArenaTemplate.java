package me.Eggses.dungeons.dungeon.bosses;

import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.RotationPosition;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record BossArenaTemplate(Supplier<DungeonBossBuilder> bossBuilderSupplier,
                                Region entryRegion,
                                RotationPosition playerSpawningRotationPosition,
                                Consumer<DungeonContext> onBossDefeat) { }