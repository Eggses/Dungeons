package me.Eggses.dungeons.commands.subcommands;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import me.Eggses.dungeons.utility.text.Messages;
import me.Eggses.dungeons.utility.text.MessageCreator;
import org.bukkit.command.*;

import java.util.List;

public class DungeonTrigger implements SubCommand {

    private static final String COMMAND_NAME = "trigger";
    private static final String COMMAND_PERMISSION = "dungeons.command.admin.cmdblock.trigger";

    private final DungeonEventRouter dungeonEventRouter;
    private final MessageCreator messageCreator;

    public DungeonTrigger(DungeonEventRouter dungeonEventRouter, MessageCreator messageCreator) {
        this.dungeonEventRouter = dungeonEventRouter;
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

        if (!(sender instanceof BlockCommandSender blockCommandSender))  {
            sender.sendMessage(messageCreator.createMessage(Messages.MUST_BE_COMMAND_BLOCK.getMessage()));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(messageCreator.createMessage(Messages.UNKNOWN_SYNTAX.getMessage()));
            return;
        }

        // While not an Event, this command is treated as an Event as its purpose is the same.
        dungeonEventRouter.handleDungeonTriggerCommand(blockCommandSender.getBlock().getWorld(), args[0]);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}