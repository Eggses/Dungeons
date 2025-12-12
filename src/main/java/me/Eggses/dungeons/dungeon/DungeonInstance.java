package me.Eggses.dungeons.dungeon;

import me.Eggses.dungeons.configuration.DungeonLog;
import me.Eggses.dungeons.dungeon.players.DungeonPlayers;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.portals.PortalController;
import me.Eggses.dungeons.dungeon.utility.BannedItems;
import me.Eggses.dungeons.dungeon.utility.GameRules;
import me.Eggses.dungeons.dungeon.utility.InstanceNameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Level;

public abstract class DungeonInstance {

    private final JavaPlugin plugin;
    private final DungeonManager dungeonManager;
    private final DungeonLog dungeonLog;
    private final String templateFileName;
    private final PortalController portalController;

    private final DungeonWorldManager dungeonWorldManager;
    private World dungeonWorld = null;
    private final DungeonPlayers dungeonPlayers = new DungeonPlayers();

    public DungeonInstance(JavaPlugin plugin,
                           DungeonManager dungeonManager,
                           String dungeonTemplateFileName,
                           DungeonPortal dungeonPortal,
                           BannedItems bannedItems,
                           InstanceNameManager instanceNameManager,
                           DungeonLog dungeonLog) {

        this.plugin = plugin;
        this.dungeonManager = dungeonManager;
        this.templateFileName = dungeonTemplateFileName;
        this.portalController = new PortalController(plugin,this, dungeonPortal, bannedItems);
        this.dungeonLog = dungeonLog;

        this.dungeonWorldManager = new DungeonWorldManager(
                plugin, dungeonTemplateFileName, instanceNameManager.generateFolderName());

        this.dungeonWorldManager.attemptToCreateInstance(this::onWorldCreated, this::errorCreatingDungeon);
    }

    private void onWorldCreated(World world) {
        this.dungeonWorld = world;
        dungeonManager.addDungeonInstance(this);
        new GameRules(world).applyRules();
        portalController.openDungeonPortal();
    }

    private void errorCreatingDungeon(Exception exception) {
        dungeonManager.deleteFailedToCreateInstance(this);
        plugin.getLogger().log(Level.SEVERE, "Dungeon Failed To Generate: ", exception);
        dungeonLog.addEntry("Dungeon Generation Failure: " + templateFileName + ".");

        Component message = Component
                .text("Dungeon Failed To Generate.")
                .color(TextColor.color(255, 20, 20));

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    public void closeDungeonPortal() {
        if (!portalController.isOpen()) return;

        portalController.closeDungeonPortal();

        if (dungeonPlayers.isEmpty()) {
            dungeonLog.addEntry("Dungeon was empty after Portal closed. " +
                    "This may be a mistake unless everyone died right at the start somehow.");
            tryEndDungeon();
        }
    }

    public void tryEndDungeon() {

        if (!dungeonPlayers.isEmpty() || portalController.isOpen()) return;

        World mainWorld = Bukkit.getWorlds().getFirst();

        List<String> errors = new ArrayList<>();
        String errorMessage = "Dungeon Set is Empty, Portal is closed, but Dungeon World contains: ";
        for (Player player : dungeonWorld.getPlayers()) {
            String error = errorMessage + player.getName();
            plugin.getLogger().severe(error);
            errors.add(error);

            player.teleport(mainWorld.getSpawnLocation());
        }
        if (!errors.isEmpty()) dungeonLog.addEntryList(errors);

        Bukkit.unloadWorld(dungeonWorld, false);

        dungeonManager.removeDungeonInstance(dungeonWorld);

        dungeonWorldManager.attemptToDeleteInstance(exception -> {
            plugin.getLogger().log(Level.SEVERE, "Dungeon Failed To be Deleted: ", exception);
            dungeonLog.addEntry("Dungeon Deletion Failure: " + templateFileName + ".");
        });
    }

    public void enterDungeon(Player player) {
        portalController.enterDungeon(player, dungeonWorld);
    }

    public boolean isInPortal(Player player) {
        return portalController.isInPortal(player);
    }


    // Not Optional: This should only ever be called when NOT null.
    public @Nullable World getDungeonWorld() {
        return dungeonWorld;
    }

    /*
    this si the approch: hide the objects: make these
    methods that give their features and can do more
    like te remove method.
     */

    public void addPlayer(Player player) {
        dungeonPlayers.add(player);
    }

    public void removePlayer(Player player) {
        dungeonPlayers.remove(player);
        if (dungeonPlayers.isEmpty()) tryEndDungeon();
    }



    public boolean isInDungeon(Player player) {
        if (dungeonWorld == null) return false;
        return dungeonPlayers.contains(player);
    }

    public boolean isInNormalWorldPortalRoom() {
        return false;
        // will fix
    }

        /*
        set keep inventory, no natural spawning etc stuff? maybe in start dungoen method


        this will be like the checking to work out
                like if someone is in a region
                same thing... store 2 points

            return true ifplayer is inside two points

                atually have a region object that stores maybe 2 locations?
            or your cusotom one actually that just ocntains

                class Region:
        Point p1
                Point p2
                        World
                                public Region(Location location).. .get the idea?
                public boolean inside(Location location)... get the idea?
                go with
            if world = this.word
                then check mroe specific

                use point object...
        becuase if you use location you store world twice...
        and world is big...

        maybe store world name not the world? as qorlds have unique names!
        // fix this


    rmeove sadles + bundles on entry:
    maybe on teleport can do this idk?

    OR dont let people take portal with those items in thier inventory!
    thats how you do it....

    and then the keysotne has a can I enter Dungeon button? click it it says Yes, all good
            or No. due to Item: SADDLE, Item:Bundle_white or Item: Bundle_red etc.

     */
}