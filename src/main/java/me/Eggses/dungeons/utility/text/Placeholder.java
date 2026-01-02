package me.Eggses.dungeons.utility.text;

public enum Placeholder {


    PREFIX_MAIN("%prefix_main%"),
    PREFIX_ERROR("%prefix_error%"),

    RELOAD_TARGET("%reload_target%"),

    PLAYER("%player%"),

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
