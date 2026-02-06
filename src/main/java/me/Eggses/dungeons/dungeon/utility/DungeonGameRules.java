package me.Eggses.dungeons.dungeon.utility;

import org.bukkit.Difficulty;
import org.bukkit.GameRules;
import org.bukkit.World;

public class DungeonGameRules {

    private final World dungeonWorld;

    public DungeonGameRules(World dungeonWorld) {
        this.dungeonWorld = dungeonWorld;
    }


    public void applyRules() {
        applyDifficulty();
        applyGameRules();
    }

    private void applyDifficulty() {
        dungeonWorld.setDifficulty(Difficulty.HARD);
    }

    private void applyGameRules() {
        dungeonWorld.setGameRule(GameRules.COMMAND_BLOCKS_WORK, true);
        dungeonWorld.setGameRule(GameRules.COMMAND_BLOCK_OUTPUT, true);
        dungeonWorld.setGameRule(GameRules.KEEP_INVENTORY, true);
        dungeonWorld.setGameRule(GameRules.SPAWN_WARDENS, false);
        dungeonWorld.setGameRule(GameRules.RAIDS, false);
        dungeonWorld.setGameRule(GameRules.ADVANCE_TIME, false);
        dungeonWorld.setGameRule(GameRules.ADVANCE_WEATHER, false);
        dungeonWorld.setGameRule(GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER, 0);
        dungeonWorld.setGameRule(GameRules.SPAWN_MOBS, false);
        dungeonWorld.setGameRule(GameRules.MOB_DROPS, true);
        dungeonWorld.setGameRule(GameRules.SPAWN_PATROLS, false);
        dungeonWorld.setGameRule(GameRules.SPAWN_WANDERING_TRADERS, false);
        dungeonWorld.setGameRule(GameRules.MOB_GRIEFING, false);
        dungeonWorld.setGameRule(GameRules.SPREAD_VINES, false);
        dungeonWorld.setGameRule(GameRules.MAX_SNOW_ACCUMULATION_HEIGHT, 0);
        dungeonWorld.setGameRule(GameRules.UNIVERSAL_ANGER, true);
        dungeonWorld.setGameRule(GameRules.RANDOM_TICK_SPEED, 0);
        dungeonWorld.setGameRule(GameRules.ENTITY_DROPS, false);
        dungeonWorld.setGameRule(GameRules.PROJECTILES_CAN_BREAK_BLOCKS, false);
        dungeonWorld.setGameRule(GameRules.FORGIVE_DEAD_PLAYERS, false);
    }
}
