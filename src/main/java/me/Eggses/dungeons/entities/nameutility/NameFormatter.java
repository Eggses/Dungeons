package me.Eggses.dungeons.entities.nameutility;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.utility.MessageCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class NameFormatter {

    private static final Map<Integer, String> LEVEL_COLOUR_MAP = new TreeMap<>();
    private static final Map<EntityType, String> ENTITY_NAME_CACHE = new HashMap<>();

    private static final String LEVEL_PREFIX = "<gray>ʟᴠʟ</gray>";
    private static final String SEPARATOR = "<gray> ⟡ <gray>";
    private static final String HEALTH_PREFIX = "<red>";
    private static final String HEALTH_SUFFIX = "<gray>ʜᴘ</gray>";
    private static final String NAME_PREFIX = "<white>";

    static {
        LEVEL_COLOUR_MAP.put(0, "<green>");
        LEVEL_COLOUR_MAP.put(10, "<blue>");
        LEVEL_COLOUR_MAP.put(20, "<gold>");
        LEVEL_COLOUR_MAP.put(30, "<light_purple>");
        LEVEL_COLOUR_MAP.put(40, "<white>");
        LEVEL_COLOUR_MAP.put(50, "<red>");
    }

    private final DungeonEntity dungeonEntity;
    private final MessageCreator messageCreator;

    private final Component levelAndName;

    public NameFormatter(DungeonEntity dungeonEntity, MessageCreator messageCreator) {
        this.dungeonEntity = dungeonEntity;
        this.messageCreator = messageCreator;

        levelAndName = messageCreator.createMessage(createLevelPart() + SEPARATOR + NAME_PREFIX + createNamePart() + SEPARATOR);
    }

    public Component updateHealth(int health) {
        Component healthDisplay = messageCreator.createMessage(HEALTH_PREFIX + health + HEALTH_SUFFIX);
        return levelAndName.append(healthDisplay);
    }

    private String createLevelPart() {

        String colour = LEVEL_COLOUR_MAP.get(0);
        int dungeonLevel = dungeonEntity.getDungeonLevel();

        for(Map.Entry<Integer, String> entry : LEVEL_COLOUR_MAP.entrySet()) {
            if (dungeonLevel < entry.getKey()) break;
            colour = entry.getValue();
        }
        return LEVEL_PREFIX + colour + dungeonLevel;
    }

    private String createNamePart() {

        MobName mobName = dungeonEntity.getMobName();
        String entityName;
        String name = mobName.getName();

        if (mobName.isOverrideName()) {
            entityName = name;
        } else {
            EntityType entityType = dungeonEntity.getEntity().getType();

            entityName = ENTITY_NAME_CACHE.get(entityType);
            if (entityName == null) {
                entityName = createNameFromEnum(entityType);
                ENTITY_NAME_CACHE.put(entityType, entityName);
            }
            entityName = entityName + name;
        }
        return entityName;
    }

    private String createNameFromEnum(EntityType entityType) {

        StringBuilder cleanedName = new StringBuilder();

        String[] splitName = entityType.name().split("_");

        for (String part : splitName) {
            if (part.length() == 1) {
                cleanedName.append(part).append(" ");
                continue;
            }

            String restInLowerCase = part.substring(1).toLowerCase();

            String full = part.charAt(0) + restInLowerCase + " ";

            cleanedName.append(full);
        }
        return cleanedName.toString();
    }
}