package me.Eggses.dungeons.utility.text;

public enum Placeholder {

    PREFIX_MAIN("%prefix_main%"),
    PREFIX_ERROR("%prefix_error%"),

    RELOAD_TARGET("%reload_target%"),
    DESTROY_TARGET("%destroy_target%"),

    PLAYER("%player%"),
    TARGET_PLAYER("%target_player%"),

    DUNGEON_NAME("%dungeon_name%"),
    OPEN_DURATION("%open_duration%"),

    GIVE_TYPE("%give_type%"),
    GIVE_KEY("%give_key%"),

    BANNED_ITEMS("%banned_items%"),

    BEST_TIME("%best_time%"),
    COMPLETIONS("%completions%"),

    COST("%cost%"),

    ;

    private final String placeholder;

    Placeholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
