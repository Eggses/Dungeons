package me.Eggses.dungeons.commands.subcommands;

import me.Eggses.dungeons.dungeon.items.DungeonItems;
import me.Eggses.dungeons.dungeon.items.management.DungeonTool;
import me.Eggses.dungeons.dungeon.types.DungeonType;
import me.Eggses.dungeons.items.ItemGive;
import me.Eggses.dungeons.utility.misc.Permission;
import me.Eggses.dungeons.utility.text.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Give implements SubCommand {

    private static final String COMMAND_NAME = "give";
    private static final Permission PERMISSION = Permission.GIVE;

    private static final String KEY = "key";
    private static final String TOOL = "tool";

    private static final List<String> OPTIONS = List.of(KEY, TOOL);

    private final DungeonItems<DungeonType> dungeonKeyItems;
    private final DungeonItems<DungeonTool> dungeonTools;
    private final ItemGive itemGive;
    private final MessageCreator messageCreator;

    public Give(DungeonItems<DungeonType> dungeonKeyItems,
                DungeonItems<DungeonTool> dungeonTools,
                ItemGive itemGive,
                MessageCreator messageCreator) {

        this.dungeonKeyItems = dungeonKeyItems;
        this.dungeonTools = dungeonTools;
        this.itemGive = itemGive;
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

        if (args.length < 3) {
            player.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_USAGE, placeholders));
            return;
        }

        placeholders.addPlaceholder(Placeholder.GIVE_TYPE, args[1]);
        placeholders.addPlaceholder(Placeholder.GIVE_KEY, args[2]);

        switch (args[1].toLowerCase(Locale.ROOT)) {
            case KEY -> {
                DungeonType dungeonType = DungeonType.getType(args[2]);
                ItemStack key = dungeonKeyItems.createItem(
                        dungeonType,
                        placeholders,
                        DungeonItems.DUNGEON_KEY_META_CONSUMER
                );
                giveItemResolver(player, key, args, placeholders);
            }
            case TOOL -> {
                DungeonTool dungeonTool = DungeonTool.getType(args[2]);
                if (dungeonTool == null) {
                    System.out.println("null type");
                    return;
                }
                ItemStack item = dungeonTools.createItem(
                        dungeonTool,
                        placeholders,
                        dungeonTool.getItemMetaConsumer()
                );
                giveItemResolver(player, item, args, placeholders);
            }
            default -> player.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_UNKNOWN_TYPE, placeholders));
        }
    }

    public void giveItemResolver(Player sender, ItemStack itemToGive, String[] args, Placeholders placeholders) {

        if (itemToGive == null) {
            sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_UNKNOWN_KEY, placeholders));
            return;
        }

        if (args.length == 3) {
            placeholders.addPlaceholder(Placeholder.TARGET_PLAYER, sender.getName());
            itemGive.giveOrDrop(sender, itemToGive);
            sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_GIVEN, placeholders));
        } else if (args.length == 4) {
            placeholders.addPlaceholder(Placeholder.TARGET_PLAYER, args[3]);

            Player recipient = Bukkit.getPlayer(args[3]);
            if (recipient == null) {
                sender.sendMessage(messageCreator.createMessage(Messages.ERROR_PLAYER_NOT_FOUND, placeholders));
                return;
            }

            itemGive.giveOrDrop(recipient, itemToGive);
            sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_GIVEN, placeholders));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {

        if (!PERMISSION.has(sender)) {
            return List.of();
        }

        if (args.length == 2) {
            return OPTIONS;
        }

        if (args.length == 3) {

            if (args[1].equalsIgnoreCase(KEY)) {
                return dungeonKeyItems.getFormattedKeyNames()
                        .stream()
                        .filter(name -> name.startsWith(args[2]))
                        .toList();

            } else if (args[1].equalsIgnoreCase(TOOL)) {
                return dungeonTools.getFormattedKeyNames()
                        .stream()
                        .filter(name -> name.startsWith(args[2]))
                        .toList();
            }
        }

        if (args.length == 4) {
            return Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Player::getName)
                    .filter(name -> name.startsWith(args[3]))
                    .toList();
        }

        return List.of();
    }
}
