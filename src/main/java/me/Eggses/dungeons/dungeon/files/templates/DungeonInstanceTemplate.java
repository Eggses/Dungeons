package me.Eggses.dungeons.dungeon.files.templates;

import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.bosses.BossArenaTemplate;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.utility.DungeonContext;

import java.util.function.Consumer;

@SuppressWarnings("ClassCanBeRecord")
public class DungeonInstanceTemplate {

    private final String templateFolderName;
    private final String dungeonName;
    private final DungeonPortal dungeonPortal;
    private final AreaControllerBuilder areaControllerBuilder;
    private final Consumer<DungeonContext> onDungeonStart;
    private final BossArenaTemplate bossArenaTemplate;

    public DungeonInstanceTemplate(String templateFolderName,
                                   String dungeonName,
                                   DungeonPortal dungeonPortal,
                                   AreaControllerBuilder areaControllerBuilder,
                                   Consumer<DungeonContext> onDungeonStart,
                                   BossArenaTemplate bossArenaTemplate) {

        this.templateFolderName = templateFolderName;
        this.dungeonName = dungeonName;
        this.dungeonPortal = dungeonPortal;
        this.areaControllerBuilder = areaControllerBuilder;
        this.onDungeonStart = onDungeonStart;
        this.bossArenaTemplate = bossArenaTemplate;
    }

    public String getTemplateFolderName() {
        return templateFolderName;
    }

    public String getDungeonName() {
        return dungeonName;
    }

    public DungeonPortal getDungeonPortal() {
        return dungeonPortal;
    }

    public AreaControllerBuilder getAreaControllerBuilder() {
        return areaControllerBuilder;
    }

    public Consumer<DungeonContext> getOnDungeonStart() {
        return onDungeonStart;
    }

    public BossArenaTemplate getBossArenaTemplate() {
        return bossArenaTemplate;
    }
}