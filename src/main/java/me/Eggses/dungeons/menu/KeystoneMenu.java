package me.Eggses.dungeons.menu;

import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.items.ItemCreator;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Placeholders;
import org.bukkit.entity.Player;

public class KeystoneMenu extends Menu {

    protected KeystoneMenu(Player owner, ConfigurationFile configurationFile, ItemCreator itemCreator, MessageCreator messageCreator, Placeholders placeholders, MenuManager menuManager) {
        super(
                configurationFile.getCustomFile().getString("keystone_menu.title"),
                Row.SIX.getSlotCount(),
                owner,
                configurationFile,
                itemCreator,
                messageCreator,
                placeholders,
                menuManager
        );
        setItems();
    }

    @Override
    protected void setItems() {

    }

    private enum KeystoneMenuItem implements MenuItem {
        PANEL("panel", -1),
        OPEN_PORTAL("open_portal", 1),
        INVALID_KEY("invalid_key", 1),
        INSERT_KEY("insert_key", 1),
        CHECK_INVENTORY("check_inventory", 1),
        CLOSE("close", 1);

        private final String path;
        private final int slot;

        private static final String ITEMS = "keystone_menu.items";

        KeystoneMenuItem(String path, int slot) {
            this.path = path;
            this.slot = slot;
        }

        @Override
        public String getNamePath() {
            return ITEMS + path + ITEM_NAME;
        }

        @Override
        public String getMaterialPath() {
            return ITEMS + path + ITEM_MATERIAL;
        }

        @Override
        public String getLorePath() {
            return ITEMS + path + ITEM_LORE;
        }

        @Override
        public int getSlot() {
            return slot;
        }
    }
}