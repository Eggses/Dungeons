package me.Eggses.dungeons.menu;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonFactory;
import me.Eggses.dungeons.dungeon.types.DungeonType;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.items.ItemGive;
import me.Eggses.dungeons.items.ItemHandler;
import me.Eggses.dungeons.items.ItemKey;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Messages;
import me.Eggses.dungeons.utility.text.Placeholder;
import me.Eggses.dungeons.utility.text.Placeholders;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class KeystoneMenu extends Menu {

    private static final int POSITION_OF_KEY = 22;

    private final DungeonFactory dungeonFactory;
    private final DungeonType dungeonType;
    private final ItemGive itemGive;
    private final ItemKey itemKey;
    private final BannedItems bannedItems;
    private final MessageCreator messageCreator;

    public KeystoneMenu(Player player,
                        DungeonFactory dungeonFactory,
                        DungeonType dungeonType,
                        ItemHandler itemHandler,
                        ItemGive itemGive,
                        ItemKey itemKey,
                        BannedItems bannedItems,
                        MessageCreator messageCreator,
                        Placeholders placeholders,
                        FileConfiguration menuConfig) {
        super(
                menuConfig.getString("keystone_menu.title"),
                Row.SIX,
                player,
                itemHandler,
                messageCreator,
                placeholders,
                menuConfig
        );
        this.dungeonFactory = dungeonFactory;
        this.dungeonType = dungeonType;
        this.itemGive = itemGive;
        this.itemKey = itemKey;
        this.bannedItems = bannedItems;
        this.messageCreator = messageCreator;

        createItems();
    }

    @Override
    protected void createItems() {

        addItem(Items.INFO);
        addItem(Items.CHECK_INVENTORY, () -> bannedItems.createAndSendBannedItemsMessage(getPlayer()));
        insertKeyState();
        addItem(Items.CLOSE, this::closeInventory);

        fillPanelItems(Items.PANEL);
        destroyItemAt(POSITION_OF_KEY);

        addAction(POSITION_OF_KEY, this::removeKeyItemIfPlaced);
    }

    @Override
    public void cleanUpOnClose() {
        removeKeyItemIfPlaced();
    }

    protected void insertKeyState() {
        addItem(Items.INSERT_KEY, () -> {});
    }

    protected void readyToOpenState() {

        addItem(Items.OPEN_PORTAL, () -> {

            Player player = getPlayer();

            Placeholders placeholders = messageCreator.placeholders();
            placeholders.addPlaceholder(Placeholder.PLAYER, player.getName());

            boolean creating = dungeonFactory.attemptToCreateDungeon(dungeonType);

            if (creating) {
                player.sendMessage(messageCreator.createMessage(Messages.KEYSTONE_OPENING, placeholders));
            } else {
                player.sendMessage(messageCreator.createMessage(Messages.KEYSTONE_DISABLED, placeholders));
            }
        });
    }


    public void insertDungeonKey(Player player, int slot, ItemStack item) {

        Optional<String> maybeKey = itemKey.getMetaData(item);
        if (maybeKey.isEmpty()) return;
        if (!maybeKey.get().equals(dungeonType.getUniqueKey())) return;

        Inventory menuInventory = getInventory();

        ItemStack existing = menuInventory.getItem(POSITION_OF_KEY);
        if (existing != null && !existing.getType().isAir()) return;

        ItemStack key = item.clone();
        player.getInventory().setItem(slot, null);

        menuInventory.setItem(POSITION_OF_KEY, key);

        readyToOpenState();
    }

    private void removeKeyItemIfPlaced() {

        ItemStack itemAtKeyPosition = getInventory().getItem(POSITION_OF_KEY);
        if (itemAtKeyPosition == null || itemAtKeyPosition.getType().isAir()) return;

        Optional<ItemStack> maybeKey = takeAndDestroyItemAt(POSITION_OF_KEY);
        if (maybeKey.isEmpty()) return;

        ItemStack item = maybeKey.get();

        itemGive.giveOrDrop(getPlayer(), item);

        insertKeyState();
    }

    private enum Items implements MenuItem {

        PANEL("panel", -1),
        INFO("info", 10),
        CHECK_INVENTORY("check_inventory", 16),
        OPEN_PORTAL("open_portal", 31),
        INSERT_KEY("insert_key", 31),
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
