package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.dungeon.files.PlayerStats;
import me.Eggses.dungeons.dungeon.types.DungeonType;
import me.Eggses.dungeons.dungeon.files.templates.DungeonInstanceTemplate;
import me.Eggses.dungeons.dungeon.files.DungeonLog;
import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.InstanceNameManager;
import me.Eggses.dungeons.tasks.TaskRunner;
import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class DungeonFactory {

    private final JavaPlugin plugin;
    private final DungeonRegistry dungeonRegistry;
    private final DungeonTemplateRegistry dungeonInstanceTemplateRegistry;
    private final TemplateReservation templateReservation;
    private final DungeonLifecycleService dungeonLifecycleService;
    private final DungeonWorldManager dungeonWorldManager;
    private final BlockRegistry blockRegistry;
    private final InstanceNameManager instanceNameManager;
    private final TaskRunner taskRunner;
    private final PlayerStats playerStats;
    private final MessageCreator messageCreator;
    private final TextFormatter textFormatter;
    private final DungeonLog dungeonLog;
    private final BannedItems bannedItems;

    public DungeonFactory(JavaPlugin plugin,
                          DungeonRegistry dungeonRegistry,
                          DungeonTemplateRegistry dungeonInstanceTemplateRegistry,
                          TemplateReservation templateReservation,
                          DungeonLifecycleService dungeonLifecycleService,
                          DungeonWorldManager dungeonWorldManager,
                          BlockRegistry blockRegistry,
                          InstanceNameManager instanceNameManager,
                          TaskRunner taskRunner,
                          PlayerStats playerStats,
                          MessageCreator messageCreator,
                          TextFormatter textFormatter,
                          DungeonLog dungeonLog,
                          BannedItems bannedItems) {

        this.plugin = plugin;
        this.dungeonRegistry = dungeonRegistry;
        this.dungeonInstanceTemplateRegistry = dungeonInstanceTemplateRegistry;
        this.templateReservation = templateReservation;
        this.dungeonLifecycleService = dungeonLifecycleService;
        this.dungeonWorldManager = dungeonWorldManager;
        this.blockRegistry = blockRegistry;
        this.instanceNameManager = instanceNameManager;
        this.taskRunner = taskRunner;
        this.playerStats = playerStats;
        this.messageCreator = messageCreator;
        this.textFormatter = textFormatter;
        this.dungeonLog = dungeonLog;
        this.bannedItems = bannedItems;
    }

    private void createDungeonInstance(World world,
                                       DungeonInstanceTemplate dungeonInstanceTemplate,
                                       String instanceFileName,
                                       DungeonType dungeonType) {

        var dungeonInstance = new DungeonInstance(
                plugin,
                dungeonLifecycleService,
                world,
                dungeonInstanceTemplate,
                blockRegistry,
                instanceFileName,
                messageCreator,
                textFormatter,
                taskRunner,
                bannedItems,
                dungeonType,
                playerStats
        );
        dungeonRegistry.addDungeonInstance(dungeonInstance);
    }

    private void failToCreateDungeonInstance(Exception e, String templateFileName,
                                             String instanceFileName, DungeonType dungeonType) {

        instanceNameManager.freeFolderName(instanceFileName);
        templateReservation.free(dungeonType);

        plugin.getLogger().log(Level.SEVERE, "Dungeon Failed To Generate: ", e);
        dungeonLog.addError("Dungeon Generation Failure: " + templateFileName + ".");

        Component message = Component
                .text("Dungeon Failed To Generate.")
                .color(TextColor.color(255, 20, 20));

        plugin.getServer().getScheduler().runTask(
                plugin,
                () -> Bukkit.getOnlinePlayers().forEach(
                        player -> player.sendMessage(message))
        );
    }

    public boolean attemptToCreateDungeon(DungeonType dungeonType) {

        if (!templateReservation.isTemplateFree(dungeonType)) return false;
        templateReservation.reserve(dungeonType);

        var template = dungeonInstanceTemplateRegistry.getDungeonInstanceTemplate(dungeonType);
        if (template == null) {
            templateReservation.free(dungeonType);
            throw new IllegalArgumentException("No template found for: " + dungeonType);
        }

        createDungeon(template, dungeonType);
        return true;
    }

    private void createDungeon(DungeonInstanceTemplate dungeonInstanceTemplate, DungeonType dungeonType) {

        String templateFolderName = dungeonInstanceTemplate.getTemplateFolderName();
        String instanceFolderName = instanceNameManager.generateFolderName(dungeonType);

        dungeonWorldManager.attemptToCreateInstance(templateFolderName, instanceFolderName,
                (world) -> createDungeonInstance(world, dungeonInstanceTemplate, instanceFolderName, dungeonType),
                (exception) -> failToCreateDungeonInstance(exception, templateFolderName, instanceFolderName, dungeonType)
        );
    }
}