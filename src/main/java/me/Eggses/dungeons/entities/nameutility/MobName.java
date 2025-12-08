package me.Eggses.dungeons.entities.nameutility;

public class MobName {

    private final String name;
    private final boolean overrideName;

    public MobName(String name, boolean overrideName) {
        this.name = name;
        this.overrideName = overrideName;
    }

    public MobName() {
        this("", false);
    }

    public String getName() {
        return name;
    }

    public boolean isOverrideName() {
        return overrideName;
    }
}
