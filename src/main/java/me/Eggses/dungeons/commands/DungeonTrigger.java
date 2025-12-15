package me.Eggses.dungeons.commands;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonEventRouter;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

public class DungeonTrigger implements CommandExecutor {

    private final DungeonEventRouter dungeonEventRouter;

    public DungeonTrigger(DungeonEventRouter dungeonEventRouter) {
        this.dungeonEventRouter = dungeonEventRouter;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String s,
                             @NotNull String @NotNull [] args) {


        if (!(sender instanceof BlockCommandSender blockCommandSender)) return true;
        if (args.length != 1) return true;

        int valueAsInt;
        try {
            valueAsInt = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return true;
        }

        dungeonEventRouter.handleDungeonTriggerCommand(blockCommandSender.getBlock().getWorld(), valueAsInt);
        return true;
    }
}