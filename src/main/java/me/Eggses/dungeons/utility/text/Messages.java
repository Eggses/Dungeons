package me.Eggses.dungeons.utility.text;

public enum Messages {

    UNKNOWN_COMMAND("<dark_red><bold>Error: </bold></dark_red><gradient:#ff5555:#ffaaaa>Unknown command.</gradient>"),
    PERMISSION_FAIL("<dark_red><bold>Error: </bold></dark_red><gradient:#ff5555:#ffaaaa>You do not have permission to use this command.</gradient>"),
    UNKNOWN_SYNTAX("<dark_red><bold>Error: </bold></dark_red><gradient:#ff5555:#ffaaaa>Unknown command syntax.</gradient>"),
    MUST_BE_PLAYER("<dark_red><bold>Error: </bold></dark_red><gradient:#ff5555:#ffaaaa>This command can only be used by a player.</gradient>"),
    MUST_BE_COMMAND_BLOCK("<dark_red><bold>Error: </bold></dark_red><gradient:#ff5555:#ffaaaa>This command can only be executed by a command block.</gradient>"),
    INVALID_DUNGEON_WORLD("<dark_red><bold>Error: </bold></dark_red><gradient:#ff5555:#ffaaaa>That world does not exist or is not a dungeon world.</gradient>"),
    DUNGEON_DESTROY_REQUESTED("<gradient:#00FF00:#32CD32>Dungeon world(s) scheduled for destruction.</gradient>"),
    ;

    private final String string;

    Messages(String string) {
        this.string = string;
    }

    public String getMessage() {
        return string;
    }
}