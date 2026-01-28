package me.Eggses.dungeons.dungeon.bosses;

import me.Eggses.dungeons.utility.text.MessageCreator;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class BossBarController {

    public record Style(
            Component name,
            BossBar.Color color,
            BossBar.Overlay overlay,
            Set<BossBar.Flag> flags
    ) {}

    private final Boss boss;
    private final Style style;
    private final MessageCreator messageCreator;
    private final String colourScheme;

    private final BossBar bossBar;
    private final Set<Player> viewers = new HashSet<>();

    public BossBarController(Boss boss,
                             String colourScheme,
                             MessageCreator messageCreator,
                             Style style) {
        this.boss = boss;
        this.colourScheme = colourScheme;
        this.messageCreator = messageCreator;
        this.style = style;

        this.bossBar = BossBar.bossBar(
                style.name,
                1.0f,
                style.color,
                style.overlay,
                style.flags
        );
    }

    public void update() {

        double healthPercentage = Math.max(0.0, Math.min(100.0, boss.getRemainingHealthPercentage()));
        double currentHealth = Math.max(0.0, boss.getHealth());

        float progress = (float) (healthPercentage / 100.0);
        bossBar.progress(progress);

        String hpText = String.format("%.0f", currentHealth);
        String pctText = String.format("%.0f%%", healthPercentage);

        Component numbers = messageCreator.createMessage(colourScheme + "- " + hpText + " (" + pctText + ")");
        System.out.println(colourScheme + "- " + hpText + " (" + pctText + ")");
        Component name = style.name.appendSpace();
        bossBar.name(name.append(numbers));
    }

    public void addViewer(Player player) {
        bossBar.addViewer(player);
        viewers.add(player);
    }

    public void removeViewer(Player player) {
        bossBar.removeViewer(player);
        viewers.remove(player);
    }

    public void removeAllViewers() {
        for (Player player : viewers) {
            bossBar.removeViewer(player);
        }
        viewers.clear();
    }
}
