package me.Eggses.dungeons.dungeon.files.templates;

import me.Eggses.dungeons.dungeon.files.templates.builders.AreaTemplate;
import me.Eggses.dungeons.dungeon.files.templates.builders.BossTemplate;
import me.Eggses.dungeons.dungeon.files.templates.builders.PortalRoomTemplate;
import me.Eggses.dungeons.dungeon.files.templates.builders.PortalTemplate;
import me.Eggses.dungeons.items.ItemTemplate;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class DungeonTemplate {

    private final String dungeonName;
    private final String templateFileName;
    private final String portalRoomWorld;
    private final PortalRoomTemplate portalRoomTemplate;
    private final ItemTemplate itemTemplate;
    private final List<String> onStart;
    private final PortalTemplate portalTemplate;
    private final List<AreaTemplate> areas;
    private final BossTemplate boss;

    public DungeonTemplate(String dungeonName,
                           String templateFileName,
                           String portalRoomWorld,
                           PortalRoomTemplate portalRoomTemplate,
                           ItemTemplate itemTemplate,
                           List<String> onStart,
                           PortalTemplate portalTemplate,
                           List<AreaTemplate> areas,
                           BossTemplate boss) {

        this.dungeonName = dungeonName;
        this.templateFileName = templateFileName;
        this.portalRoomWorld = portalRoomWorld;
        this.portalRoomTemplate = portalRoomTemplate;
        this.itemTemplate = itemTemplate;
        this.onStart = onStart;
        this.portalTemplate = portalTemplate;
        this.areas = areas;
        this.boss = boss;
    }

    public String getDungeonName() {
        return dungeonName;
    }

    public String getTemplateFileName() {
        return templateFileName;
    }

    public String getPortalRoomWorld() {
        return portalRoomWorld;
    }

    public PortalRoomTemplate getPortalRoomTemplate() {
        return portalRoomTemplate;
    }

    public ItemTemplate getItemTemplate() {
        return itemTemplate;
    }

    public List<String> getOnStart() {
        return onStart;
    }

    public PortalTemplate getPortalTemplate() {
        return portalTemplate;
    }

    public List<AreaTemplate> getAreas() {
        return areas;
    }

    public BossTemplate getBoss() {
        return boss;
    }
}