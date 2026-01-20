package me.Eggses.dungeons.blocks.events;

import me.Eggses.dungeons.dungeon.lifecycle.DungeonFactory;
import me.Eggses.dungeons.dungeon.lifecycle.TemplateReservation;
import me.Eggses.dungeons.dungeon.types.DungeonType;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.eventhandler.EventBehaviour;
import me.Eggses.dungeons.eventhandler.EventContext;
import me.Eggses.dungeons.items.ItemGive;
import me.Eggses.dungeons.items.ItemHandler;
import me.Eggses.dungeons.items.ItemKey;
import me.Eggses.dungeons.menu.KeystoneMenu;
import me.Eggses.dungeons.menu.Menu;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Messages;
import me.Eggses.dungeons.utility.text.Placeholder;
import me.Eggses.dungeons.utility.text.Placeholders;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractOpenMenu implements EventBehaviour<PlayerInteractEvent> {

    private final DungeonFactory dungeonFactory;
    private final TemplateReservation templateReservation;
    private final DungeonType dungeonType;
    private final ItemHandler itemHandler;
    private final ItemGive itemGive;
    private final ItemKey itemKey;
    private final BannedItems bannedItems;
    private final MessageCreator messageCreator;
    private final Placeholders placeholders;
    private final FileConfiguration menuConfig;

    public InteractOpenMenu(DungeonFactory dungeonFactory,
                            TemplateReservation templateReservation,
                            DungeonType dungeonType,
                            ItemHandler itemHandler,
                            ItemGive itemGive,
                            ItemKey itemKey,
                            BannedItems bannedItems,
                            MessageCreator messageCreator,
                            Placeholders placeholders,
                            FileConfiguration menuConfig) {

        this.dungeonFactory = dungeonFactory;
        this.templateReservation = templateReservation;
        this.dungeonType = dungeonType;
        this.itemHandler = itemHandler;
        this.itemGive = itemGive;
        this.itemKey = itemKey;
        this.bannedItems = bannedItems;
        this.messageCreator = messageCreator;
        this.placeholders = placeholders;
        this.menuConfig = menuConfig;
    }

    @Override
    public void handleEvent(PlayerInteractEvent event, EventContext eventContext) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Placeholders placeholders = messageCreator.placeholders();

        Player player = event.getPlayer();
        placeholders.addPlaceholder(Placeholder.PLAYER, player.getName());
        placeholders.addAll(this.placeholders);

        if (!templateReservation.isTemplateFree(dungeonType)) {
            player.sendMessage(messageCreator.createMessage(Messages.KEYSTONE_DISABLED, placeholders));
            return;
        }

        Menu keystoneMenu = new KeystoneMenu(
                player,
                dungeonFactory,
                dungeonType,
                itemHandler,
                itemGive,
                itemKey,
                bannedItems,
                messageCreator,
                placeholders,
                menuConfig
        );
        keystoneMenu.open();
    }
}
