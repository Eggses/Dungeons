package me.Eggses.dungeons.commands.subcommands;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import me.Eggses.dungeons.utility.misc.Permission;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Messages;
import me.Eggses.dungeons.utility.text.Placeholder;
import me.Eggses.dungeons.utility.text.Placeholders;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Locale;

public class DestroyInstance implements SubCommand {

    private static final String COMMAND_NAME = "destroy";
    private static final Permission PERMISSION = Permission.DESTROY;

    private static final String ALL = "all";

    private final DungeonRegistry dungeonRegistry;
    private final MessageCreator messageCreator;

    public DestroyInstance(DungeonRegistry dungeonRegistry, MessageCreator messageCreator) {
        this.dungeonRegistry = dungeonRegistry;
        this.messageCreator = messageCreator;
    }

    @Override
    public String commandName() {
        return COMMAND_NAME;
    }

    @Override
    public Permission commandPermission() {
        return PERMISSION;
    }

    @Override
    public void execute(CommandSender sender, String[] args, Placeholders placeholders) {

        if (!PERMISSION.has(sender)) {
            sender.sendMessage(messageCreator.createMessage(Messages.ERROR_PERMISSION_FAIL, placeholders));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_DESTROY_USAGE, placeholders));
            return;
        }

        String destroyTarget = args[1].toLowerCase(Locale.ROOT);
        placeholders.addPlaceholder(Placeholder.DESTROY_TARGET, destroyTarget);

        if (destroyTarget.equals(ALL)) {

            if (!dungeonRegistry.isAnyInstanceExist()) {
                sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_DESTROY_NO_WORLDS_EXIST, placeholders));
                return;
            }

            dungeonRegistry.endAllInstances(true);
            sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_DESTROY_REQUESTED, placeholders));
            return;
        }

        DungeonInstance instanceToDestroy = dungeonRegistry.getDungeonWithWorldName(destroyTarget);
        if (instanceToDestroy != null) {
            dungeonRegistry.endDungeonInstance(instanceToDestroy, true);
            sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_DESTROY_REQUESTED, placeholders));
            return;
        }

        sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_DESTROY_INVALID_DUNGEON_WORLD, placeholders));
        sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_DESTROY_USAGE, placeholders));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {

        if (!PERMISSION.has(sender)) {
            return List.of();
        }

        if (args.length != 2) {
            return List.of();
        }

        String currentString = args[1].toLowerCase(Locale.ROOT);
        List<String> suggestions = dungeonRegistry.getDungeonWorldNames();
        if (!suggestions.isEmpty()) suggestions.add(ALL);

        return suggestions.stream()
                .filter(suggestion -> suggestion.startsWith(currentString))
                .toList();
    }
}
