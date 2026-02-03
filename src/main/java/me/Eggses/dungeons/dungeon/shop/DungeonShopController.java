package me.Eggses.dungeons.dungeon.shop;

import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dungeon.items.DungeonItems;
import me.Eggses.dungeons.dungeon.items.management.DungeonTool;
import me.Eggses.dungeons.dungeon.regions.RotationPosition;
import me.Eggses.dungeons.entities.mobs.EntityManager;
import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.nameutility.MobName;
import me.Eggses.dungeons.items.ItemGive;
import me.Eggses.dungeons.items.ItemHandler;
import me.Eggses.dungeons.menu.DungeonShopMenu;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Placeholder;
import me.Eggses.dungeons.utility.text.Placeholders;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

public class DungeonShopController {

    private final Map<UUID, EnumSet<DungeonTool>> availableTools = new HashMap<>();
    private final Set<UUID> shopkeepers = new HashSet<>();

    private final EntityManager entityManager;
    private final DungeonItems<DungeonTool> dungeonsToolItems;
    private final ItemHandler itemHandler;
    private final ItemGive itemGive;
    private final MessageCreator messageCreator;
    private final ConfigurationFile menuFile;

    public DungeonShopController(EntityManager entityManager,
                                 DungeonItems<DungeonTool> dungeonsToolItems,
                                 ItemHandler itemHandler,
                                 ItemGive itemGive,
                                 MessageCreator messageCreator,
                                 ConfigurationFile menuFile) {

        this.entityManager = entityManager;
        this.dungeonsToolItems = dungeonsToolItems;
        this.itemHandler = itemHandler;
        this.itemGive = itemGive;
        this.messageCreator = messageCreator;
        this.menuFile = menuFile;
    }

    public EnumSet<DungeonTool> getToolsAvailableFor(Player player) {
        return availableTools.computeIfAbsent(player.getUniqueId(),
                key -> EnumSet.allOf(DungeonTool.class)
        );
    }

    public void removeTool(Player player, DungeonTool dungeonTool) {
        EnumSet<DungeonTool> available = availableTools.get(player.getUniqueId());
        if (available == null) return;
        available.remove(dungeonTool);
    }

    public void handleInteract(Player player, Entity entity) {

        if (!shopkeepers.contains(entity.getUniqueId())) return;

        Placeholders placeholders = messageCreator.placeholders();
        placeholders.addPlaceholder(Placeholder.PLAYER, player.getName());

        new DungeonShopMenu(
                player,
                dungeonsToolItems,
                this,
                itemHandler,
                itemGive,
                messageCreator,
                placeholders,
                menuFile.getCustomFile()
        ).open();
    }

    public void createShop(RotationPosition spawningLocation) {

        MobBuilder shopBuilder = new MobBuilder(EntityType.VILLAGER, spawningLocation)
                .mobName(new MobName("Dungeon Shop", true))
                .count(1)
                .dungeonLevel(1000)
                .spawnChanges(dungeonEntity -> {
                    var ac = dungeonEntity.getAttributeController();
                    ac.setBaseAttribute(Attribute.MAX_HEALTH, 100);
                    ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, 0);
                    ac.setBaseAttribute(Attribute.MOVEMENT_EFFICIENCY, 0);

                    Entity entity = dungeonEntity.getEntity();
                    entity.setInvulnerable(true);
                    shopkeepers.add(entity.getUniqueId());
                });

        entityManager.spawnMobWithoutAdding(shopBuilder);
    }
}
