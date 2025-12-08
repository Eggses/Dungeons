package me.Eggses.dungeons.entities.attributes;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class AttributeController {

    private static final double BASE_HEALTH_PERCENTAGE_INCREASE = 0.10;
    private static final double MELEE_DAMAGE_PERCENTAGE_INCREASE = 0.045;
    private static final double MOVEMENT_SPEED_PERCENTAGE_INCREASE = 0.003;

    private static final double ARMOUR_PER_LEVEL = 0.12;
    private static final double ARMOUR_TOUGHNESS_PER_LEVEL = 0.06;

    private static final double RANGED_DAMAGE_PERCENTAGE_INCREASE = 0.03;
    private static final double CREEPER_DAMAGE_PERCENTAGE_INCREASE = 0.015;
    private static final double MAGIC_DAMAGE_PERCENTAGE_INCREASE = 0.02;

    private static final Map<Attribute, Double> percentageIncreaseAttributeMap = new HashMap<>();
    private static final Map<Attribute, Double> additiveIncreaseAttributeMap = new HashMap<>();

    static {
        percentageIncreaseAttributeMap.put(Attribute.MAX_HEALTH, BASE_HEALTH_PERCENTAGE_INCREASE);
        percentageIncreaseAttributeMap.put(Attribute.ATTACK_DAMAGE, MELEE_DAMAGE_PERCENTAGE_INCREASE);
        percentageIncreaseAttributeMap.put(Attribute.MOVEMENT_SPEED, MOVEMENT_SPEED_PERCENTAGE_INCREASE);

        additiveIncreaseAttributeMap.put(Attribute.ARMOR, ARMOUR_PER_LEVEL);
        additiveIncreaseAttributeMap.put(Attribute.ARMOR_TOUGHNESS, ARMOUR_TOUGHNESS_PER_LEVEL);
    }

    private final DungeonEntity dungeonEntity;

    public AttributeController(DungeonEntity dungeonEntity) {
        this.dungeonEntity = dungeonEntity;
    }

    private static BiFunction<DungeonEntity, Double, Double> damageFormulaBuilder(double constant) {
        return (mob, baseDamage) -> {
            double level = mob.getDungeonLevel();
            double multiplier = 1.0 + (constant * level);
            return baseDamage * multiplier;
        };
    }

    private static final BiFunction<DungeonEntity, Double, Double> EXPLOSION_DAMAGE_FORMULA =
            damageFormulaBuilder(CREEPER_DAMAGE_PERCENTAGE_INCREASE);
    private static final BiFunction<DungeonEntity, Double, Double> RANGED_DAMAGE_FORMULA =
            damageFormulaBuilder(RANGED_DAMAGE_PERCENTAGE_INCREASE);
    private static final BiFunction<DungeonEntity, Double, Double> MAGIC_DAMAGE_FORMULA =
            damageFormulaBuilder(MAGIC_DAMAGE_PERCENTAGE_INCREASE);

    public static BiFunction<DungeonEntity, Double, Double> getExplosionDamageFormula() {
        return EXPLOSION_DAMAGE_FORMULA;
    }

    public static BiFunction<DungeonEntity, Double, Double> getRangedDamageFormula() {
        return RANGED_DAMAGE_FORMULA;
    }

    public static BiFunction<DungeonEntity, Double, Double> getMagicDamageFormula() {
        return MAGIC_DAMAGE_FORMULA;
    }

    private static final TriFunction<AttributeInstance, Double, Integer, Double> PERCENTAGE_INCREASE_FORMULA =
            (attributeInstance, constant, level) ->
                    attributeInstance.getBaseValue() * (1.0 + (constant * level));

    private static final TriFunction<AttributeInstance, Double, Integer, Double> ADDITIVE_INCREASE_FORMULA =
            (attributeInstance, constant, level) ->
                    attributeInstance.getBaseValue() + (constant * level);


    public void setBaseAttribute(Attribute attribute, double value) {
        AttributeInstance attributeInstance = dungeonEntity.getEntity().getAttribute(attribute);
        if (attributeInstance == null) return;

        attributeInstance.setBaseValue(value);
    }

    public void applyAttributes() {
        int dungeonLevel = dungeonEntity.getDungeonLevel();
        applyAttributesFromMap(percentageIncreaseAttributeMap, PERCENTAGE_INCREASE_FORMULA, dungeonLevel);
        applyAttributesFromMap(additiveIncreaseAttributeMap, ADDITIVE_INCREASE_FORMULA, dungeonLevel);
    }

    private void applyAttributesFromMap(Map<Attribute, Double> attributeMap,
                                        TriFunction<AttributeInstance, Double, Integer, Double> formula,
                                        int dungeonLevel) {

        for (Map.Entry<Attribute, Double> entry : attributeMap.entrySet()) {
            Attribute attribute = entry.getKey();
            double constant = entry.getValue();

            AttributeInstance attributeInstance = dungeonEntity.getEntity().getAttribute(attribute);
            if (attributeInstance == null) continue;

            double updatedBaseValue = formula.apply(attributeInstance, constant, dungeonLevel);
            attributeInstance.setBaseValue(updatedBaseValue);
        }
    }
}