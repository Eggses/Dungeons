package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.tasks.Task;
import me.Eggses.dungeons.tasks.TaskProvider;
import me.Eggses.dungeons.utility.sound.DungeonSound;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import me.Eggses.dungeons.utility.text.MessageCreator;

public class HarvestIncreaseOverTime implements TaskProvider<Boss> {

    private static final long DELAY_BEFORE_STACKING = 20L;
    private static final long STACK_APPLY_PERIOD = 20L * 3L;

    private final Harvest harvest;
    private final MessageCreator messageCreator;
    private final SoundPlayer soundPlayer;

    public HarvestIncreaseOverTime(Harvest harvest, MessageCreator messageCreator, SoundPlayer soundPlayer) {
        this.harvest = harvest;
        this.messageCreator = messageCreator;
        this.soundPlayer = soundPlayer;
    }

    @Override
    public Task<Boss> getTask() {
        return ctx ->
                ctx.runTaskRepeatedly(() -> {
                    harvest.increment();
                    int stackCount = harvest.getStackCount();

                    if (stackCount % 10 != 0) return; // Not multiple of 10.

                    var message = messageCreator.createMessage("<dark_red>Harvest at " + stackCount + " stacks.");
                    var sound = soundPlayer.createSound(DungeonSound.WARDEN_ATTACK_IMPACT.getMinecraftSound());

                    Boss boss = ctx.getOwner();
                    if (boss == null) return;

                    boss.getPlayersInFight().forEach(player -> {
                        player.sendMessage(message);
                        soundPlayer.playSound(sound, player);
                    });

                }, DELAY_BEFORE_STACKING, STACK_APPLY_PERIOD);
    }
}
