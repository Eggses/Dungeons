package me.Eggses.dungeons.dungeon.lifecycle;

public enum DungeonType {

    MALIGNANT_MARSH("malignant_marsh");

    private final String templateName;

    DungeonType(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }
}

