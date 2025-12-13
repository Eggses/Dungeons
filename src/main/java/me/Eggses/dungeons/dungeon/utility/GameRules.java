package me.Eggses.dungeons.dungeon.utility;

import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;

public class GameRules {

    private final World dungeonWorld;

    public GameRules(World dungeonWorld) {
        this.dungeonWorld = dungeonWorld;
    }

    public void applyRules() {

        // dungeonWorld.setViewDistance();
        dungeonWorld.setDifficulty(Difficulty.HARD);

        dungeonWorld.setGameRule(GameRule.COMMAND_BLOCKS_ENABLED, true);
        dungeonWorld.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, true);
        dungeonWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
        dungeonWorld.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
        dungeonWorld.setGameRule(GameRule.DISABLE_RAIDS, true);
        dungeonWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        dungeonWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        dungeonWorld.setGameRule(GameRule.DO_FIRE_TICK, false);
        dungeonWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        dungeonWorld.setGameRule(GameRule.DO_MOB_LOOT, false);
        dungeonWorld.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        dungeonWorld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        dungeonWorld.setGameRule(GameRule.MOB_GRIEFING, false);
        dungeonWorld.setGameRule(GameRule.DO_VINES_SPREAD, false);
        dungeonWorld.setGameRule(GameRule.SNOW_ACCUMULATION_HEIGHT, 0);
        dungeonWorld.setGameRule(GameRule.UNIVERSAL_ANGER, true);
        dungeonWorld.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
        dungeonWorld.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        dungeonWorld.setGameRule(GameRule.PROJECTILES_CAN_BREAK_BLOCKS, false);
        dungeonWorld.setGameRule(GameRule.FORGIVE_DEAD_PLAYERS, false);
    }
}