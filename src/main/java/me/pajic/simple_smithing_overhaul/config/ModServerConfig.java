package me.pajic.simple_smithing_overhaul.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = "simple_smithing_overhaul", bus = EventBusSubscriber.Bus.MOD)
public class ModServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue UPGRADING_HAS_EXPERIENCE_COST;
    private static final ModConfigSpec.IntValue UPGRADING_BASE_EXPERIENCE_COST;
    private static final ModConfigSpec.BooleanValue IGNORE_TOO_EXPENSIVE;

    private static final ModConfigSpec.IntValue PINNACLE_EXPERIENCE_COST;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> EXCLUDED_FROM_MAXED_OUT_CHECK;

    private static final ModConfigSpec.BooleanValue MODIFY_REPAIR_UNIT_COSTS;
    private static final ModConfigSpec.IntValue HEAD_ARMOR_UNITS;
    private static final ModConfigSpec.IntValue CHEST_ARMOR_UNITS;
    private static final ModConfigSpec.IntValue LEG_ARMOR_UNITS;
    private static final ModConfigSpec.IntValue FOOT_ARMOR_UNITS;
    private static final ModConfigSpec.IntValue HORSE_ARMOR_UNITS;
    private static final ModConfigSpec.IntValue WOLF_ARMOR_UNITS;
    private static final ModConfigSpec.IntValue PICKAXE_UNITS;
    private static final ModConfigSpec.IntValue AXE_UNITS;
    private static final ModConfigSpec.IntValue HOE_UNITS;
    private static final ModConfigSpec.IntValue SWORD_UNITS;
    private static final ModConfigSpec.IntValue SHOVEL_UNITS;
    private static final ModConfigSpec.IntValue SHIELD_UNITS;
    private static final ModConfigSpec.IntValue ELYTRA_UNITS;
    private static final ModConfigSpec.IntValue MACE_UNITS;
    private static final ModConfigSpec.IntValue WHETSTONE_UNITS;
    private static final ModConfigSpec.IntValue BOW_UNITS;
    private static final ModConfigSpec.IntValue CROSSBOW_UNITS;
    private static final ModConfigSpec.IntValue FLINT_AND_STEEL_UNITS;
    private static final ModConfigSpec.IntValue SHEARS_UNITS;
    private static final ModConfigSpec.IntValue TRIDENT_UNITS;
    private static final ModConfigSpec.IntValue BRUSH_UNITS;
    private static final ModConfigSpec.IntValue FISHING_ROD_UNITS;
    private static final ModConfigSpec.IntValue CARROT_ON_A_STICK_UNITS;
    private static final ModConfigSpec.IntValue WARPED_FUNGUS_ON_A_STICK_UNITS;

    private static final ModConfigSpec.BooleanValue MODIFY_DEGRADATION_CHANCE;
    private static final ModConfigSpec.DoubleValue DEGRADATION_CHANCE;
    private static final ModConfigSpec.BooleanValue FREE_UNENCHANTED_REPAIRS;
    private static final ModConfigSpec.BooleanValue NO_WORK_COST_INCREASE_ON_REPAIR;
    private static final ModConfigSpec.BooleanValue NO_PRIOR_WORK_COST;
    private static final ModConfigSpec.BooleanValue FREE_RENAMES;
    private static final ModConfigSpec.BooleanValue NO_TOO_EXPENSIVE;

    private static final ModConfigSpec.BooleanValue REPAIR_COST_REDUCTION_RECIPE;
    private static final ModConfigSpec.BooleanValue INCREASED_DISENCHANT_XP_GAIN;

    private static final ModConfigSpec.BooleanValue WEIGHTED_LEVELS;

    private static final ModConfigSpec.BooleanValue MODIFY_BOTTLE_XP_REWARD;
    private static final ModConfigSpec.IntValue BOTTLE_MIN_XP;
    private static final ModConfigSpec.IntValue BOTTLE_MAX_XP;

    private static final ModConfigSpec.BooleanValue LIMIT_ENCHANTING_TABLE_POWER;
    private static final ModConfigSpec.IntValue ENCHANTING_TABLE_POWER_LIMIT;
    private static final ModConfigSpec.BooleanValue LIMIT_ENCHANTED_LOOT_POWER;
    private static final ModConfigSpec.IntValue ENCHANTED_LOOT_POWER_LIMIT;
    private static final ModConfigSpec.BooleanValue LIMIT_BOOK_TRADE_LEVEL;
    private static final ModConfigSpec.IntValue BOOK_TRADE_LEVEL_LIMIT;
    private static final ModConfigSpec.BooleanValue LIMIT_BOOK_TRADE_USES;
    private static final ModConfigSpec.IntValue BOOK_TRADE_USES_LIMIT;

    static {
        BUILDER.comment("Simple Smithing Overhaul Configuration");

        BUILDER.push("enchantmentUpgrading");
            UPGRADING_HAS_EXPERIENCE_COST = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.enchantmentUpgrading.upgradingHasExperienceCost")
                    .define("upgradingHasExperienceCost", true);
            UPGRADING_BASE_EXPERIENCE_COST = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.enchantmentUpgrading.upgradingBaseExperienceCost")
                    .defineInRange("upgradingBaseExperienceCost", 5, 1, Integer.MAX_VALUE);
            IGNORE_TOO_EXPENSIVE = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.enchantmentUpgrading.ignoreTooExpensive")
                    .define("ignoreTooExpensive", true);
        BUILDER.pop();

        BUILDER.push("pinnacleEnchantment");
            PINNACLE_EXPERIENCE_COST = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.pinnacleEnchantment.pinnacleExperienceCost")
                    .defineInRange("pinnacleExperienceCost", 30, 1, Integer.MAX_VALUE);
            EXCLUDED_FROM_MAXED_OUT_CHECK = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.pinnacleEnchantment.excludedFromMaxedOutCheck")
                    .defineListAllowEmpty("excludedFromMaxedOutCheck",List.of(), () -> "", o -> true);
        BUILDER.pop();

        BUILDER.push("streamlinedRepairs");
            MODIFY_REPAIR_UNIT_COSTS = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.modifyRepairUnitCosts")
                    .define("modifyRepairUnitCosts", true);

            BUILDER.push("armor");
                HEAD_ARMOR_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.armor.headArmorUnits")
                        .defineInRange("headArmorUnits", 5, 1, Integer.MAX_VALUE);
                CHEST_ARMOR_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.armor.chestArmorUnits")
                        .defineInRange("chestArmorUnits", 8, 1, Integer.MAX_VALUE);
                LEG_ARMOR_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.armor.legArmorUnits")
                        .defineInRange("legArmorUnits", 7, 1, Integer.MAX_VALUE);
                FOOT_ARMOR_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.armor.footArmorUnits")
                        .defineInRange("footArmorUnits", 4, 1, Integer.MAX_VALUE);
                HORSE_ARMOR_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.armor.horseArmorUnits")
                        .defineInRange("horseArmorUnits", 6, 1, Integer.MAX_VALUE);
                WOLF_ARMOR_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.armor.wolfArmorUnits")
                        .defineInRange("wolfArmorUnits", 6, 1, Integer.MAX_VALUE);
            BUILDER.pop();

            BUILDER.push("tools");
                PICKAXE_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.tools.pickaxeUnits")
                        .defineInRange("pickaxeUnits", 3, 1, Integer.MAX_VALUE);
                AXE_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.tools.axeUnits")
                        .defineInRange("axeUnits", 3, 1, Integer.MAX_VALUE);
                HOE_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.tools.hoeUnits")
                        .defineInRange("hoeUnits", 2, 1, Integer.MAX_VALUE);
                SWORD_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.tools.swordUnits")
                        .defineInRange("swordUnits", 2, 1, Integer.MAX_VALUE);
                SHOVEL_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.tools.shovelUnits")
                        .defineInRange("shovelUnits", 1, 1, Integer.MAX_VALUE);
            BUILDER.pop();

            BUILDER.push("uniqueItems");
                SHIELD_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.uniqueItems.shieldUnits")
                        .defineInRange("shieldUnits", 6, 1, Integer.MAX_VALUE);
                ELYTRA_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.uniqueItems.elytraUnits")
                        .defineInRange("elytraUnits", 2, 1, Integer.MAX_VALUE);
                MACE_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.uniqueItems.maceUnits")
                        .defineInRange("maceUnits", 2, 1, Integer.MAX_VALUE);
                WHETSTONE_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.uniqueItems.whetstoneUnits")
                        .defineInRange("whetstoneUnits", 6, 1, Integer.MAX_VALUE);
                BOW_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.uniqueItems.bowUnits")
                        .defineInRange("bowUnits", 3, 1, Integer.MAX_VALUE);
                CROSSBOW_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.uniqueItems.crossbowUnits")
                        .defineInRange("crossbowUnits", 2, 1, Integer.MAX_VALUE);
                FLINT_AND_STEEL_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.uniqueItems.flintAndSteelUnits")
                        .defineInRange("flintAndSteelUnits", 1, 1, Integer.MAX_VALUE);
                SHEARS_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.uniqueItems.shearsUnits")
                        .defineInRange("shearsUnits", 2, 1, Integer.MAX_VALUE);
                TRIDENT_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.uniqueItems.tridentUnits")
                        .defineInRange("tridentUnits", 3, 1, Integer.MAX_VALUE);
                BRUSH_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.uniqueItems.brushUnits")
                        .defineInRange("brushUnits", 1, 1, Integer.MAX_VALUE);
                FISHING_ROD_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.uniqueItems.fishingRodUnits")
                        .defineInRange("fishingRodUnits", 2, 1, Integer.MAX_VALUE);
                CARROT_ON_A_STICK_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.uniqueItems.carrotOnAStickUnits")
                        .defineInRange("carrotOnAStickUnits", 1, 1, Integer.MAX_VALUE);
                WARPED_FUNGUS_ON_A_STICK_UNITS = BUILDER
                        .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.uniqueItems.warpedFungusOnAStickUnits")
                        .defineInRange("warpedFungusOnAStickUnits", 1, 1, Integer.MAX_VALUE);
            BUILDER.pop();
        BUILDER.pop();

        BUILDER.push("anvilImprovements");
            MODIFY_DEGRADATION_CHANCE = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.modifyDegradationChance")
                    .define("modifyDegradationChance", true);
            DEGRADATION_CHANCE = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.degradationChance")
                    .defineInRange("degradationChance", 6.0, 0.0, 100.0);
            FREE_UNENCHANTED_REPAIRS = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.freeUnenchantedRepairs")
                    .define("freeUnenchantedRepairs", true);
            NO_WORK_COST_INCREASE_ON_REPAIR = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.noWorkCostIncreaseOnRepair")
                    .define("noWorkCostIncreaseOnRepair", true);
            NO_PRIOR_WORK_COST = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.noPriorWorkCost")
                    .define("noPriorWorkCost", false);
            FREE_RENAMES = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.freeRenames")
                    .define("freeRenames", true);
            NO_TOO_EXPENSIVE = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.noTooExpensive")
                    .define("noTooExpensive", true);
        BUILDER.pop();

        BUILDER.push("grindstoneImprovements");
            REPAIR_COST_REDUCTION_RECIPE = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.grindstoneImprovements.repairCostReductionRecipe")
                    .define("repairCostReductionRecipe", true);
            INCREASED_DISENCHANT_XP_GAIN = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.grindstoneImprovements.increasedDisenchantXpGain")
                    .define("increasedDisenchantXpGain", true);
        BUILDER.pop();

        BUILDER.push("enchantedBookLootTweaks");
            WEIGHTED_LEVELS = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.enchantedBookLootTweaks.weightedLevels")
                    .define("weightedLevels", true);
        BUILDER.pop();

        BUILDER.push("improvedExperienceBottle");
            MODIFY_BOTTLE_XP_REWARD = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.improvedExperienceBottle.modifyXpReward")
                    .define("modifyXpReward", true);
            BOTTLE_MIN_XP = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.improvedExperienceBottle.minXp")
                    .defineInRange("minXp", 30, 1, Integer.MAX_VALUE);
            BOTTLE_MAX_XP = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.improvedExperienceBottle.maxXp")
                    .defineInRange("maxXp", 50, 1, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.push("enchantmentLimits");
            LIMIT_ENCHANTING_TABLE_POWER = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.enchantmentLimits.limitEnchantingTablePower")
                    .define("limitEnchantingTablePower", true);
            ENCHANTING_TABLE_POWER_LIMIT = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.enchantmentLimits.enchantingTablePowerLimit")
                    .defineInRange("enchantingTablePowerLimit", 10, 1, 15);
            LIMIT_ENCHANTED_LOOT_POWER = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.enchantmentLimits.limitEnchantedLootPower")
                    .define("limitEnchantedLootPower", true);
            ENCHANTED_LOOT_POWER_LIMIT = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.enchantmentLimits.enchantedLootPowerLimit")
                    .defineInRange("enchantedLootPowerLimit", 20, 1, 50);
            LIMIT_BOOK_TRADE_LEVEL = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.enchantmentLimits.limitBookTradeLevel")
                    .define("limitBookTradeLevel", true);
            BOOK_TRADE_LEVEL_LIMIT = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.enchantmentLimits.bookTradeLevelLimit")
                    .defineInRange("bookTradeLevelLimit", 1, 1, Integer.MAX_VALUE);
            LIMIT_BOOK_TRADE_USES = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.enchantmentLimits.limitBookTradeUses")
                    .define("limitBookTradeUses", true);
            BOOK_TRADE_USES_LIMIT = BUILDER
                    .translation("text.config.simple_smithing_overhaul.option.enchantmentLimits.bookTradeUsesLimit")
                    .defineInRange("bookTradeUsesLimit", 3, 1, Integer.MAX_VALUE);
        BUILDER.pop();
    }

    public static final ModConfigSpec SERVER_SPEC = BUILDER.build();

    public static boolean upgradingHasExperienceCost;
    public static int upgradingBaseExperienceCost;
    public static boolean ignoreTooExpensive;
    public static int pinnacleExperienceCost;
    public static List<String> excludedFromMaxedOutCheck;
    public static boolean modifyAnvilRepairUnitCosts;
    public static int headArmorUnits;
    public static int chestArmorUnits;
    public static int legArmorUnits;
    public static int footArmorUnits;
    public static int horseArmorUnits;
    public static int wolfArmorUnits;
    public static int pickaxeUnits;
    public static int axeUnits;
    public static int hoeUnits;
    public static int swordUnits;
    public static int shovelUnits;
    public static int shieldUnits;
    public static int elytraUnits;
    public static int maceUnits;
    public static int whetstoneUnits;
    public static int bowUnits;
    public static int crossbowUnits;
    public static int flintAndSteelUnits;
    public static int shearsUnits;
    public static int tridentUnits;
    public static int brushUnits;
    public static int fishingRodUnits;
    public static int carrotOnAStickUnits;
    public static int warpedFungusOnAStickUnits;
    public static boolean modifyDegradationChance;
    public static double degradationChance;
    public static boolean freeUnenchantedRepairs;
    public static boolean noWorkCostIncreaseOnRepair;
    public static boolean noPriorWorkCost;
    public static boolean freeRenames;
    public static boolean noTooExpensive;
    public static boolean repairCostReductionRecipe;
    public static boolean increasedDisenchantXpGain;
    public static boolean weightedLevels;
    public static boolean modifyXpReward;
    public static int minXp;
    public static int maxXp;
    public static boolean limitEnchantingTablePower;
    public static int enchantingTablePowerLimit;
    public static boolean limitEnchantedLootPower;
    public static int enchantedLootPowerLimit;
    public static boolean limitBookTradeLevel;
    public static int bookTradeLevelLimit;
    public static boolean limitBookTradeUses;
    public static int bookTradeUsesLimit;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        updateConfig(event);
    }

    @SubscribeEvent
    static void onChange(final ModConfigEvent.Reloading event) {
        updateConfig(event);
    }

    private static void updateConfig(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SERVER_SPEC) {
            upgradingHasExperienceCost = UPGRADING_HAS_EXPERIENCE_COST.get();
            upgradingBaseExperienceCost = UPGRADING_BASE_EXPERIENCE_COST.get();
            ignoreTooExpensive = IGNORE_TOO_EXPENSIVE.get();
            pinnacleExperienceCost = PINNACLE_EXPERIENCE_COST.get();
            excludedFromMaxedOutCheck = new ArrayList<>(EXCLUDED_FROM_MAXED_OUT_CHECK.get());
            modifyAnvilRepairUnitCosts = MODIFY_REPAIR_UNIT_COSTS.get();
            headArmorUnits = HEAD_ARMOR_UNITS.get();
            chestArmorUnits = CHEST_ARMOR_UNITS.get();
            legArmorUnits = LEG_ARMOR_UNITS.get();
            footArmorUnits = FOOT_ARMOR_UNITS.get();
            horseArmorUnits = HORSE_ARMOR_UNITS.get();
            wolfArmorUnits = WOLF_ARMOR_UNITS.get();
            pickaxeUnits = PICKAXE_UNITS.get();
            axeUnits = AXE_UNITS.get();
            hoeUnits = HOE_UNITS.get();
            swordUnits = SWORD_UNITS.get();
            shovelUnits = SHOVEL_UNITS.get();
            shieldUnits = SHIELD_UNITS.get();
            elytraUnits = ELYTRA_UNITS.get();
            maceUnits = MACE_UNITS.get();
            whetstoneUnits = WHETSTONE_UNITS.get();
            bowUnits = BOW_UNITS.get();
            crossbowUnits = CROSSBOW_UNITS.get();
            flintAndSteelUnits = FLINT_AND_STEEL_UNITS.get();
            shearsUnits = SHEARS_UNITS.get();
            tridentUnits = TRIDENT_UNITS.get();
            brushUnits = BRUSH_UNITS.get();
            fishingRodUnits = FISHING_ROD_UNITS.get();
            carrotOnAStickUnits = CARROT_ON_A_STICK_UNITS.get();
            warpedFungusOnAStickUnits = WARPED_FUNGUS_ON_A_STICK_UNITS.get();
            modifyDegradationChance = MODIFY_DEGRADATION_CHANCE.get();
            degradationChance = DEGRADATION_CHANCE.get();
            freeUnenchantedRepairs = FREE_UNENCHANTED_REPAIRS.get();
            noWorkCostIncreaseOnRepair = NO_WORK_COST_INCREASE_ON_REPAIR.get();
            noPriorWorkCost = NO_PRIOR_WORK_COST.get();
            freeRenames = FREE_RENAMES.get();
            noTooExpensive = NO_TOO_EXPENSIVE.get();
            repairCostReductionRecipe = REPAIR_COST_REDUCTION_RECIPE.get();
            increasedDisenchantXpGain = INCREASED_DISENCHANT_XP_GAIN.get();
            weightedLevels = WEIGHTED_LEVELS.get();
            modifyXpReward = MODIFY_BOTTLE_XP_REWARD.get();
            minXp = BOTTLE_MIN_XP.get();
            maxXp = BOTTLE_MAX_XP.get();
            limitEnchantingTablePower = LIMIT_ENCHANTING_TABLE_POWER.get();
            enchantingTablePowerLimit = ENCHANTING_TABLE_POWER_LIMIT.get();
            limitEnchantedLootPower = LIMIT_ENCHANTED_LOOT_POWER.get();
            enchantedLootPowerLimit = ENCHANTED_LOOT_POWER_LIMIT.get();
            limitBookTradeLevel = LIMIT_BOOK_TRADE_LEVEL.get();
            bookTradeLevelLimit = BOOK_TRADE_LEVEL_LIMIT.get();
            limitBookTradeUses = LIMIT_BOOK_TRADE_USES.get();
            bookTradeUsesLimit = BOOK_TRADE_USES_LIMIT.get();
        }
    }
}
