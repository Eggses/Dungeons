package me.Eggses.dungeons.listenersOLD;

import me.Eggses.dungeons.entities.equipment.ArmourCreator;
import me.Eggses.dungeons.entities.equipment.WeaponEquipment;
import me.Eggses.dungeons.entities.eventbehaviour.EntityEventBehaviour;
import me.Eggses.dungeons.entities.eventbehaviour.damage.FireImpact;
import me.Eggses.dungeons.entities.eventbehaviour.damage.FrostImpact;
import me.Eggses.dungeons.entities.eventbehaviour.explosion.BeeExplosion;
import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.mobs.MobType;
import me.Eggses.dungeons.entities.taskbehaviour.EntityRepeatingTask;
import me.Eggses.dungeons.entities.taskbehaviour.EntityTaskBehaviour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class AdminTestingDelete implements Listener {

    private final EntityManager entityManager;

    public AdminTestingDelete(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    /*
    ./gradlew build

    Then build -> libs -> and its there.

     */


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        Location location = event.getBlock().getLocation().add(2, 0, 2);

        MobBuilder builder = new MobBuilder(Skeleton.class, location)
                .dungeonLevel(5)
                .count(2)
                .weaponEquipment(new WeaponEquipment(Material.IRON_AXE, Material.IRON_SWORD))
                .armourEquipment(new ArmourCreator(
                        ArmourCreator.ArmorMaterial.IRON,
                        TrimPattern.SILENCE,
                        TrimMaterial.REDSTONE)
                        .generateTopHalfOfSet())
                .mobNameAndSpawnFinalizer(MobType.KNIGHT)
                .entityEventBehaviour(new EntityEventBehaviour()
                        .addEventBehaviour(new FireImpact())
                        .addEventBehaviour(new FrostImpact()
                        ));

        MobBuilder fiend  = new MobBuilder(Husk.class, location)
                .dungeonLevel(20)
                .mobNameAndSpawnFinalizer(MobType.FIEND)
                .weaponEquipment(new WeaponEquipment(Material.IRON_AXE));


        MobBuilder taskMob = new MobBuilder(Bogged.class, location)
                .entityTaskBehaviour(new EntityTaskBehaviour().addEntityTask(new EntityRepeatingTask(
                        ((dungeonEntity, taskManager) -> {
                            Player player = Bukkit.getPlayer("DarkestDepths");
                            assert player != null;
                            player.sendMessage("Task Ran");
                        }), 0, 20))
                );

        MobBuilder explosive = new MobBuilder(Creeper.class, location)
                .entityEventBehaviour(new EntityEventBehaviour().addEventBehaviour(new BeeExplosion()));

        entityManager.spawnMob(builder);
        entityManager.spawnMob(fiend);
        entityManager.spawnMob(taskMob);
        entityManager.spawnMob(explosive);
    }



}
