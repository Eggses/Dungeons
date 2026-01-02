package me.Eggses.dungeons.commandsOLD.subcommands;

import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonRegistry;
import me.Eggses.dungeons.utility.text.Messages;
import me.Eggses.dungeons.utility.text.MessageCreator;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class DestroyDungeon implements SubCommand {

    private static final String COMMAND_NAME = "destroy";
    private static final String COMMAND_PERMISSION = "dungeons.commands.admin.destroy";

    private final DungeonRegistry dungeonRegistry;
    private final MessageCreator messageCreator;

    public DestroyDungeon(DungeonRegistry dungeonRegistry, MessageCreator messageCreator) {
        this.dungeonRegistry = dungeonRegistry;
        this.messageCreator = messageCreator;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String getPermission() {
        return COMMAND_PERMISSION;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {

        if (!sender.hasPermission(COMMAND_PERMISSION)) {
            sender.sendMessage(messageCreator.createMessage(Messages.PERMISSION_FAIL.getMessage()));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(messageCreator.createMessage(Messages.UNKNOWN_SYNTAX.getMessage()));
            return;
        }

        if (args[1].equals("ALL")) {
            dungeonRegistry.endAllInstances(true);
            sender.sendMessage(messageCreator.createMessage(Messages.DUNGEON_DESTROY_REQUESTED.getMessage()));
            return;
        }

        DungeonInstance dungeonInstance = dungeonRegistry.getDungeonWithWorldName(args[1]);
        if (dungeonInstance == null) {
            sender.sendMessage(messageCreator.createMessage(Messages.INVALID_DUNGEON_WORLD.getMessage()));
            return;
        }

        dungeonRegistry.endDungeonInstance(dungeonInstance, true);
        sender.sendMessage(messageCreator.createMessage(Messages.DUNGEON_DESTROY_REQUESTED.getMessage()));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {

        if (!sender.hasPermission(COMMAND_PERMISSION)) return List.of();

        List<String> list = new ArrayList<>();
        if (args.length == 2) {
            list.add("ALL");
            dungeonRegistry.getDungeonWorlds().forEach(world -> list.add(world.getName()));
        }

        return list;
    }
}