package me.Eggses.dungeons.menu;

import me.Eggses.dungeons.dungeon.items.DungeonItems;
import me.Eggses.dungeons.dungeon.items.management.DungeonTool;
import me.Eggses.dungeons.dungeon.shop.DungeonShopController;
import me.Eggses.dungeons.items.ItemGive;
import me.Eggses.dungeons.items.ItemHandler;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Messages;
import me.Eggses.dungeons.utility.text.Placeholder;
import me.Eggses.dungeons.utility.text.Placeholders;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class DungeonShopMenu extends Menu {

    private final Player player;
    private final DungeonItems<DungeonTool> dungeonsToolItems;
    private final DungeonShopController dungeonShopController;
    private final ItemGive itemGive;
    private final MessageCreator messageCreator;
    private final Placeholders placeholders;

    private static final Map<DungeonTool, Integer> DUNGEON_TOOL_POSITIONS = Map.of(
            DungeonTool.DUNGEON_AXE, 10
    );

    public DungeonShopMenu(Player player,
                           DungeonItems<DungeonTool> dungeonsToolItems,
                           DungeonShopController dungeonShopController,
                           ItemHandler itemHandler,
                           ItemGive itemGive,
                           MessageCreator messageCreator,
                           Placeholders placeholders,
                           FileConfiguration menuConfig) {
        super(
                menuConfig.getString("dungeon_shop_menu.title"),
                Row.SIX,
                player,
                itemHandler,
                messageCreator,
                placeholders,
                menuConfig
        );
        this.player = player;
        this.dungeonsToolItems = dungeonsToolItems;
        this.dungeonShopController = dungeonShopController;
        this.itemGive = itemGive;
        this.messageCreator = messageCreator;
        this.placeholders = placeholders;

        createItems();
    }

    @Override
    public void cleanUpOnClose() {
    }

    @Override
    protected void createItems() {
        addItem(Items.CLOSE, this::closeInventory);

        EnumSet<DungeonTool> availableTools = dungeonShopController.getToolsAvailableFor(player);
        placeholders.addPlaceholder(Placeholder.COST, "FREE!");

        for (Map.Entry<DungeonTool, Integer> entry : DUNGEON_TOOL_POSITIONS.entrySet()) {

            DungeonTool dungeonTool = entry.getKey();
            Integer position = entry.getValue();

            if (availableTools.contains(dungeonTool)) {

                ItemStack itemStack = dungeonsToolItems.createItem(
                        dungeonTool,
                        placeholders,
                        dungeonTool.getItemMetaConsumer()
                );

                List<Component> currentLore = itemStack.lore();
                List<Component> newLore = (currentLore != null) ? new ArrayList<>(currentLore) : new ArrayList<>();

                newLore.add(messageCreator.createMessage(""));
                newLore.add(messageCreator.createMessage(Messages.CLICK_TO_CLAIM, placeholders));
                itemStack.lore(newLore);

                addItem(itemStack, position, () -> {

                    ItemStack itemToGive = dungeonsToolItems.createItem(
                            dungeonTool,
                            placeholders,
                            dungeonTool.getItemMetaConsumer()
                    );
                    itemGive.giveOrDrop(player, itemToGive);
                    makeOutOfStock(position);
                    dungeonShopController.removeTool(player, dungeonTool);
                });
            } else {
                makeOutOfStock(position);
            }
        }
        fillPanelItems(Items.PANEL);
    }

    private void makeOutOfStock(int index) {
        ItemStack itemStack = createItemStack(Items.OUT_OF_STOCK, placeholders, (itemMeta -> {}));
        getInventory().setItem(index, itemStack);
        addAction(index, () -> {});
    }

    private enum Items implements MenuItem {

        PANEL("panel", -1),
        OUT_OF_STOCK("out_of_stock", -1),
        CLOSE("close", 49);

        private static final String BASE = "dungeon_shop_menu.items.";

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
