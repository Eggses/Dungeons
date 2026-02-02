package me.Eggses.dungeons.commands.subcommands;

import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dungeon.files.PlayerStats;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonTemplateRegistry;
import me.Eggses.dungeons.items.ItemHandler;
import me.Eggses.dungeons.menu.Menu;
import me.Eggses.dungeons.menu.StatsMenu;
import me.Eggses.dungeons.utility.misc.Permission;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Messages;
import me.Eggses.dungeons.utility.text.Placeholder;
import me.Eggses.dungeons.utility.text.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Stats implements SubCommand {

    private static final String COMMAND_NAME = "stats";
    private static final Permission PERMISSION = Permission.STATS;

    private final PlayerStats playerStats;
    private final ItemHandler itemHandler;
    private final ConfigurationFile menuConfig;
    private final DungeonTemplateRegistry dungeonTemplateRegistry;
    private final MessageCreator messageCreator;

    public Stats(PlayerStats playerStats,
                 ItemHandler itemHandler,
                 ConfigurationFile menuConfig,
                 DungeonTemplateRegistry dungeonTemplateRegistry,
                 MessageCreator messageCreator) {

        this.playerStats = playerStats;
        this.itemHandler = itemHandler;
        this.menuConfig = menuConfig;
        this.dungeonTemplateRegistry = dungeonTemplateRegistry;
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

        if (!(sender instanceof Player player)) {
            sender.sendMessage(messageCreator.createMessage(Messages.ERROR_MUST_BE_PLAYER, placeholders));
            return;
        }

        if (args.length == 1) {
            placeholders.addPlaceholder(Placeholder.TARGET_PLAYER, sender.getName());
            Menu statsMenu = new StatsMenu(
                    player,
                    player,
                    playerStats,
                    itemHandler,
                    messageCreator,
                    placeholders,
                    menuConfig.getCustomFile(),
                    dungeonTemplateRegistry
            );
            statsMenu.open();
        } else if (args.length == 2) {
            String targetPlayerName = args[1];
            placeholders.addPlaceholder(Placeholder.TARGET_PLAYER, targetPlayerName);

            // Works for online players too.
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetPlayerName);
            if (!target.hasPlayedBefore()) {
                sender.sendMessage(messageCreator.createMessage(Messages.ERROR_PLAYER_NOT_FOUND, placeholders));
                return;
            }

            Menu statsMenu = new StatsMenu(
                    player,
                    target,
                    playerStats,
                    itemHandler,
                    messageCreator,
                    placeholders,
                    menuConfig.getCustomFile(),
                    dungeonTemplateRegistry
            );
            statsMenu.open();
        } else {
            sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_STATS_USAGE, placeholders));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {

        if (!PERMISSION.has(sender)) {
            return List.of();
        }

        if (args.length == 2) {
            return Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Player::getName)
                    .filter(name -> name.startsWith(args[1]))
                    .toList();
        }

        return List.of();
    }
}
