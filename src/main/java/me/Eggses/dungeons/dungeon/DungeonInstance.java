package me.Eggses.dungeons.dungeon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public abstract class DungeonInstance {

    private static final int PORTAL_OPEN_DURATION_TICKS = 120 * 20;

    private final JavaPlugin plugin;
    private final DungeonManager dungeonManager;
    private World dungeonWorld = null;
    private boolean failedToCreate = false;

    private final Set<UUID> playersInWorld = new HashSet<>();

    public DungeonInstance(JavaPlugin plugin, DungeonManager dungeonManager, String dungeonTemplateFileName) {

        this.plugin = plugin;
        this.dungeonManager = dungeonManager;

        DungeonCreation dungeonCreation = new DungeonCreation(
                plugin,
                dungeonTemplateFileName,
                produceInstanceName(),
                this::commenceDungeon,
                this::errorCreatingDungeon);

        dungeonCreation.attemptToCreateInstance();
    }

    private void commenceDungeon(World world) {
        this.dungeonWorld = world;
        dungeonManager.addDungeonInstance(this);
        addGameRules();
        openPortal();
        Bukkit.getScheduler().runTaskLater(plugin, this::closePortal, PORTAL_OPEN_DURATION_TICKS);
    }

    private void errorCreatingDungeon(Exception exception) {
        failedToCreate = true;
        plugin.getLogger().log(Level.SEVERE, "Dungeon Failed To Generate: ", exception);

        Component message = Component
                .text("Dungeon Failed To Generate.")
                .color(TextColor.color(255, 20, 20));

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    public boolean getFailedToCreate() {
        return failedToCreate;
    }

    public abstract void openPortal();
    public abstract void closePortal();
    public abstract String produceInstanceName();

    // Not Optional: This should only ever be called when NOT null.
    public @Nullable World getDungeonWorld() {
        return dungeonWorld;
    }

    public void addPlayer(Player player) {
        playersInWorld.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        playersInWorld.remove(player.getUniqueId());
    }

    public boolean isInDungeon(Player player) {
        if (dungeonWorld == null) return false;
        return playersInWorld.contains(player.getUniqueId());
    }

    public boolean isInNormalWorldPortalRoom() {
        return false;
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

    private void addGameRules() {
        if (dungeonWorld == null) return;
        dungeonWorld.setGameRule(GameRule.COMMAND_BLOCKS_ENABLED, true);
        dungeonWorld.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, true);
        dungeonWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
        dungeonWorld.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
        dungeonWorld.setGameRule(GameRule.DISABLE_RAIDS, true);
        dungeonWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        dungeonWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        dungeonWorld.setGameRule(GameRule.DO_FIRE_TICK, false);
        dungeonWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        dungeonWorld.setGameRule(GameRule.DO_MOB_LOOT, false);
        dungeonWorld.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        dungeonWorld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        dungeonWorld.setGameRule(GameRule.MOB_GRIEFING, false);
        dungeonWorld.setGameRule(GameRule.DO_VINES_SPREAD, false);
        dungeonWorld.setGameRule(GameRule.SNOW_ACCUMULATION_HEIGHT, 0);
        dungeonWorld.setGameRule(GameRule.UNIVERSAL_ANGER, true);
        dungeonWorld.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
        dungeonWorld.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        dungeonWorld.setGameRule(GameRule.PROJECTILES_CAN_BREAK_BLOCKS, false);
        dungeonWorld.setGameRule(GameRule.FORGIVE_DEAD_PLAYERS, false);
    }
}