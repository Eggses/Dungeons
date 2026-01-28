package me.Eggses.dungeons.dungeon.bosses;

import me.Eggses.dungeons.blocks.BlockRegistry;
import me.Eggses.dungeons.dungeon.bosses.phases.Phase;
import me.Eggses.dungeons.dungeon.bosses.phases.Rotation;
import me.Eggses.dungeons.dungeon.bosses.mechanics.BossCustomHealth;
import me.Eggses.dungeons.dungeon.bosses.mechanics.TargetTopDamage;
import me.Eggses.dungeons.dungeon.bosses.swampboss.mechanics.*;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.entities.equipment.ArmourCreator;
import me.Eggses.dungeons.entities.equipment.ArmourEquipment;
import me.Eggses.dungeons.entities.equipment.WeaponEquipment;
import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.mobs.mobtype.MobUtility;
import me.Eggses.dungeons.eventhandler.EventDefinition;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import me.Eggses.dungeons.utility.text.MessageCreator;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.*;
import java.util.function.Supplier;

public class BossRegistry {

    private static final String SWAMP_BOSS = "swamp_boss_rotbloom";

    private final Map<String, Supplier<DungeonBossBuilder>> bossBuilders = new HashMap<>();

    private final MobUtility mobUtility;
    private final MessageCreator messageCreator;
    private final SoundPlayer soundPlayer;
    private final BlockRegistry blockRegistry;

    public BossRegistry(MobUtility mobUtility,
                        MessageCreator messageCreator,
                        SoundPlayer soundPlayer,
                        BlockRegistry blockRegistry) {

        this.mobUtility = mobUtility;
        this.messageCreator = messageCreator;
        this.soundPlayer = soundPlayer;
        this.blockRegistry = blockRegistry;

        addSwampBoss();
    }

    public Supplier<DungeonBossBuilder> getDungeonBossBuilder(String boss) {
        return bossBuilders.get(boss);
    }

    @SuppressWarnings("ExtractMethodRecommender")
    public void addSwampBoss() {
        bossBuilders.put(SWAMP_BOSS, () -> {

            WeaponEquipment weaponEquipment = new WeaponEquipment(Material.DIAMOND_SWORD);
            weaponEquipment.alterAllItems(itemMeta -> itemMeta.addEnchant(Enchantment.BANE_OF_ARTHROPODS, 1, true));

            ArmourEquipment armourEquipment = new ArmourCreator(
                    ArmourCreator.ArmourSetMaterial.DIAMOND,
                    TrimPattern.FLOW,
                    TrimMaterial.EMERALD)
                    .generateFullSet();
            armourEquipment.alterAllArmour(itemMeta -> itemMeta.addEnchant(Enchantment.BLAST_PROTECTION, 1, true));

            MobBuilder swampMobBuilder = new MobBuilder(EntityType.BOGGED, new Position(-1182, 67, 99));
            swampMobBuilder
                    .count(1)
                    .dungeonLevel(1)
                    .weaponEquipment(weaponEquipment)
                    .armourEquipment(armourEquipment)
                    .spawnChanges(dungeonEntity -> {
                        var ac = dungeonEntity.getAttributeController();
                        ac.setBaseAttribute(Attribute.SCALE, 1.1);
                        ac.setBaseAttribute(Attribute.MOVEMENT_SPEED, 0.28);

                        PathfinderMob pathfinderMob = mobUtility.toPathFinderMobWithClearedGoal(dungeonEntity.getEntity());
                        if (pathfinderMob == null) return;

                        mobUtility.toZombieStyleMeleeGoals(pathfinderMob);
                    });

            final MossController mossController = new MossController(blockRegistry);
            final Harvest harvest = new Harvest();

            Phase phase1 = new Phase.PhaseBuilder(100.0)
                    .addPermanentEvent(new EventDefinition<>(EntityDamageEvent.class, BossCustomHealth::new))
                    .addPermanentEvent(new EventDefinition<>(EntityDamageByEntityEvent.class, TargetTopDamage::new))
                    .addPermanentEvent(new EventDefinition<>(EntityDamageByEntityEvent.class, InfectedHit::new))
                    .addOneOffTask(new Enrage().getTask())
                    .build();

            var poison = new Poison(messageCreator, soundPlayer).getTask();
            var overwhelmingFungus = new OverwhelmingFungus(mossController, messageCreator, soundPlayer).getTask();
            var fungalExplosion = new FungalExplosion(mossController, messageCreator, soundPlayer).getTask();

            List<Rotation.RotationStep> rotationSteps = List.of(
                    new Rotation.RotationStep(20 * 20, poison),
                    new Rotation.RotationStep(22 * 20, fungalExplosion),
                    new Rotation.RotationStep(15 * 20, poison),
                    new Rotation.RotationStep(20 * 20, overwhelmingFungus),
                    new Rotation.RotationStep(26 * 20, fungalExplosion)
            );

            var fireBurstExplosion = new FireBurstExplosion(harvest, mossController, soundPlayer);
            var lightningFireController = new LightningFireController(blockRegistry, fireBurstExplosion);

            Phase phase2 = new Phase.PhaseBuilder(75.0)
                    .addPermanentEvent(new EventDefinition<>(EntityDamageByEntityEvent.class, () -> new Execute(soundPlayer)))
                    .addOneOffTask(new HarvestIncreaseOverTime(harvest, messageCreator, soundPlayer).getTask())
                    .addOneOffTask(new LightningFire(lightningFireController).getTask())
                    .addPermanentEvent(new EventDefinition<>(EntityDamageByEntityEvent.class, () -> new IncreaseDamage(harvest)))
                    .addRotationStepList(rotationSteps)
                    .build();

            Component name = messageCreator.createMessage("<gradient:#8b0000:#c91f1f:#ff3b3b:#b11212>Rotbloom Ascendant");

            return new DungeonBossBuilder()
                    .mobBuilder(swampMobBuilder)
                    .bossName(name)
                    .colourScheme("<magenta>")
                    .health(2000.0)
                    .style(new BossBarController.Style(
                            name,
                            BossBar.Color.PURPLE,
                            BossBar.Overlay.NOTCHED_10,
                            Set.of(BossBar.Flag.DARKEN_SCREEN)))
                    .phases(List.of(phase1, phase2))
                    .addCleanUp(() -> {
                        mossController.removeAllMoss();
                        lightningFireController.removeAllFire();
                    });
        });
    }
}
