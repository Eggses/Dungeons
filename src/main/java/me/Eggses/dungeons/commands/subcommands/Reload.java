package me.Eggses.dungeons.commands.subcommands;

import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dungeon.files.DungeonTools;
import me.Eggses.dungeons.dungeon.files.PlayerStats;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonLoadingManager;
import me.Eggses.dungeons.utility.misc.Permission;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Messages;
import me.Eggses.dungeons.utility.text.Placeholder;
import me.Eggses.dungeons.utility.text.Placeholders;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Locale;

public class Reload implements SubCommand {

    private static final String COMMAND_NAME = "reload";
    private static final Permission PERMISSION = Permission.RELOAD;

    private static final String ALL = "all";
    private static final String MESSAGES = "messages";
    private static final String MENUS = "menus";
    private static final String TEMPLATES = "dungeon_templates";
    private static final String PLAYER_STATS = "player_stats";
    private static final String DUNGEON_TOOLS = "dungeon_tools";

    private static final List<String> OPTIONS = List.of(ALL, MESSAGES, MENUS, TEMPLATES, PLAYER_STATS, DUNGEON_TOOLS);

    private final DungeonLoadingManager dungeonLoadingManager;
    private final DungeonTools dungeonTools;
    private final ConfigurationFile messagesFile;
    private final ConfigurationFile menusFile;
    private final PlayerStats playerStats;
    private final MessageCreator messageCreator;

    public Reload(DungeonLoadingManager dungeonLoadingManager,
                  DungeonTools dungeonTools,
                  ConfigurationFile messagesFile,
                  ConfigurationFile menusFile, PlayerStats playerStats,
                  MessageCreator messageCreator) {

        this.dungeonLoadingManager = dungeonLoadingManager;
        this.dungeonTools = dungeonTools;
        this.messagesFile = messagesFile;
        this.menusFile = menusFile;
        this.playerStats = playerStats;
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
            sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_RELOAD_USAGE, placeholders));
            return;
        }

        switch (args[1].toLowerCase(Locale.ROOT)) {
            case ALL -> {
                dungeonLoadingManager.reloadAllDungeons();
                messagesFile.reloadCustomFile();
                menusFile.reloadCustomFile();
                playerStats.flushSave();
                playerStats.reload();
                dungeonTools.reload();
                placeholders.addPlaceholder(Placeholder.RELOAD_TARGET, ALL);
                sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_RELOAD_RELOADED, placeholders));
            }
            case MESSAGES -> {
                messagesFile.reloadCustomFile();
                placeholders.addPlaceholder(Placeholder.RELOAD_TARGET, MESSAGES);
                sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_RELOAD_RELOADED, placeholders));
            }
            case MENUS -> {
                menusFile.reloadCustomFile();
                placeholders.addPlaceholder(Placeholder.RELOAD_TARGET, MENUS);
                sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_RELOAD_RELOADED, placeholders));
            }
            case TEMPLATES -> {
                dungeonLoadingManager.reloadAllDungeons();
                placeholders.addPlaceholder(Placeholder.RELOAD_TARGET, TEMPLATES);
                sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_RELOAD_RELOADED, placeholders));
            }
            case PLAYER_STATS -> {
                playerStats.flushSave();
                playerStats.reload();
                placeholders.addPlaceholder(Placeholder.RELOAD_TARGET, PLAYER_STATS);
                sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_RELOAD_RELOADED, placeholders));
            }
            case DUNGEON_TOOLS -> {
                dungeonTools.reload();
                placeholders.addPlaceholder(Placeholder.RELOAD_TARGET, DUNGEON_TOOLS);
                sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_RELOAD_RELOADED, placeholders));
            }
            default -> {
                placeholders.addPlaceholder(Placeholder.RELOAD_TARGET, args[1]);
                sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_RELOAD_UNKNOWN_TARGET, placeholders));
                sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_RELOAD_USAGE, placeholders));
            }
        }
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
        return OPTIONS.stream()
                .filter(suggestion -> suggestion.startsWith(currentString))
                .toList();
    }
}
