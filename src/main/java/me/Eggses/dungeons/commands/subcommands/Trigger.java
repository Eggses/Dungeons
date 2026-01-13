package me.Eggses.dungeons.commands.subcommands;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import me.Eggses.dungeons.utility.misc.Permission;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Messages;
import me.Eggses.dungeons.utility.text.Placeholders;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Trigger implements SubCommand {

    private static final String COMMAND_NAME = "trigger";
    private static final Permission PERMISSION = Permission.TRIGGER;

    private final DungeonEventRouter dungeonEventRouter;
    private final MessageCreator messageCreator;

    public Trigger(DungeonEventRouter dungeonEventRouter, MessageCreator messageCreator) {
        this.dungeonEventRouter = dungeonEventRouter;
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

        if (args.length != 1) {
            return;
        }

        Location locationOfBlock = (sender instanceof BlockCommandSender commandBlock) ? commandBlock.getBlock().getLocation() : null;
        if (locationOfBlock != null) dungeonEventRouter.handleDungeonTriggerCommand(locationOfBlock);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
