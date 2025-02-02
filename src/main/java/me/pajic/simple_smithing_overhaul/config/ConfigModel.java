package me.pajic.simple_smithing_overhaul.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

import java.util.List;

@Modmenu(modId = "simple_smithing_overhaul")
@Config(name = "simple_smithing_overhaul", wrapperName = "ModConfig")
@Sync(Option.SyncMode.OVERRIDE_CLIENT)
@SuppressWarnings("unused")
public class ConfigModel {
    @Nest public EnchantmentUpgrading enchantmentUpgrading = new EnchantmentUpgrading();
    @Nest public PinnacleEnchantment pinnacleEnchantment = new PinnacleEnchantment();
    @Nest public Whetstone whetstone = new Whetstone();
    @Nest public StreamlinedRepairs streamlinedRepairs = new StreamlinedRepairs();
    @Nest public AnvilImprovements anvilImprovements = new AnvilImprovements();
    @Nest public GrindstoneImprovements grindstoneImprovements = new GrindstoneImprovements();

    public static class EnchantmentUpgrading {
        @RestartRequired public boolean enableEnchantmentUpgrading = true;
        public boolean upgradingHasExperienceCost = true;
        @PredicateConstraint("greaterThanZero") public int upgradingBaseExperienceCost = 5;
        public boolean ignoreTooExpensive = true;

        public static boolean greaterThanZero(int value) {
            return value > 0;
        }
    }

    public static class PinnacleEnchantment {
        @RestartRequired public boolean enablePinnacleEnchantment = true;
        @PredicateConstraint("greaterThanZero") public int pinnacleExperienceCost = 30;
        public List<String> excludedFromMaxedOutCheck = List.of();

        public static boolean greaterThanZero(int value) {
            return value > 0;
        }
    }

    public static class Whetstone {
        @RestartRequired public boolean enableWhetstone = true;
    }

    public static class StreamlinedRepairs {
        @RestartRequired public NetheriteRepairMaterials netheriteRepairMaterial = NetheriteRepairMaterials.NETHERITE_SCRAP;
        public boolean modifyAnvilRepairUnitCosts = true;
        @Nest public Armor armor = new Armor();
        @Nest public Tools tools = new Tools();
        @Nest public UniqueItems uniqueItems = new UniqueItems();
        @RestartRequired public List<String> modRepairableItems = List.of(
                "another_furniture:furniture_hammer;#minecraft:planks",
                "guarding:netherite_shield;minecraft:netherite_scrap",
                "rearm:netherite_bow;minecraft:netherite_scrap",
                "rearm:netherite_crossbow;minecraft:netherite_scrap",
                "chalk:black_chalk;minecraft:calcite",
                "chalk:black_glow_chalk;minecraft:glow_ink_sac",
                "chalk:blue_chalk;minecraft:calcite",
                "chalk:blue_glow_chalk;minecraft:glow_ink_sac",
                "chalk:brown_chalk;minecraft:calcite",
                "chalk:brown_glow_chalk;minecraft:glow_ink_sac",
                "chalk:chalk;minecraft:calcite",
                "chalk:glow_chalk;minecraft:glow_ink_sac",
                "chalk:cyan_chalk;minecraft:calcite",
                "chalk:cyan_glow_chalk;minecraft:glow_ink_sac",
                "chalk:gray_chalk;minecraft:calcite",
                "chalk:gray_glow_chalk;minecraft:glow_ink_sac",
                "chalk:green_chalk;minecraft:calcite",
                "chalk:green_glow_chalk;minecraft:glow_ink_sac",
                "chalk:light_blue_chalk;minecraft:calcite",
                "chalk:light_blue_glow_chalk;minecraft:glow_ink_sac",
                "chalk:light_gray_chalk;minecraft:calcite",
                "chalk:light_gray_glow_chalk;minecraft:glow_ink_sac",
                "chalk:lime_chalk;minecraft:calcite",
                "chalk:lime_glow_chalk;minecraft:glow_ink_sac",
                "chalk:magenta_chalk;minecraft:calcite",
                "chalk:magenta_glow_chalk;minecraft:glow_ink_sac",
                "chalk:orange_chalk;minecraft:calcite",
                "chalk:orange_glow_chalk;minecraft:glow_ink_sac",
                "chalk:pink_chalk;minecraft:calcite",
                "chalk:pink_glow_chalk;minecraft:glow_ink_sac",
                "chalk:purple_chalk;minecraft:calcite",
                "chalk:purple_glow_chalk;minecraft:glow_ink_sac",
                "chalk:red_chalk;minecraft:calcite",
                "chalk:red_glow_chalk;minecraft:glow_ink_sac",
                "chalk:yellow_chalk;minecraft:calcite",
                "chalk:yellow_glow_chalk;minecraft:glow_ink_sac"
        );
        public List<String> modItemUnitCosts = List.of(
                "#farmersdelight:tools/knives;1",
                "another_furniture:furniture_hammer;3",
                "rearm:netherite_bow;1",
                "rearm:netherite_crossbow;1",
                "guarding:netherite_shield;1",
                "vshorses:horseshoe;3",
                "chalk:black_chalk;2",
                "chalk:black_glow_chalk;1",
                "chalk:blue_chalk;2",
                "chalk:blue_glow_chalk;1",
                "chalk:brown_chalk;2",
                "chalk:brown_glow_chalk;1",
                "chalk:chalk;2",
                "chalk:glow_chalk;1",
                "chalk:cyan_chalk;2",
                "chalk:cyan_glow_chalk;1",
                "chalk:gray_chalk;2",
                "chalk:gray_glow_chalk;1",
                "chalk:green_chalk;2",
                "chalk:green_glow_chalk;1",
                "chalk:light_blue_chalk;2",
                "chalk:light_blue_glow_chalk;1",
                "chalk:light_gray_chalk;2",
                "chalk:light_gray_glow_chalk;1",
                "chalk:lime_chalk;2",
                "chalk:lime_glow_chalk;1",
                "chalk:magenta_chalk;2",
                "chalk:magenta_glow_chalk;1",
                "chalk:orange_chalk;2",
                "chalk:orange_glow_chalk;1",
                "chalk:pink_chalk;2",
                "chalk:pink_glow_chalk;1",
                "chalk:purple_chalk;2",
                "chalk:purple_glow_chalk;1",
                "chalk:red_chalk;2",
                "chalk:red_glow_chalk;1",
                "chalk:yellow_chalk;2",
                "chalk:yellow_glow_chalk;1"
        );
    }

    public static class AnvilImprovements {
        public boolean modifyDegradationChance = true;
        @RangeConstraint(min = 0.0F, max = 100.0F, decimalPlaces = 1) public float degradationChance = 6.0F;
        public boolean freeUnenchantedRepairs = true;
        public boolean noWorkCostIncreaseOnRepair = true;
        public boolean noPriorWorkCost = false;
        public boolean freeRenames = true;
        public boolean noTooExpensive = true;
    }

    public static class GrindstoneImprovements {
        public boolean repairCostReductionRecipe = true;
        public boolean increasedDisenchantXpGain = true;
    }

    public static class Armor {
        public int headArmorUnits = 5;
        public int chestArmorUnits = 8;
        public int legArmorUnits = 7;
        public int footArmorUnits = 4;
        public int horseArmorUnits = 6;
        public int wolfArmorUnits = 6;
    }

    public static class Tools {
        public int pickaxeUnits = 3;
        public int axeUnits = 3;
        public int hoeUnits = 2;
        public int swordUnits = 2;
        public int shovelUnits = 1;
    }

    public static class UniqueItems {
        public int shieldUnits = 6;
        public int elytraUnits = 2;
        public int maceUnits = 2;
        public int whetstoneUnits = 6;
        public int bowUnits = 3;
        public int crossbowUnits = 2;
        public int flintAndSteelUnits = 1;
        public int shearsUnits = 2;
        public int tridentUnits = 3;
        public int brushUnits = 1;
        public int fishingRodUnits = 2;
        public int carrotOnAStickUnits = 1;
        public int warpedFungusOnAStickUnits = 1;
    }

    public enum NetheriteRepairMaterials {
        NETHERITE_INGOT, NETHERITE_SCRAP, DIAMOND
    }
}
