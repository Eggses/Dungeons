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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Give implements SubCommand {

    private static final String COMMAND_NAME = "give";
    private static final Permission PERMISSION = Permission.GIVE;

    private static final String KEY = "key";
    private static final List<String> OPTIONS = List.of(KEY);

    private final DungeonKeyItems dungeonKeyItems;
    private final ItemGive itemGive;
    private final MessageCreator messageCreator;
    private final TextFormatter textFormatter;

    private List<String> dungeonNames;

    public Give(DungeonKeyItems dungeonKeyItems,
                ItemGive itemGive,
                MessageCreator messageCreator,
                TextFormatter textFormatter) {

        this.dungeonKeyItems = dungeonKeyItems;
        this.itemGive = itemGive;
        this.messageCreator = messageCreator;
        this.textFormatter = textFormatter;
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

        if (args.length < 3 || args.length > 5) {
            player.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_USAGE, placeholders));
            return;
        }

        Optional<GiveCommandArgs> maybeGiveCommandArgs = parseGiveCommandPlayerAndQuantity(player, args, placeholders);
        if (maybeGiveCommandArgs.isEmpty()) return;

        GiveCommandArgs giveCommandArgs = maybeGiveCommandArgs.get();

        placeholders.addPlaceholder(Placeholder.GIVE_TYPE, args[1]);
        placeholders.addPlaceholder(Placeholder.GIVE_ID, args[2]);
        placeholders.addPlaceholder(Placeholder.QUANTITY, String.valueOf(giveCommandArgs.quantity()));
        placeholders.addPlaceholder(Placeholder.TARGET_PLAYER, giveCommandArgs.player().getName());

        if (args[1].equals(KEY)) {
            giveKey(giveCommandArgs, player, args, placeholders);
            return;
        }

        player.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_UNKNOWN_TYPE, placeholders));
    }

    private void giveKey(GiveCommandArgs giveCommandArgs, Player sender, String[] args, Placeholders placeholders) {

        Player recipient = giveCommandArgs.player;

        ItemStack itemStack = dungeonKeyItems.getDungeonKey(DungeonType.getType(args[2]), placeholders);

        if (itemStack == null) {
            sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_UNKNOWN_ID, placeholders));
            return;
        }

        itemGive.giveOrDrop(recipient, new ItemGive.ItemAmount(itemStack, giveCommandArgs.quantity));

        sender.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_GIVEN, placeholders));
    }

    private Optional<GiveCommandArgs> parseGiveCommandPlayerAndQuantity(Player player, String[] args, Placeholders placeholders) {

        // Give 1 to yourself
        if (args.length == 3) {
            return Optional.of(new GiveCommandArgs(player, 1));
        }

        // Give X to yourself OR give 1 to another.
        if (args.length == 4) {

            Integer quantity = tryParseToPositiveInt(args[3]);
            if (quantity != null) {
                return Optional.of(new GiveCommandArgs(player, quantity));
            }

            Player targetPlayer = Bukkit.getPlayerExact(args[3]);
            if (targetPlayer != null) {
                return Optional.of(new GiveCommandArgs(targetPlayer, 1));
            }

            player.sendMessage(messageCreator.createMessage(Messages.DUNGEONS_GIVE_USAGE, placeholders));
            return Optional.empty();
        }

        // Give X to a target.
        if (args.length == 5) {

            Player targetPlayer = Bukkit.getPlayerExact(args[3]);
            Integer quantity = tryParseToPositiveInt(args[4]);

            if (targetPlayer == null) {
                placeholders.addPlaceholder(Placeholder.TARGET_PLAYER, args[3]);
                player.sendMessage(messageCreator.createMessage(Messages.ERROR_PLAYER_NOT_FOUND, placeholders));
                return Optional.empty();
            }

            if (quantity == null) {
                placeholders.addPlaceholder(Placeholder.QUANTITY, args[4]);
                player.sendMessage(messageCreator.createMessage(Messages.ERROR_INVALID_QUANTITY, placeholders));
                return Optional.empty();
            }

            return Optional.of(new GiveCommandArgs(targetPlayer, quantity));
        }
        return Optional.empty();
    }

    private Integer tryParseToPositiveInt(String number) {
        try {
            int value = Integer.parseInt(number);
            return (value > 0) ? value : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private record GiveCommandArgs(Player player, int quantity) {
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {

        if (!PERMISSION.has(sender)) {
            return List.of();
        }

        if (args.length == 2) {
            return OPTIONS.stream()
                    .filter(option -> args[1].startsWith(option))
                    .toList();
        }

        if (args.length == 3 && args[1].equals(KEY)) {
            if (dungeonNames != null) return dungeonNames;
            dungeonNames = Arrays.stream(DungeonType.values())
                    .map(DungeonType::getUniqueKey)
                    .map(string ->
                            textFormatter.formatName(
                                    string,
                                    TextFormatter.SPLITTER_UNDERSCORE,
                                    TextFormatter.SEPARATOR_UNDERSCORE
                            ))
                    .toList();
            return dungeonNames.stream()
                    .filter(name -> name.startsWith(args[2]))
                    .toList();
        }

        List<String> suggestions = new ArrayList<>();

        if (args.length == 4) {
            Bukkit.getOnlinePlayers().forEach(player -> suggestions.add(player.getName()));
        }

        if (args.length == 4 || args.length == 5) {

            for (int i = 1; i <= 20; i++) {
                suggestions.add(String.valueOf(i));
            }

            String index = (args.length == 4) ? args[3] : args[4];

            return suggestions.stream()
                    .filter(suggestion -> suggestion.startsWith(index))
                    .toList();
        }
        return List.of();
    }
}
