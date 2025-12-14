package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.configuration.DungeonLog;
import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.instance.DungeonConfiguration;
import me.Eggses.dungeons.dungeon.instance.MalignantMarsh;
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
    private final DungeonLifecycleService dungeonLifecycleService;
    private final DungeonWorldManager dungeonWorldManager;
    private final InstanceNameManager instanceNameManager;
    private final MessageCreator messageCreator;
    private final TaskManager taskManager;
    private final DungeonLog dungeonLog;


    public DungeonFactory(JavaPlugin plugin,
                          DungeonRegistry dungeonRegistry,
                          DungeonLifecycleService dungeonLifecycleService,
                          DungeonWorldManager dungeonWorldManager,
                          InstanceNameManager instanceNameManager,
                          MessageCreator messageCreator,
                          TaskManager taskManager,
                          DungeonLog dungeonLog) {

        this.plugin = plugin;
        this.dungeonRegistry = dungeonRegistry;
        this.dungeonLifecycleService = dungeonLifecycleService;
        this.dungeonWorldManager = dungeonWorldManager;
        this.instanceNameManager = instanceNameManager;
        this.messageCreator = messageCreator;
        this.taskManager = taskManager;
        this.dungeonLog = dungeonLog;

    }

    private void createDungeonInstance(World world,
                                       DungeonConfiguration dungeonConfiguration,
                                       String instanceFileName) {

        var dungeonInstance = new DungeonInstance(
                plugin,
                dungeonLifecycleService,
                world,
                dungeonConfiguration,
                instanceFileName,
                messageCreator,
                taskManager
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
        String templateFileName = dungeonType.getTemplateName();
        String instanceFileName = instanceNameManager.generateFolderName();

        dungeonWorldManager.attemptToCreateInstance(templateFileName, instanceFileName,
                (world) -> createDungeonInstance(world, configuration, instanceFileName),
                (exception) -> failToCreateDungeonInstance(exception, templateFileName, instanceFileName)
        );
    }

    private DungeonConfiguration getDungeonConfiguration(DungeonType dungeonType) {
        return switch (dungeonType) {
            case MALIGNANT_MARSH -> new MalignantMarsh();
        };
    }
}
