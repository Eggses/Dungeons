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
    ERROR_MUST_BE_COMMAND_BLOCK("shared.error.must_be_command_block"),

    // Reload
    DUNGEONS_RELOAD_USAGE("dungeons_reload.usage"),
    DUNGEONS_RELOAD_UNKNOWN_TARGET("dungeons_reload.unknown_target"),
    DUNGEONS_RELOAD_RELOADED("dungeons_reload.reloaded"),

    // Destroy
    DUNGEONS_DESTROY_USAGE("dungeons_destroy.usage"),
    DUNGEONS_DESTROY_INVALID_DUNGEON_WORLD("dungeons_destroy.invalid_dungeon_world"),
    DUNGEONS_DESTROY_REQUESTED("dungeons_destroy.requested"),

    // Keystone
    KEYSTONE_DISABLED("keystone.disabled");

    private final String path;

    Messages(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
