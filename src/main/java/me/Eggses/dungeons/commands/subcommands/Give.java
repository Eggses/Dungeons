package me.Eggses.dungeons.commands.subcommands;

import me.Eggses.dungeons.dungeon.items.DungeonKeyItems;
import me.Eggses.dungeons.dungeon.types.DungeonType;
import me.Eggses.dungeons.items.ItemGive;
import me.Eggses.dungeons.utility.misc.Permission;
import me.Eggses.dungeons.utility.text.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Give implements SubCommand {

    private static final String COMMAND_NAME = "give";
    private static final Permission PERMISSION = Permission.GIVE;

    private final DungeonKeyItems dungeonKeyItems;
    private final ItemGive itemGive;
    private final MessageCreator messageCreator;

    public Give(DungeonKeyItems dungeonKeyItems,
                ItemGive itemGive,
                MessageCreator messageCreator) {

        this.dungeonKeyItems = dungeonKeyItems;
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

        if (args.length < 2) {
            player.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_USAGE, placeholders));
            return;
        }

        placeholders.addPlaceholder(Placeholder.GIVE_TYPE, args[1]);

        DungeonType dungeonType = DungeonType.getType(args[1]);
        ItemStack key = dungeonKeyItems.getDungeonKey(dungeonType, placeholders);

        if (key == null) {
            player.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_UNKNOWN_TYPE, placeholders));
            return;
        }

        if (args.length == 2) {
            placeholders.addPlaceholder(Placeholder.TARGET_PLAYER, player.getName());
            itemGive.giveOrDrop(player, key);
            player.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_GIVEN, placeholders));
            return;
        }

        if (args.length == 3) {
            placeholders.addPlaceholder(Placeholder.TARGET_PLAYER, args[2]);

            Player recipient = Bukkit.getPlayer(args[2]);
            if (recipient == null) {
                player.sendMessage(messageCreator.createMessage(Messages.ERROR_PLAYER_NOT_FOUND, placeholders));
                return;
            }

            itemGive.giveOrDrop(recipient, key);
            player.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_GIVEN, placeholders));
            return;
        }

        player.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_USAGE, placeholders));
    }


    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {

        if (!PERMISSION.has(sender)) {
            return List.of();
        }

        if (args.length == 2) {
            return dungeonKeyItems.getDungeonKeyNames()
                    .stream()
                    .filter(name -> name.startsWith(args[1]))
                    .toList();
        }

        if (args.length == 3) {
            return Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Player::getName)
                    .filter(name -> name.startsWith(args[2]))
                    .toList();
        }

        return List.of();
    }
}
