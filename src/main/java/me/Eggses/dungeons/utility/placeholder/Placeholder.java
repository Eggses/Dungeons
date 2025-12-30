package me.Eggses.dungeons.utility.placeholder;

public enum Placeholder {

    DUNGEON_NAME("%dungeon_name%"),
    OPEN_DURATION("%open_duration%");

    private final String placeholder;

    Placeholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
