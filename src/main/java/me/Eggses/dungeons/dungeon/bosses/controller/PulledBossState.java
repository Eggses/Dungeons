package me.Eggses.dungeons.dungeon.bosses.controller;

import me.Eggses.dungeons.dungeon.players.Players;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.utility.text.MessageCreator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PulledBossState implements ArenaControllerState{

    private final BossArenaController bossArenaController;
    private final Region entryRegion;
    private final MessageCreator messageCreator;

    public PulledBossState(BossArenaController bossArenaController, Region entryRegion, MessageCreator messageCreator) {
        this.bossArenaController = bossArenaController;
        this.entryRegion = entryRegion;
        this.messageCreator = messageCreator;
    }

    @Override
    public void onStateStart() {
        bossArenaController.startFight();
    }

    @Override
    public void handlePlayerMovement(Player player, Location movementLocation) {
        if (entryRegion.within(movementLocation)) {
            player.sendMessage(messageCreator.createMessage("<red>You cannot enter while a fight is in progress!"));
        }
    }

    @Override
    public void leaveBossArena(Player player) {
        Players players = bossArenaController.getPlayersInArena();
        players.remove(player);

        if (players.isEmpty()) {
            bossArenaController.failBossFight();
            bossArenaController.changeStateToReadyToCommenceState();
        }
    }
}
