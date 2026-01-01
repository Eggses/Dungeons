package me.Eggses.dungeons.utility.text;

public enum Messages {

    UNKNOWN_COMMAND("unknown_command"),
    PERMISSION_FAIL("permission_fail"),
    UNKNOWN_SYNTAX("unknown_syntax"),
    MUST_BE_PLAYER("must_be_player"),
    MUST_BE_COMMAND_BLOCK("must_be_command_block"),
    INVALID_DUNGEON_WORLD("invalid_dungeon_world"),
    DUNGEON_DESTROY_REQUESTED("dungeon_destroy_requested"),
    KEYSTONE_DISABLED("keystone_disabled"),
    ;

    private final String path;

    Messages(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}