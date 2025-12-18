package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.files.misc.DungeonLog;
import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.instance.configurations.DungeonConfiguration;
import me.Eggses.dungeons.dungeon.instance.configurations.FlatTest;
import me.Eggses.dungeons.dungeon.instance.configurations.MalignantMarsh;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.InstanceNameManager;
import me.Eggses.dungeons.entities.taskbehaviour.TaskManager;
import me.Eggses.dungeons.utility.MessageCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class DungeonFactory {

    private final JavaPlugin plugin;
    private final DungeonRegistry dungeonRegistry;
    private final DungeonInstanceCoordinator dungeonInstanceCoordinator;
    private final DungeonWorldManager dungeonWorldManager;
    private final InstanceNameManager instanceNameManager;
    private final MessageCreator messageCreator;
    private final TaskManager taskManager;
    private final DungeonLog dungeonLog;
    private final BannedItems bannedItems;

    public DungeonFactory(JavaPlugin plugin,
                          DungeonRegistry dungeonRegistry,
                          DungeonInstanceCoordinator dungeonInstanceCoordinator,
                          DungeonWorldManager dungeonWorldManager,
                          InstanceNameManager instanceNameManager,
                          MessageCreator messageCreator,
                          TaskManager taskManager,
                          DungeonLog dungeonLog,
                          BannedItems bannedItems) {

        this.plugin = plugin;
        this.dungeonRegistry = dungeonRegistry;
        this.dungeonInstanceCoordinator = dungeonInstanceCoordinator;
        this.dungeonWorldManager = dungeonWorldManager;
        this.instanceNameManager = instanceNameManager;
        this.messageCreator = messageCreator;
        this.taskManager = taskManager;
        this.dungeonLog = dungeonLog;
        this.bannedItems = bannedItems;
    }

    private void createDungeonInstance(World world,
                                       DungeonConfiguration dungeonConfiguration,
                                       String instanceFileName) {

        var dungeonInstance = new DungeonInstance(
                plugin,
                dungeonInstanceCoordinator,
                world,
                dungeonConfiguration,
                instanceFileName,
                messageCreator,
                taskManager,
                bannedItems
        );
        dungeonRegistry.addDungeonInstance(dungeonInstance);
    }

    private void failToCreateDungeonInstance(Exception e, String templateFileName, String instanceFileName) {

        instanceNameManager.freeFolderName(instanceFileName);

        plugin.getLogger().log(Level.SEVERE, "Dungeon Failed To Generate: ", e);
        dungeonLog.addEntry("Dungeon Generation Failure: " + templateFileName + ".");

        Component message = Component
                .text("Dungeon Failed To Generate.")
                .color(TextColor.color(255, 20, 20));

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }


    public void createDungeon(DungeonType dungeonType) {

        DungeonConfiguration configuration = getDungeonConfiguration(dungeonType);
        String templateFileName = configuration.getTemplateFolderName();
        String instanceFileName = instanceNameManager.generateFolderName();

        dungeonWorldManager.attemptToCreateInstance(templateFileName, instanceFileName,
                (world) -> createDungeonInstance(world, configuration, instanceFileName),
                (exception) -> failToCreateDungeonInstance(exception, templateFileName, instanceFileName)
        );
    }

    private DungeonConfiguration getDungeonConfiguration(DungeonType dungeonType) {
        return switch (dungeonType) {
            case TEST_DELETE -> new FlatTest();
            case MALIGNANT_MARSH -> new MalignantMarsh();

        };
    }
}
