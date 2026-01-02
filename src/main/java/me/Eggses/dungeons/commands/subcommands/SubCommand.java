package me.Eggses.dungeons.commands.subcommands;

import me.Eggses.dungeons.utility.misc.Permission;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    String commandName();
    Permission commandPermission();
    void execute(CommandSender sender, String[] args);
    List<String> tabComplete(CommandSender sender, String[] args);
}
