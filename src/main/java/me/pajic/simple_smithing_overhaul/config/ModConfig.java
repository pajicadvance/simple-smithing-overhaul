package me.pajic.simple_smithing_overhaul.config;

import me.fzzyhmstrs.fzzy_config.annotations.*;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.AllowableStrings;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedChoiceList;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.*;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.util.ChanceAndCount;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.lang.Integer;
import java.util.List;
import java.util.Map;

@Version(version = 1)
public class ModConfig extends Config {
    public ModConfig() {
        super(Main.CONFIG_RL);
    }

    public EnchantmentUpgrading enchantmentUpgrading = new EnchantmentUpgrading();
    public PinnacleEnchantment pinnacleEnchantment = new PinnacleEnchantment();
    public Whetstone whetstone = new Whetstone();
    public StreamlinedRepairs streamlinedRepairs = new StreamlinedRepairs();
    public AnvilImprovements anvilImprovements = new AnvilImprovements();
    public GrindstoneImprovements grindstoneImprovements = new GrindstoneImprovements();
    public EnchantedBookLootTweaks enchantedBookLootTweaks = new EnchantedBookLootTweaks();
    public ImprovedExperienceBottle improvedExperienceBottle = new ImprovedExperienceBottle();
    public EnchantmentLimits enchantmentLimits = new EnchantmentLimits();

    public static class EnchantmentUpgrading extends ConfigSection {
        @RequiresAction(action = Action.RESTART)
        public ValidatedBoolean enableEnchantmentUpgrading = new ValidatedBoolean(true);
        public ValidatedBoolean upgradingHasExperienceCost = new ValidatedBoolean(true);
        public ValidatedInt upgradingBaseExperienceCost = new ValidatedInt(5, Integer.MAX_VALUE, 1);
        public ValidatedBoolean ignoreTooExpensive = new ValidatedBoolean(true);
    }

    public static class PinnacleEnchantment extends ConfigSection {
        @RequiresAction(action = Action.RESTART)
        public ValidatedBoolean enablePinnacleEnchantment = new ValidatedBoolean(true);
        public ValidatedInt pinnacleBaseExperienceCost = new ValidatedInt(30, Integer.MAX_VALUE, 1);
        public ValidatedInt pinnacleExperienceCostIncrease = new ValidatedInt(5, Integer.MAX_VALUE, 1);
        public ValidatedBoolean colorPinnacleItemName = new ValidatedBoolean(true);
        public ValidatedString pinnacleItemNameColor = new ValidatedString("Light Purple", new AllowableStrings(ModUtil.nameColors::contains, () -> ModUtil.nameColors));
        public ValidatedList<ResourceLocation> excludedFromMaxedOutCheck = ValidatedIdentifier.ofSuppliedList(
                ResourceLocation.withDefaultNamespace("mending"),
                () -> ModUtil.enchantmentSuggestions
        ).toList(
                ResourceLocation.withDefaultNamespace("mending"),
                ResourceLocation.withDefaultNamespace("thorns"),
                ResourceLocation.withDefaultNamespace("fire_aspect"),
                ResourceLocation.withDefaultNamespace("punch"),
                ResourceLocation.withDefaultNamespace("knockback")
        );
    }

    public static class Whetstone extends ConfigSection {
        @RequiresAction(action = Action.RESTART)
        public ValidatedBoolean enableWhetstone = new ValidatedBoolean(true);
    }

    public static class StreamlinedRepairs extends ConfigSection {
        @RequiresAction(action = Action.RESTART)
        public ValidatedEnum<NetheriteRepairMaterials> netheriteRepairMaterial = new ValidatedEnum<>(NetheriteRepairMaterials.DIAMOND);
        public ValidatedBoolean modifyAnvilRepairUnitCosts = new ValidatedBoolean(true);
        public Armor armor = new Armor();
        public Tools tools = new Tools();
        public UniqueItems uniqueItems = new UniqueItems();
        @SuppressWarnings("unchecked") @RequiresAction(action = Action.RESTART)
        public ValidatedMap<String, String> modRepairableItems = (new ValidatedMap.Builder())
                .keyHandler(new ValidatedString("diamond_pickaxe", new AllowableStrings(ModUtil.itemSuggestions::contains, () -> ModUtil.itemSuggestions)))
                .valueHandler(new ValidatedString("diamond", new AllowableStrings(ModUtil.itemSuggestions::contains, () -> ModUtil.itemSuggestions)))
                .defaults(Map.of(
                        "another_furniture:furniture_hammer", "#minecraft:planks",
                        "guarding:netherite_shield", "minecraft:diamond",
                        "rearm:netherite_bow", "minecraft:diamond",
                        "rearm:netherite_crossbow", "minecraft:diamond",
                        "#chalk:chalks", "minecraft:calcite",
                        "#chalk:glow_chalks", "minecraft:glow_ink_sac"
                ))
                .build();
        @SuppressWarnings("unchecked")
        public ValidatedMap<String, Integer> modItemUnitCosts = (new ValidatedMap.Builder())
                .keyHandler(new ValidatedString("diamond_pickaxe", new AllowableStrings(ModUtil.itemSuggestions::contains, () -> ModUtil.itemSuggestions)))
                .valueHandler(new ValidatedInt(1, 9, 1))
                .defaults(Map.of(
                        "#farmersdelight:tools/knives", 1,
                        "another_furniture:furniture_hammer", 3,
                        "rearm:netherite_bow", 3,
                        "rearm:netherite_crossbow", 3,
                        "guarding:netherite_shield", 6,
                        "vshorses:horseshoe", 3,
                        "#chalk:chalks", 2,
                        "#chalk:glow_chalks", 1
                ))
                .build();
    }

    public static class AnvilImprovements extends ConfigSection {
        public ValidatedBoolean modifyDegradationChance = new ValidatedBoolean(true);
        public ValidatedFloat degradationChance = new ValidatedFloat(6.0F, 100.0F, 0);
        public ValidatedBoolean freeUnenchantedRepairs = new ValidatedBoolean(true);
        public ValidatedBoolean noWorkCostIncreaseOnRepair = new ValidatedBoolean(true);
        public ValidatedBoolean noPriorWorkCost = new ValidatedBoolean(false);
        public ValidatedBoolean freeRenames = new ValidatedBoolean(true);
        public ValidatedBoolean noTooExpensive = new ValidatedBoolean(true);
    }

    public static class GrindstoneImprovements extends ConfigSection {
        public ValidatedBoolean repairCostReductionRecipe = new ValidatedBoolean(true);
        public ValidatedBoolean increasedDisenchantXpGain = new ValidatedBoolean(true);
    }

    public static class EnchantedBookLootTweaks extends ConfigSection {
        public ValidatedBoolean weightedLevels = new ValidatedBoolean(true);
        @RequiresAction(action = Action.RESTART)
        public ValidatedBoolean additionalChestLoot = new ValidatedBoolean(true);
        @SuppressWarnings("unchecked") @RequiresAction(action = Action.RESTART)
        public ValidatedMap<ResourceLocation, ChanceAndCount> bookLootLocations = (new ValidatedMap.Builder())
                .keyHandler(ValidatedIdentifier.ofDynamicKey(
                        ResourceLocation.withDefaultNamespace("chests/simple_dungeon"),
                        Registries.LOOT_TABLE,
                        "all_loot_tables",
                        (rl, lth) -> rl.getPath().contains("chests") || rl.getPath().contains("gameplay") || rl.getPath().contains("archaeology")
                ))
                .valueHandler(new ValidatedAny<>(new ChanceAndCount()))
                .defaults(Map.<ResourceLocation, ChanceAndCount>ofEntries(
                        Map.entry(ResourceLocation.parse("minecraft:chests/abandoned_mineshaft"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/ancient_city"), new ChanceAndCount(50, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/bastion_other"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/bastion_treasure"), new ChanceAndCount(100, 3)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/buried_treasure"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/desert_pyramid"), new ChanceAndCount(75, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/jungle_temple"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/pillager_outpost"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/nether_bridge"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/simple_dungeon"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/stronghold_corridor"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/stronghold_crossing"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/stronghold_library"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/underwater_ruin_big"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/underwater_ruin_small"), new ChanceAndCount(50, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/woodland_mansion"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("betteroceanmonuments:chests/upper_side_chamber"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("betterjungletemples:chests/treasure"), new ChanceAndCount(100, 2)),
                        Map.entry(ResourceLocation.parse("betterdungeons:spider_dungeon/chests/egg_room"), new ChanceAndCount(50, 1)),
                        Map.entry(ResourceLocation.parse("betterdungeons:skeleton_dungeon/chests/common"), new ChanceAndCount(50, 1)),
                        Map.entry(ResourceLocation.parse("betterdungeons:skeleton_dungeon/chests/middle"), new ChanceAndCount(50, 1)),
                        Map.entry(ResourceLocation.parse("betterdungeons:zombie_dungeon/chests/common"), new ChanceAndCount(50, 1)),
                        Map.entry(ResourceLocation.parse("betterdungeons:zombie_dungeon/chests/special"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("betterdungeons:zombie_dungeon/chests/tombstone"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("betterdungeons:small_nether_dungeon/chests/common"), new ChanceAndCount(50, 1)),
                        Map.entry(ResourceLocation.parse("betterfortresses:chests/keep"), new ChanceAndCount(20, 1)),
                        Map.entry(ResourceLocation.parse("betterfortresses:chests/beacon"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/badlands"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/dark_forest"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/deep"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/desert"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/icy"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/jungle"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/mushroom"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/nether"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/ocean"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/snow"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/swamp"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/basalt"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/birch"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/crimson"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/dark_forest"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/desert"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/end"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/icy"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/jungle"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/nether"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/ocean"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/savanna"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/soul"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/stone"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/swamp"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/taiga"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/warped"), new ChanceAndCount(100, 1))
                ))
                .build();
    }

    public static class ImprovedExperienceBottle extends ConfigSection {
        public ValidatedBoolean modifyXpReward = new ValidatedBoolean(true);
        public ValidatedInt minXp = new ValidatedInt(30, Integer.MAX_VALUE, 1);
        public ValidatedInt maxXp = new ValidatedInt(50, Integer.MAX_VALUE, 1);
        @RequiresAction(action = Action.RESTART) @NonSync @ClientModifiable
        public ValidatedBoolean renameToExperienceBottle = new ValidatedBoolean(true);
        @RequiresAction(action = Action.RESTART)
        public ValidatedBoolean additionalChestLoot = new ValidatedBoolean(true);
        @SuppressWarnings("unchecked") @RequiresAction(action = Action.RESTART)
        public ValidatedMap<ResourceLocation, ChanceAndCount> bottleLootLocations = (new ValidatedMap.Builder())
                .keyHandler(ValidatedIdentifier.ofDynamicKey(
                        ResourceLocation.withDefaultNamespace("chests/simple_dungeon"),
                        Registries.LOOT_TABLE,
                        "all_loot_tables",
                        (rl, lth) -> rl.getPath().contains("chests") || rl.getPath().contains("gameplay") || rl.getPath().contains("archaeology")
                ))
                .valueHandler(new ValidatedAny<>(new ChanceAndCount()))
                .defaults(Map.<ResourceLocation, ChanceAndCount>ofEntries(
                        Map.entry(ResourceLocation.parse("minecraft:chests/abandoned_mineshaft"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/ancient_city"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/end_city_treasure"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/jungle_temple"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/pillager_outpost"), new ChanceAndCount(100, 2)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/simple_dungeon"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/stronghold_corridor"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/stronghold_crossing"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/stronghold_library"), new ChanceAndCount(100, 2)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/woodland_mansion"), new ChanceAndCount(100, 3)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/desert_pyramid"), new ChanceAndCount(75, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/nether_bridge"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/buried_treasure"), new ChanceAndCount(100, 2)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/underwater_ruin_big"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/underwater_ruin_small"), new ChanceAndCount(50, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/bastion_other"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("minecraft:chests/bastion_treasure"), new ChanceAndCount(100, 3)),
                        Map.entry(ResourceLocation.parse("betteroceanmonuments:chests/upper_side_chamber"), new ChanceAndCount(100, 3)),
                        Map.entry(ResourceLocation.parse("betterjungletemples:chests/treasure"), new ChanceAndCount(100, 3)),
                        Map.entry(ResourceLocation.parse("betterdungeons:spider_dungeon/chests/egg_room"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("betterdungeons:skeleton_dungeon/chests/common"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("betterdungeons:skeleton_dungeon/chests/middle"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("betterdungeons:zombie_dungeon/chests/common"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("betterdungeons:zombie_dungeon/chests/special"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("betterdungeons:zombie_dungeon/chests/tombstone"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("betterdungeons:small_nether_dungeon/chests/common"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("betterfortresses:chests/keep"), new ChanceAndCount(50, 1)),
                        Map.entry(ResourceLocation.parse("betterfortresses:chests/beacon"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/badlands"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/dark_forest"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/deep"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/desert"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/icy"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/jungle"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/mushroom"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/nether"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/ocean"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/snow"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/dungeons/swamp"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/basalt"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/birch"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/crimson"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/dark_forest"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/desert"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/end"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/icy"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/jungle"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/nether"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/ocean"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/savanna"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/soul"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/stone"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/swamp"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/taiga"), new ChanceAndCount(100, 1)),
                        Map.entry(ResourceLocation.parse("repurposed_structures:chests/mineshafts/warped"), new ChanceAndCount(100, 1))
                ))
                .build();
    }

    public static class EnchantmentLimits extends ConfigSection {
        public ValidatedBoolean limitEnchantingTablePower = new ValidatedBoolean(true);
        public ValidatedInt enchantingTablePowerLimit = new ValidatedInt(10, 15, 1);
        public ValidatedBoolean limitEnchantedLootPower = new ValidatedBoolean(true);
        public ValidatedInt enchantedLootPowerLimit = new ValidatedInt(20, 50, 1);
        public ValidatedBoolean limitBookTradeLevel = new ValidatedBoolean(true);
        public ValidatedInt bookTradeLevelLimit = new ValidatedInt(1, Integer.MAX_VALUE, 1);
        public ValidatedBoolean limitBookTradeUses = new ValidatedBoolean(true);
        public ValidatedInt bookTradeUsesLimit = new ValidatedInt(3, Integer.MAX_VALUE, 1);
    }

    public static class Armor extends ConfigSection {
        public ValidatedInt headArmorUnits = new ValidatedInt(5, 9, 1);
        public ValidatedInt chestArmorUnits = new ValidatedInt(8, 9, 1);
        public ValidatedInt legArmorUnits = new ValidatedInt(7, 9, 1);
        public ValidatedInt footArmorUnits = new ValidatedInt(4, 9, 1);
        public ValidatedInt horseArmorUnits = new ValidatedInt(6, 9, 1);
        public ValidatedInt wolfArmorUnits = new ValidatedInt(6, 9, 1);;
    }

    public static class Tools extends ConfigSection {
        public ValidatedInt pickaxeUnits = new ValidatedInt(3, 9, 1);
        public ValidatedInt axeUnits = new ValidatedInt(3, 9, 1);
        public ValidatedInt hoeUnits = new ValidatedInt(2, 9, 1);
        public ValidatedInt swordUnits = new ValidatedInt(2, 9, 1);
        public ValidatedInt shovelUnits = new ValidatedInt(1, 9, 1);
    }

    public static class UniqueItems extends ConfigSection {
        public ValidatedInt shieldUnits = new ValidatedInt(6, 9, 1);
        public ValidatedInt elytraUnits = new ValidatedInt(2, 9, 1);
        public ValidatedInt maceUnits = new ValidatedInt(2, 9, 1);
        public ValidatedInt whetstoneUnits = new ValidatedInt(6, 9, 1);
        public ValidatedInt bowUnits = new ValidatedInt(3, 9, 1);
        public ValidatedInt crossbowUnits = new ValidatedInt(3, 9, 1);
        public ValidatedInt flintAndSteelUnits = new ValidatedInt(1, 9, 1);
        public ValidatedInt shearsUnits = new ValidatedInt(2, 9, 1);
        public ValidatedInt tridentUnits = new ValidatedInt(3, 9, 1);
        public ValidatedInt brushUnits = new ValidatedInt(1, 9, 1);
        public ValidatedInt fishingRodUnits = new ValidatedInt(2, 9, 1);
        public ValidatedInt carrotOnAStickUnits = new ValidatedInt(1, 9, 1);
        public ValidatedInt warpedFungusOnAStickUnits = new ValidatedInt(1, 9, 1);
    }
}
