package me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.tasks.Task;
import me.Eggses.dungeons.tasks.TaskProvider;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;

import java.util.Set;

public class Enrage implements TaskProvider<Boss> {

    private static final long ENRAGE_DELAY = 20L * 60L * 10L;
    private static final double ENRAGE_DAMAGE = 100000;

    @Override
    public Task<Boss> getTask() {
        return (ctx) -> ctx.runTaskLaterAndRemove(() -> {

            Boss boss = ctx.getOwner();
            if (boss == null) return;

            Set<Player> players = boss.getPlayersInFight();
            DamageSource damageSource = DamageSource.builder(DamageType.MAGIC).build();
            for (Player player : players) {
                player.damage(ENRAGE_DAMAGE, damageSource);
            }
        }, ENRAGE_DELAY);
    }
}
