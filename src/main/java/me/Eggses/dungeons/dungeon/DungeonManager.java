package me.Eggses.dungeons.dungeon;

import me.Eggses.dungeons.configuration.DungeonLog;
import me.Eggses.dungeons.dungeon.baseinstance.MalignantMarsh;
import me.Eggses.dungeons.dungeon.utility.InstanceNameManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class DungeonManager {

    private final Map<World, DungeonInstance> dungeonInstances = new HashMap<>();
    private final Map<Long, DungeonInstance> instancesWithOpenPortals = new HashMap<>();

    private final JavaPlugin plugin;
    private final InstanceNameManager instanceNameManager;

    public DungeonManager(JavaPlugin plugin, DungeonLog dungeonLog) {
        this.plugin = plugin;
        instanceNameManager = new InstanceNameManager();
    }

    public boolean isInDungeon(Player player) {
        DungeonInstance dungeonInstance = dungeonInstances.get(player.getWorld());
        if (dungeonInstance == null) return false;

        return dungeonInstance.isInDungeon(player);
    }

    public void handleMovementEventInWorld(Player player, long chunkKey) {

        DungeonInstance dungeonInstance = instancesWithOpenPortals.get(chunkKey);
        if (dungeonInstance == null) return;

        dungeonInstance.handleMovementEventInWorld(player);
    }

    public void addToOpenPortals(DungeonInstance dungeonInstance, Set<Long> portalChunkKeys) {

        for (Long chunkKey : portalChunkKeys) {
            instancesWithOpenPortals.put(chunkKey, dungeonInstance);
        }
    }

    public void removeFromOpenPortals(Set<Long> portalChunkKeys) {

        for (Long chunkKey : portalChunkKeys) {
            instancesWithOpenPortals.remove(chunkKey);
        }
    }

    public void handleMovementEventInDungeon(Player player) {
        DungeonInstance dungeonInstance = dungeonInstances.get(player.getWorld());
        if (dungeonInstance == null) return;

        dungeonInstance.handleMovementEventInDungeon(player);
    }

    public boolean isInNormalWorldPortalRoom(Player player) {
        /*
        THis wont work.... nothing to do with being in the wolrd...
        will always return false!
         */

        /*
        Okay this is used to prevent item drop: this is a very indpeedant thing
        from the dungeon instance... as it can exist without an instance right?

        portal room + the keystone is awlays there... so this isnt related to a
        Dungeon instance and probably should not be in this class...

         */
        return false;
    }

    public Optional<DungeonInstance> getDungeonInstance(World world) {
        DungeonInstance dungeonInstance = dungeonInstances.get(world);
        return Optional.ofNullable(dungeonInstance);
    }

    public void addDungeonInstance(DungeonInstance dungeonInstance) {
        World world = dungeonInstance.getDungeonWorld();
        if (world == null) return;
        dungeonInstances.put(world, dungeonInstance);
    }

    public void removeDungeonInstance(World world) {
        DungeonInstance dungeonInstance = dungeonInstances.remove(world);
        if (dungeonInstance == null) return;

        freeFolderName(dungeonInstance.getInstanceFileName());
    }

    public void freeFolderName(String instanceFolderName) {
        instanceNameManager.freeFolderName(instanceFolderName);
    }


/*

    https://chatgpt.com/share/693a48b8-f368-8001-a98b-5df54030c623

    SO IDea:

    Map<DungeonLevelKey, NotRunnableSomeObjectExplainedBelow> = linked map.

    Map<Region, DungeonLevelKEy> linkedHashMap...

    Map<something like button press, DungeonLevelKey >

    So. reigon ocntains a world, and 2 x and 2 y and 2 z fields.
    Simply, add regions in order of encoruter, iterate through linked hasmap and
            its probably going to use break; becuase you will find a region that you fit in
            first likley...
    then once you use a Dungeonkey, remove it from the map


    does each instance track its own mobs... porbably.


A Runnable acraully is more complex:
its a DungeonZone object

with a

void onStart();
void onEnd(); // opens the door to the next room...

each time mob dies... check if entity manager is empty, if it is run onEnd...

while a Zone is active... the movememnt check mentioned above... auto ends... consider:

onMove(Event event) {

if (player not in a dungeon, return)
Dungeon dungeonPlayerIsIn = ...

if (dungeonPlayerisIn.getActiveZone()) return;

iterate over a LinkedHashMap, then...

for region in key set...

if key.isWithin(player.getLocation);
break; and run the zones onStart(); method which also sets the flag to true

maybe onStart() {
spawn();
flag = true
}
where spawn is the abstract method zones implement.

if region order is the encounter order... and you use a linkedhashmap... the chance is that during
iteration, if a player is in a region... so no early return... then you iterate... and check
the more likley ones first...








    can now prevent item drop in that room
            check for bundles there and saddles dont let entry with them

    monitor harrisons world for bad commands...
    track all commands... and sever warning of the command if it was not tracked..


    or check a massive region...
    if a command block runs inside that... it okay
            else prevent it?




 */


    public void createDungeon(DungeonType dungeonType) {
        switch (dungeonType) {

            case MALIGNANT_MARSH -> new MalignantMarsh(plugin);

            default -> {
            }
        }
    }

    public enum DungeonType {
        MALIGNANT_MARSH(),
    }
}
