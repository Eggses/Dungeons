package me.Eggses.dungeons.dungeon.items.management;

import java.util.Locale;

public enum DungeonTool {
    AXE("dungeon_axe"),
    ;

    private final String configurationSectionName;

    DungeonTool(String configurationSectionName) {
        this.configurationSectionName = configurationSectionName;
    }

    public String getConfigurationSectionName() {
        return configurationSectionName;
    }

    public static DungeonTool getType(String type) {
        try {
            return DungeonTool.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
