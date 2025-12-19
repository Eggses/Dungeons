package me.Eggses.dungeons.commands.subcommands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    String getCommandName();
    String getPermission();
    void onCommand(CommandSender sender, String[] args);
    List<String> onTabComplete(CommandSender sender, String[] args);
}
