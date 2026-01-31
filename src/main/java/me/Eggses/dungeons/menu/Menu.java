package me.Eggses.dungeons.menu;

import me.Eggses.dungeons.items.ItemHandler;
import me.Eggses.dungeons.items.ItemTemplate;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class Menu implements InventoryHolder {

    protected static final String ITEM_NAME = "name";
    protected static final String ITEM_MATERIAL = "material";
    protected static final String ITEM_LORE = "lore";

    private final Inventory inventory;
    private final Player player;
    private final ItemHandler itemHandler;
    private final FileConfiguration menuConfig;
    private final MessageCreator messageCreator;

    private final Map<Integer, Runnable> onClickActions = new HashMap<>();

    protected Menu(String title,
                   Row rows,
                   Player player,
                   ItemHandler itemHandler,
                   MessageCreator messageCreator,
                   Placeholders placeholders,
                   FileConfiguration menuConfig
    ) {
        this.inventory = Bukkit.createInventory(
                this, rows.getSlotCount(), messageCreator.createMessage(title, placeholders)
        );

        this.player = player;
        this.itemHandler = itemHandler;
        this.menuConfig = menuConfig;
        this.messageCreator = messageCreator;
    }

    protected void addItem(MenuItem menuItem) {
        addItem(menuItem, null, null, null);
    }

    protected void addItem(MenuItem menuItem, Runnable action) {
        addItem(menuItem, action, messageCreator.placeholders(), null);
    }

    protected void addItem(MenuItem menuItem, Placeholders placeholders) {
        addItem(menuItem, null, placeholders, null);
    }

    protected void addItem(MenuItem menuItem, Runnable action, Placeholders placeholders) {
        addItem(menuItem, action, placeholders, null);
    }

    protected void addItem(MenuItem menuItem, Runnable action, Placeholders placeholders, Consumer<ItemMeta> itemMetaConsumer) {

        String name = menuConfig.getString(menuItem.getNamePath());
        String material = menuConfig.getString(menuItem.getMaterialPath());
        List<String> lore = menuConfig.getStringList(menuItem.getLorePath());
        int slot = menuItem.getSlot();

        ItemTemplate itemTemplate = new ItemTemplate(name, material, lore);

        ItemStack itemStack = itemHandler.createItem(itemTemplate, placeholders, itemMetaConsumer);

        inventory.setItem(slot, itemStack);

        if (action != null) onClickActions.put(slot, action);
    }

    protected void fillPanelItems(MenuItem menuItem) {

        String material = menuConfig.getString(menuItem.getMaterialPath());
        ItemTemplate itemTemplate = new ItemTemplate(null, material, null);
        ItemStack item = itemHandler.createItem(itemTemplate, messageCreator.placeholders(), ItemHandler.NO_DISPLAY);

        ItemStack[] items = inventory.getContents();

        for (int i = 0; i < items.length; i++) {
            if (items[i] == null || items[i].getType().isAir()) {
                inventory.setItem(i, item);
            }
        }
    }

    protected void addAction(int slot, Runnable runnable) {
        onClickActions.put(slot, runnable);
    }

    protected void destroyItemAt(int slot) {
        inventory.setItem(slot, null);
    }

    protected Optional<ItemStack> takeAndDestroyItemAt(int slot) {

        ItemStack item = inventory.getContents()[slot];
        if (item != null) {
            inventory.setItem(slot, null);
            return Optional.of(item);
        }
        return Optional.empty();
    }

    public void open() {
        player.openInventory(inventory);
    }

    public final void closeInventory() {
        cleanUpOnClose();
        player.closeInventory();
    }

    public abstract void cleanUpOnClose();

    public Player getPlayer() {
        return player;
    }

    protected abstract void createItems();

    public void click(int slot) {
        Runnable runnable = onClickActions.get(slot);
        if (runnable != null) runnable.run();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    protected enum Row {
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6);

        private final int row;

        Row(int row) {
            this.row = row;
        }

        public int getSlotCount() {
            return row * 9;
        }
    }
}
