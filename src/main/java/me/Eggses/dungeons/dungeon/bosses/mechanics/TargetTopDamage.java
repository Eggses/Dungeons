package me.Eggses.dungeons.dungeon.bosses.mechanics;

import me.Eggses.dungeons.dungeon.bosses.Boss;
import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.*;

public class TargetTopDamage implements EventBehaviour<EntityDamageByEntityEvent> {

    private static final long COOLDOWN = 5000;

    private long lastTargetChange = 0;
    private final Map<UUID, DamageLog> damageLogs = new HashMap<>();
    private Mob bossMob;

    @Override
    public void handleEvent(EntityDamageByEntityEvent event, EventContext eventContext) {

        DungeonEntity owner = eventContext.getOwnerOfBehaviour();
        if (!(owner instanceof Boss boss)) return;
        Entity entity = event.getEntity();
        if (!DungeonEntity.equalsIgnoreNull(owner, entity)) return;
        if (!(event.getDamager() instanceof Player player)) return;

        if (bossMob == null || !bossMob.isValid()) {
            if (entity instanceof Mob mob) bossMob = mob;
            else return;
        }

        DamageLog log = damageLogs.computeIfAbsent(player.getUniqueId(), DamageLog::new);
        log.damage += event.getFinalDamage();

        long currentTime = System.currentTimeMillis();
        if (!(currentTime > lastTargetChange + COOLDOWN)) return;
        lastTargetChange = currentTime;

        DamageLog top = null;
        for (DamageLog damageLog : damageLogs.values()) {
            if (top == null || top.damage < damageLog.damage) top = damageLog;
        }

        if (top == null) return;

        Player topPlayer = Bukkit.getPlayer(top.playerUUID);
        if (topPlayer == null || !boss.isInFight(topPlayer)) return;

        bossMob.setTarget(topPlayer);

        for (DamageLog damageLog : damageLogs.values()) damageLog.damage = 0;
    }

    private static class DamageLog {
        private final UUID playerUUID;
        private double damage;

        private DamageLog(UUID playerUUID) {
            this.playerUUID = playerUUID;
        }
    }
}
