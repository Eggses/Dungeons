package me.Eggses.dungeons.menu;

import me.Eggses.dungeons.dungeon.files.PlayerStats;
import me.Eggses.dungeons.dungeon.lifecycle.DungeonTemplateRegistry;
import me.Eggses.dungeons.dungeon.types.DungeonType;
import me.Eggses.dungeons.items.ItemHandler;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Placeholder;
import me.Eggses.dungeons.utility.text.Placeholders;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Map;
import java.util.Optional;

public class StatsMenu extends Menu {

    private static final Map<DungeonType, MenuItem> MENU_ITEM_MAP = Map.of(
            DungeonType.MALIGNANT_MARSH, Items.MALIGNANT_MARSH
    );

    private final Player statsFor;
    private final PlayerStats playerStats;
    private final DungeonTemplateRegistry dungeonTemplateRegistry;
    private final MessageCreator messageCreator;
    private final Placeholders placeholders;

    public StatsMenu(Player player,
                     Player statsFor,
                     PlayerStats playerStats,
                     ItemHandler itemHandler,
                     MessageCreator messageCreator,
                     Placeholders placeholders,
                     FileConfiguration menuConfig,
                     DungeonTemplateRegistry dungeonTemplateRegistry) {
        super(
                menuConfig.getString("stats_menu.title"),
                Row.SIX,
                player,
                itemHandler,
                messageCreator,
                placeholders,
                menuConfig
        );
        this.statsFor = statsFor;
        this.playerStats = playerStats;
        this.messageCreator = messageCreator;
        this.dungeonTemplateRegistry = dungeonTemplateRegistry;
        this.placeholders = placeholders;

        createItems();
    }

    @Override
    public void cleanUpOnClose() {
        // no tasks
    }

    @Override
    protected void createItems() {

        addItem(Items.CLOSE, this::closeInventory);

        addItem(Items.INFO, null, placeholders, (itemMeta -> {
            if (!(itemMeta instanceof SkullMeta skullMeta)) return;
            skullMeta.setOwningPlayer(statsFor);
        }));
        // TODO make this stuff support offline players... as skull meta supports that.

        for (Map.Entry<DungeonType, MenuItem> entry : MENU_ITEM_MAP.entrySet()) {

            DungeonType dungeonType = entry.getKey();
            var template = dungeonTemplateRegistry.getNonInstanceDungeonTemplate(dungeonType);
            placeholders.addPlaceholder(Placeholder.DUNGEON_NAME, template.dungeonName());

            Optional<PlayerStats.DungeonStat> maybeStats = playerStats.getStatsFor(statsFor, dungeonType);

            if (maybeStats.isEmpty()) {
                placeholders.addPlaceholder(Placeholder.BEST_TIME, "N/A");
                placeholders.addPlaceholder(Placeholder.COMPLETIONS, "N/A");
            } else {
                PlayerStats.DungeonStat stats = maybeStats.get();
                placeholders.addPlaceholder(Placeholder.BEST_TIME, msToTime(stats.bestTimeMs()));
                placeholders.addPlaceholder(Placeholder.COMPLETIONS, String.valueOf(stats.completions()));
            }
        }
        fillPanelItems(Items.PANEL);
    }

    private String msToTime(long ms) {
        long totalSeconds = ms / 1000;

        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        }

        return String.format("%dm %ds", minutes, seconds);
    }


    private enum Items implements MenuItem {

        PANEL("panel", -1),
        INFO("info", 13),
        MALIGNANT_MARSH("malignant_marsh", 19),
        CLOSE("close", 49);

        private static final String BASE = "keystone_menu.items.";

        private final String key;
        private final int slot;

        Items(String key, int slot) {
            this.key = key;
            this.slot = slot;
        }

        private String basePath() {
            return BASE + key + ".";
        }

        @Override
        public String getNamePath() {
            return basePath() + ITEM_NAME;
        }

        @Override
        public String getMaterialPath() {
            return basePath() + ITEM_MATERIAL;
        }

        @Override
        public String getLorePath() {
            return basePath() + ITEM_LORE;
        }

        @Override
        public int getSlot() {
            return slot;
        }
    }
}
