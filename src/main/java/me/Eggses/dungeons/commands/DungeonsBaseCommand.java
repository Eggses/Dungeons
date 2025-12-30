package me.Eggses.dungeons.commands;

import me.Eggses.dungeons.commands.subcommands.DestroyDungeon;
import me.Eggses.dungeons.commands.subcommands.DungeonTrigger;
import me.Eggses.dungeons.commands.subcommands.SubCommand;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import me.Eggses.dungeons.utility.text.Messages;
import me.Eggses.dungeons.utility.text.MessageCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DungeonsBaseCommand implements CommandExecutor, TabCompleter {

    private static final String COMMAND_PERMISSION = "dungeons.commands";
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    private final MessageCreator messageCreator;

    public DungeonsBaseCommand(DungeonRegistry dungeonRegistry,
                               DungeonEventRouter dungeonEventRouter,
                               MessageCreator messageCreator) {

        this.messageCreator = messageCreator;

        var dungeonTrigger = new DungeonTrigger(dungeonEventRouter, messageCreator);
        var destroyDungeon = new DestroyDungeon(dungeonRegistry, messageCreator);

        subCommands.put(dungeonTrigger.getCommandName(), dungeonTrigger);
        subCommands.put(destroyDungeon.getCommandName(), destroyDungeon);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String s,
                             @NotNull String @NotNull [] args) {

        if (!sender.hasPermission(COMMAND_PERMISSION)) {
            sender.sendMessage(messageCreator.createMessage(Messages.PERMISSION_FAIL.getMessage()));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(messageCreator.createMessage(Messages.UNKNOWN_SYNTAX.getMessage()));
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand != null) {
            subCommand.onCommand(sender, args);
        }
        else {
            sender.sendMessage(messageCreator.createMessage(Messages.UNKNOWN_COMMAND.getMessage()));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command, @NotNull String s,
                                                @NotNull String @NotNull [] args) {

        if (!sender.hasPermission(COMMAND_PERMISSION)) return List.of();

        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            for (SubCommand subCommand : subCommands.values()) {
                if (sender.hasPermission(subCommand.getPermission())) {
                    list.add(subCommand.getCommandName());
                }
            }
        }

        if (args.length > 1) {
            SubCommand subCommand = subCommands.get(args[0].toLowerCase());
            if (subCommand != null) list = subCommand.onTabComplete(sender, args);
        }

        return list;
    }
}
