package me.Eggses.dungeons.utility.text;

public enum Messages {

    // Prefixes
    PREFIX_MAIN("shared.prefix.main"),
    PREFIX_ERROR("shared.prefix.error"),

    // Shared errors
    ERROR_UNKNOWN_COMMAND("shared.error.unknown_command"),
    ERROR_PERMISSION_FAIL("shared.error.permission_fail"),
    ERROR_UNKNOWN_SYNTAX("shared.error.unknown_syntax"),
    ERROR_MUST_BE_PLAYER("shared.error.must_be_player"),
    ERROR_PLAYER_NOT_FOUND("shared.error.player_not_found"),

    // Reload
    DUNGEONS_RELOAD_USAGE("dungeons_reload.usage"),
    DUNGEONS_RELOAD_UNKNOWN_TARGET("dungeons_reload.unknown_target"),
    DUNGEONS_RELOAD_RELOADED("dungeons_reload.reloaded"),

    // Destroy
    DUNGEONS_DESTROY_USAGE("dungeons_destroy.usage"),
    DUNGEONS_DESTROY_INVALID_DUNGEON_WORLD("dungeons_destroy.invalid_dungeon_world"),
    DUNGEONS_DESTROY_REQUESTED("dungeons_destroy.requested"),
    DUNGEONS_DESTROY_NO_WORLDS_EXIST("dungeons_destroy.no_worlds_exist"),

    // Give
    DUNGEONS_GIVE_USAGE("dungeons_give.usage"),
    DUNGEONS_GIVE_UNKNOWN_KEY("dungeons_give.unknown_key"),
    DUNGEONS_GIVE_GIVEN("dungeons_give.given"),
    // Keystone
    KEYSTONE_DISABLED("keystone.disabled"),
    KEYSTONE_OPENING("keystone.opening"),

    BANNED_ITEMS_ALLOWED_TO_ENTER("banned_items.allowed_to_enter"),
    BANNED_ITEMS_DENIED_TO_ENTER("banned_items.denied_to_enter"),

    ;

    private final String path;

    Messages(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
