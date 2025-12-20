package me.Eggses.dungeons.dungeon.lifecycle;

import me.Eggses.dungeons.dungeon.files.DungeonLog;
import me.Eggses.dungeons.dungeon.instance.DungeonInstance;
import me.Eggses.dungeons.dungeon.instance.configurations.DungeonTemplate;
import me.Eggses.dungeons.dungeon.instance.configurations.FlatTest;
import me.Eggses.dungeons.dungeon.instance.configurations.MalignantMarsh;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.InstanceNameManager;
import me.Eggses.dungeons.entities.tasks.TaskManager;
import me.Eggses.dungeons.utility.MessageCreator;
import me.Eggses.dungeons.utility.SoundPlayer;
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
    private final TaskManager taskManager;
    private final MessageCreator messageCreator;
    private final SoundPlayer soundPlayer;
    private final DungeonLog dungeonLog;
    private final BannedItems bannedItems;

    public DungeonFactory(JavaPlugin plugin,
                          DungeonRegistry dungeonRegistry,
                          DungeonInstanceCoordinator dungeonInstanceCoordinator,
                          DungeonWorldManager dungeonWorldManager,
                          InstanceNameManager instanceNameManager,
                          TaskManager taskManager,
                          MessageCreator messageCreator,
                          SoundPlayer soundPlayer,
                          DungeonLog dungeonLog,
                          BannedItems bannedItems) {

        this.plugin = plugin;
        this.dungeonRegistry = dungeonRegistry;
        this.dungeonInstanceCoordinator = dungeonInstanceCoordinator;
        this.dungeonWorldManager = dungeonWorldManager;
        this.instanceNameManager = instanceNameManager;
        this.taskManager = taskManager;
        this.messageCreator = messageCreator;
        this.soundPlayer = soundPlayer;
        this.dungeonLog = dungeonLog;
        this.bannedItems = bannedItems;
    }

    private void createDungeonInstance(World world,
                                       DungeonTemplate dungeonTemplate,
                                       String instanceFileName) {

        var dungeonInstance = new DungeonInstance(
                plugin,
                dungeonInstanceCoordinator,
                world,
                dungeonTemplate,
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
        dungeonLog.addError("Dungeon Generation Failure: " + templateFileName + ".");

        Component message = Component
                .text("Dungeon Failed To Generate.")
                .color(TextColor.color(255, 20, 20));

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    public void createDungeon(DungeonType dungeonType) {

        DungeonTemplate template = getDungeonTemplate(dungeonType);
        String templateFileName = template.getTemplateFolderName();
        String instanceFileName = instanceNameManager.generateFolderName();

        dungeonWorldManager.attemptToCreateInstance(templateFileName, instanceFileName,
                (world) -> createDungeonInstance(world, template, instanceFileName),
                (exception) -> failToCreateDungeonInstance(exception, templateFileName, instanceFileName)
        );
    }

    private DungeonTemplate getDungeonTemplate(DungeonType dungeonType) {
        return switch (dungeonType) {
            case FLAT_TEST -> new FlatTest(plugin, messageCreator, soundPlayer);
            case MALIGNANT_MARSH -> new MalignantMarsh(plugin, messageCreator, soundPlayer);
        };
    }
}