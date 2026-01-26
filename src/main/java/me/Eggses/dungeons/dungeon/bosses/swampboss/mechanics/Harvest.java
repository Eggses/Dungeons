package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

public class Harvest {

    private int stackCount = 0;

    public Harvest() {}

    public void increment() {
        stackCount++;
    }

    public void reset() {
        stackCount = 0;
    }

    public int getStackCount() {
        return stackCount;
    }
}
