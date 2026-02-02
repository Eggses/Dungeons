package me.Eggses.dungeons.utility.misc;

import org.bukkit.command.CommandSender;

public enum Permission {

    DUNGEONS_BASE("dungeons.command.base"),
    RELOAD("dungeons.command.reload"),
    TRIGGER("dungeons.command.trigger"),
    GIVE("dungeons.command.give"),
    GIVE_OTHERS("dungeons.command.give.others"),
    DESTROY("dungeons.command.destroy"),
    STATS("dungeons.command.stats"),
    ;

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public boolean has(CommandSender sender) {
        return sender.hasPermission(permission);
    }
}
