package me.Eggses.dungeons.menu;

import me.Eggses.dungeons.configuration.ConfigurationFile;
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
import java.util.function.Consumer;

public abstract class Menu implements InventoryHolder {

    protected static final String ITEM_NAME = "name";
    protected static final String ITEM_MATERIAL = "material";
    protected static final String ITEM_LORE = "lore";

    private final Inventory inventory;
    private final Player owner;
    private final Map<Integer, Runnable> actions = new HashMap<>();
    private final ConfigurationFile configurationFile;
    private final ItemCreator itemCreator;
    private final Placeholders placeholders;

    protected Menu(String title,
                   int slotCount,
                   Player owner,
                   ConfigurationFile configurationFile,
                   ItemCreator itemCreator,
                   MessageCreator messageCreator,
                   Placeholders placeholders,
                   MenuManager menuManager) {

        this.inventory = Bukkit.createInventory(this, slotCount, messageCreator.createMessage(title, placeholders));
        this.owner = owner;
        this.configurationFile = configurationFile;
        this.itemCreator = itemCreator;
        this.placeholders = placeholders;

        menuManager.addAndOpen(this);
    }

    public void execute(int slotClicked) {
        Runnable action = actions.get(slotClicked);
        if (action != null) action.run();
    }

    protected void addItem(ItemStack itemStack, int slot, Runnable runnable) {
        inventory.setItem(slot, itemStack);
        if (runnable != null) actions.put(slot, runnable);
    }

    protected void addItem(ItemStack itemStack, int slot) {
        addItem(itemStack, slot, null);
    }

    protected ItemStack createItem(MenuItem menuItem) {
        return createItem(menuItem, (itemMeta) -> {});
    }

    protected ItemStack createPanelItem(MenuItem menuItem) {
        return createItem(menuItem, itemMeta -> itemMeta.setHideTooltip(true));
    }

    private ItemStack createItem(MenuItem menuItem, Consumer<ItemMeta> itemMetaConsumer) {

        FileConfiguration file = configurationFile.getCustomFile();

        String itemName = file.getString(menuItem.getNamePath());
        String itemMaterial = file.getString(menuItem.getMaterialPath());
        List<String> itemLore = file.getStringList(menuItem.getLorePath());

        ItemStackTemplate itemStackTemplate = new ItemStackTemplate(itemName, itemMaterial, itemLore, false);
        return itemCreator.createItem(itemStackTemplate, itemMetaConsumer, placeholders);
    }

    protected abstract void setItems();

    public void onMenuClose() {
        return;
    }

    public final void open() {
        owner.openInventory(inventory);
    }

    @Override
    public final @NotNull Inventory getInventory() {
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