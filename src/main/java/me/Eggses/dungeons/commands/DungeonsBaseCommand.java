package me.Eggses.dungeons.commands;

import me.Eggses.dungeons.commands.subcommands.*;
import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dungeon.items.DungeonKeys;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonLoadingManager;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import me.Eggses.dungeons.items.ItemCreator;
import me.Eggses.dungeons.items.ItemGive;
import me.Eggses.dungeons.utility.misc.Permission;
import me.Eggses.dungeons.utility.text.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DungeonsBaseCommand implements CommandExecutor, TabCompleter {

    private final DungeonRegistry dungeonRegistry;
    private final DungeonEventRouter dungeonEventRouter;
    private final DungeonLoadingManager dungeonLoadingManager;
    private final DungeonKeys dungeonKeys;
    private final ItemCreator itemCreator;
    private final ItemGive itemGive;
    private final ConfigurationFile messagesFile;
    private final ConfigurationFile menusFile;
    private final MessageCreator messageCreator;
    private final TextFormatter textFormatter;

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public DungeonsBaseCommand(DungeonRegistry dungeonRegistry,
                               DungeonEventRouter dungeonEventRouter,
                               DungeonLoadingManager dungeonLoadingManager,
                               DungeonKeys dungeonKeys,
                               ItemCreator itemCreator,
                               ItemGive itemGive,
                               ConfigurationFile messagesFile,
                               ConfigurationFile menusFile,
                               MessageCreator messageCreator,
                               TextFormatter textFormatter) {

        this.dungeonRegistry = dungeonRegistry;
        this.dungeonEventRouter = dungeonEventRouter;
        this.dungeonLoadingManager = dungeonLoadingManager;
        this.dungeonKeys = dungeonKeys;
        this.itemCreator = itemCreator;
        this.itemGive = itemGive;
        this.messagesFile = messagesFile;
        this.menusFile = menusFile;
        this.messageCreator = messageCreator;
        this.textFormatter = textFormatter;

        registerCommands();
    }

    private void registerCommands() {
        var trigger = new Trigger(dungeonEventRouter, messageCreator);
        var reload = new Reload(dungeonLoadingManager, messagesFile, menusFile, messageCreator);
        var give = new Give(dungeonKeys, itemCreator, itemGive, messageCreator, textFormatter);
        var destroyInstance = new DestroyInstance(dungeonRegistry, messageCreator);

        subCommands.put(trigger.commandName(), trigger);
        subCommands.put(reload.commandName(), reload);
        subCommands.put(give.commandName(), give);
        subCommands.put(destroyInstance.commandName(), destroyInstance);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        Placeholders placeholders = messageCreator.placeholders();
        placeholders.addPlaceholder(Placeholder.PLAYER, sender.getName());

        if (!Permission.DUNGEONS_BASE.has(sender)) {
            sender.sendMessage(messageCreator.createMessage(Messages.ERROR_PERMISSION_FAIL, placeholders));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(messageCreator.createMessage(Messages.ERROR_UNKNOWN_COMMAND, placeholders));
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase(Locale.ROOT));
        if (subCommand != null) {
            subCommand.execute(sender, args, placeholders);
        } else {
            sender.sendMessage(messageCreator.createMessage(Messages.ERROR_UNKNOWN_COMMAND, placeholders));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!Permission.DUNGEONS_BASE.has(sender)) {
            return List.of();
        }

        if (args.length == 1) {
            String typed = args[0].toLowerCase(Locale.ROOT);

            return subCommands.keySet()
                    .stream()
                    .filter(commandName -> {
                        SubCommand subCommand = subCommands.get(commandName);
                        return subCommand.commandPermission().has(sender);
                    })
                    .filter(commandName -> commandName.startsWith(typed))
                    .toList();
        }

        if (args.length > 1) {
            SubCommand subCommand = subCommands.get(args[0].toLowerCase(Locale.ROOT));
            if (subCommand != null) return subCommand.tabComplete(sender, args);
        }

        return List.of();
    }
}
