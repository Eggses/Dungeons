package me.Eggses.dungeons.dungeon;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public abstract class DungeonInstance {

    private static final int PORTAL_OPEN_DURATION_TICKS = 120 * 20;

    private final JavaPlugin plugin;
    private World dungeonWorld = null;

    public DungeonInstance(JavaPlugin plugin, DungeonManager dungeonManager, String dungeonTemplateFileName) {

        this.plugin = plugin;

        DungeonCreation dungeonCreation = new DungeonCreation(
                plugin,
                dungeonTemplateFileName,
                produceInstanceName(),
                world -> {
                    this.dungeonWorld = world;
                    startDungeon();
                },
                exception -> {
                    plugin.getLogger().log(Level.SEVERE, "Error In Creation", exception);
                });
        dungeonCreation.createInstance();
    }

    private void startDungeon() {
        openPortal();
        Bukkit.getScheduler().runTaskLater(plugin, this::closePortal, PORTAL_OPEN_DURATION_TICKS);
    }

    public abstract void openPortal();
    public abstract void closePortal();

    public abstract String produceInstanceName();

    public @Nullable World getDungeonWorld() {
        return dungeonWorld;
    }

    public boolean isInDungeon(Player player) {
        if (dungeonWorld == null) return false;
        return dungeonWorld.getPlayers().contains(player);
    }

    /*
    public boolean isInNormalWorldPortalRoom() {
        return false;

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
    }

    rmeove sadles + bundles on entry:
    maybe on teleport can do this idk?

    OR dont let people take portal with those items in thier inventory!
    thats how you do it....

    and then the keysotne has a can I enter Dungeon button? click it it says Yes, all good
            or No. due to Item: SADDLE, Item:Bundle_white or Item: Bundle_red etc.

     */
}