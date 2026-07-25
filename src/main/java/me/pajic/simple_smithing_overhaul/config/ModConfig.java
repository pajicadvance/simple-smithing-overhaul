package me.pajic.simple_smithing_overhaul.config;

import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.ClientModifiable;
import me.fzzyhmstrs.fzzy_config.annotations.NonSync;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.AllowableStrings;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedAny;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedString;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.util.ChanceAndCount;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.resources.Identifier;

import java.util.Map;

@Version(version = 2)
public class ModConfig extends Config {

    public ModConfig() {
        super(SSO.id("config-v2"));
    }

	public StreamlinedRepairs streamlinedRepairs = new StreamlinedRepairs();
	public ItemDestructionPrevention itemDestructionPrevention = new ItemDestructionPrevention();
	public PortableItemRepair portableItemRepair = new PortableItemRepair();
	public MendingRework mendingRework = new MendingRework();
	public AnvilImprovements anvilImprovements = new AnvilImprovements();
	public GrindstoneImprovements grindstoneImprovements = new GrindstoneImprovements();
    public EnchantmentUpgrading enchantmentUpgrading = new EnchantmentUpgrading();
    public PinnacleEnchantment pinnacleEnchantment = new PinnacleEnchantment();
	public EnchantmentLimits enchantmentLimits = new EnchantmentLimits();
    public EnchantedBookLootTweaks enchantedBookLootTweaks = new EnchantedBookLootTweaks();
    public ImprovedExperienceBottle improvedExperienceBottle = new ImprovedExperienceBottle();
	public ModIntegration modIntegration = new ModIntegration();

    public static class EnchantmentUpgrading extends ConfigSection {
        @RequiresAction(action = Action.RESTART)
        public ValidatedBoolean enableEnchantmentUpgrading = new ValidatedBoolean();
        public ValidatedBoolean upgradingHasExperienceCost = new ValidatedBoolean();
        public ValidatedInt upgradingBaseExperienceCost = new ValidatedInt(5, Integer.MAX_VALUE, 1);
        public ValidatedBoolean ignoreTooExpensive = new ValidatedBoolean();
    }

    public static class PinnacleEnchantment extends ConfigSection {
        @RequiresAction(action = Action.RESTART)
        public ValidatedBoolean enablePinnacleEnchantment = new ValidatedBoolean();
        public ValidatedInt pinnacleBaseExperienceCost = new ValidatedInt(30, Integer.MAX_VALUE, 1);
        public ValidatedInt pinnacleExperienceCostIncrease = new ValidatedInt(5, Integer.MAX_VALUE, 1);
        public ValidatedInt maxPinnacleEnchantmentsOnItem = new ValidatedInt(1, Integer.MAX_VALUE, 1);
		public ValidatedEnum<PinnacleDuplicationMaterials> duplicationMaterial = new ValidatedEnum<>(PinnacleDuplicationMaterials.SCULK_CATALYST);
        public ValidatedBoolean colorPinnacleItemName = new ValidatedBoolean();
        public ValidatedString pinnacleItemNameColor = new ValidatedString("Light Purple", new AllowableStrings(ModUtil.colorNames::contains, () -> ModUtil.colorNames));
        public ValidatedList<Identifier> excludedFromMaxedOutCheck = new ValidatedIdentifier(Identifier.withDefaultNamespace("mending")).toList(
                Identifier.withDefaultNamespace("mending"),
                Identifier.withDefaultNamespace("thorns"),
                Identifier.withDefaultNamespace("fire_aspect"),
                Identifier.withDefaultNamespace("punch"),
                Identifier.withDefaultNamespace("knockback")
        );
    }

    public static class PortableItemRepair extends ConfigSection {
        @RequiresAction(action = Action.RESTART)
        public ValidatedBoolean enableWhetstone = new ValidatedBoolean();
		public ValidatedList<String> flintMaterialWhitelist = new ValidatedString(
				"", new AllowableStrings(ModUtil.itemSuggestions::contains, () -> ModUtil.itemSuggestions)
		).toList(
				"#minecraft:wooden_tool_materials",
				"#minecraft:stone_tool_materials",
				"#minecraft:copper_tool_materials",
				"#minecraft:iron_tool_materials",
				"#minecraft:repairs_leather_armor",
				"#minecraft:repairs_copper_armor",
				"#minecraft:repairs_chain_armor",
				"#minecraft:repairs_iron_armor",
				"minecraft:flint",
				"minecraft:string",
				"minecraft:feather",
				"minecraft:carrot",
				"minecraft:warped_fungus"
		);
    }

    @SuppressWarnings("rawtypes")
    public static class StreamlinedRepairs extends ConfigSection {
        @RequiresAction(action = Action.RESTART)
        public ValidatedEnum<NetheriteRepairMaterials> netheriteRepairMaterial = new ValidatedEnum<>(NetheriteRepairMaterials.DIAMOND);
        public ValidatedBoolean modifyAnvilRepairUnitCosts = new ValidatedBoolean();
        public Armor armor = new Armor();
        public Tools tools = new Tools();
        public UniqueItems uniqueItems = new UniqueItems();
		@RequiresAction(action = Action.RESTART)
		public ValidatedBoolean vanillaRepairables = new ValidatedBoolean();
        @SuppressWarnings("unchecked") @RequiresAction(action = Action.RESTART)
        public ValidatedMap<String, String> modRepairableItems = (new ValidatedMap.Builder())
                .keyHandler(new ValidatedString("", new AllowableStrings(ModUtil.itemSuggestions::contains, () -> ModUtil.itemSuggestions)))
                .valueHandler(new ValidatedString("diamond", new AllowableStrings(ModUtil.itemSuggestions::contains, () -> ModUtil.itemSuggestions)))
                .defaults(Map.of(
                        "rearm:netherite_shield", "minecraft:diamond",
                        "rearm:netherite_bow", "minecraft:diamond",
                        "rearm:netherite_crossbow", "minecraft:diamond",
                        "#chalk:chalks", "minecraft:calcite"
                ))
                .build();
        @SuppressWarnings("unchecked")
        public ValidatedMap<String, Integer> modItemUnitCosts = (new ValidatedMap.Builder())
                .keyHandler(new ValidatedString("", new AllowableStrings(ModUtil.itemSuggestions::contains, () -> ModUtil.itemSuggestions)))
                .valueHandler(new ValidatedInt(1, 9, 1))
                .defaults(Map.of(
                        "#farmersdelight:tools/knives", 1,
                        "rearm:netherite_bow", 3,
                        "rearm:netherite_crossbow", 3,
                        "rearm:netherite_shield", 3,
                        "#chalk:chalks", 2
                ))
                .build();
    }

	public static class ItemDestructionPrevention extends ConfigSection {
		public ValidatedEnum<DestructionPreventMode> mode = new ValidatedEnum<>(DestructionPreventMode.ALL);
		public ValidatedList<String> allowList = new ValidatedString(
				"", new AllowableStrings(ModUtil.itemSuggestions::contains, () -> ModUtil.itemSuggestions)
		).toList();
	}

	public static class MendingRework extends ConfigSection {
		public ValidatedBoolean enabled = new ValidatedBoolean();
		public ValidatedBoolean repairOnShiftUse = new ValidatedBoolean();
		public ValidatedBoolean autoRepairOnBreak = new ValidatedBoolean();
		public ValidatedBoolean enableRegularMendingBehavior = new ValidatedBoolean(false);
	}

    public static class AnvilImprovements extends ConfigSection {
        public ValidatedBoolean modifyDegradationChance = new ValidatedBoolean();
        public ValidatedFloat degradationChance = new ValidatedFloat(6.0F, 100.0F, 0);
        public ValidatedBoolean freeUnenchantedRepairs = new ValidatedBoolean();
        public ValidatedBoolean noWorkCostIncreaseOnRepair = new ValidatedBoolean();
        public ValidatedBoolean noPriorWorkCost = new ValidatedBoolean(false);
        public ValidatedBoolean freeRenames = new ValidatedBoolean();
        public ValidatedBoolean noTooExpensive = new ValidatedBoolean();
    }

    public static class GrindstoneImprovements extends ConfigSection {
        public ValidatedBoolean repairCostReductionRecipe = new ValidatedBoolean();
        public ValidatedBoolean increasedDisenchantXpGain = new ValidatedBoolean();
    }

    @SuppressWarnings("rawtypes")
    public static class EnchantedBookLootTweaks extends ConfigSection {
        public ValidatedBoolean weightedLevels = new ValidatedBoolean();
        @RequiresAction(action = Action.RESTART)
        public ValidatedBoolean additionalChestLoot = new ValidatedBoolean();
        @SuppressWarnings("unchecked") @RequiresAction(action = Action.RESTART)
        public ValidatedMap<Identifier, ChanceAndCount> bookLootLocations = (new ValidatedMap.Builder())
                .keyHandler(new ValidatedIdentifier(Identifier.withDefaultNamespace("chests/simple_dungeon")))
                .valueHandler(new ValidatedAny<>(new ChanceAndCount()))
                .defaults(Map.<Identifier, ChanceAndCount>ofEntries(
                        Map.entry(Identifier.parse("minecraft:chests/abandoned_mineshaft"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/ancient_city"), new ChanceAndCount(50, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/bastion_other"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/bastion_treasure"), new ChanceAndCount(100, 3)),
                        Map.entry(Identifier.parse("minecraft:chests/buried_treasure"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/desert_pyramid"), new ChanceAndCount(75, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/jungle_temple"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/pillager_outpost"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/nether_bridge"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/simple_dungeon"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/stronghold_corridor"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/stronghold_crossing"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/stronghold_library"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/underwater_ruin_big"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/underwater_ruin_small"), new ChanceAndCount(50, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/woodland_mansion"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("betteroceanmonuments:chests/upper_side_chamber"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("betterjungletemples:chests/treasure"), new ChanceAndCount(100, 2)),
                        Map.entry(Identifier.parse("betterdungeons:spider_dungeon/chests/egg_room"), new ChanceAndCount(50, 1)),
                        Map.entry(Identifier.parse("betterdungeons:skeleton_dungeon/chests/common"), new ChanceAndCount(50, 1)),
                        Map.entry(Identifier.parse("betterdungeons:skeleton_dungeon/chests/middle"), new ChanceAndCount(50, 1)),
                        Map.entry(Identifier.parse("betterdungeons:zombie_dungeon/chests/common"), new ChanceAndCount(50, 1)),
                        Map.entry(Identifier.parse("betterdungeons:zombie_dungeon/chests/special"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("betterdungeons:zombie_dungeon/chests/tombstone"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("betterdungeons:small_nether_dungeon/chests/common"), new ChanceAndCount(50, 1)),
                        Map.entry(Identifier.parse("betterfortresses:chests/keep"), new ChanceAndCount(20, 1)),
                        Map.entry(Identifier.parse("betterfortresses:chests/beacon"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/badlands"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/dark_forest"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/deep"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/desert"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/icy"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/jungle"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/mushroom"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/nether"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/ocean"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/snow"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/swamp"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/basalt"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/birch"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/crimson"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/dark_forest"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/desert"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/end"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/icy"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/jungle"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/nether"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/ocean"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/savanna"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/soul"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/stone"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/swamp"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/taiga"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/warped"), new ChanceAndCount(100, 1))
                ))
                .build();
    }

    @SuppressWarnings("rawtypes")
    public static class ImprovedExperienceBottle extends ConfigSection {
        public ValidatedBoolean modifyXpReward = new ValidatedBoolean();
        public ValidatedInt minXp = new ValidatedInt(30, Integer.MAX_VALUE, 1);
        public ValidatedInt maxXp = new ValidatedInt(50, Integer.MAX_VALUE, 1);
        @RequiresAction(action = Action.RESTART) @NonSync @ClientModifiable
        public ValidatedBoolean renameToExperienceBottle = new ValidatedBoolean();
        @RequiresAction(action = Action.RESTART)
        public ValidatedBoolean additionalChestLoot = new ValidatedBoolean();
        @SuppressWarnings("unchecked") @RequiresAction(action = Action.RESTART)
        public ValidatedMap<Identifier, ChanceAndCount> bottleLootLocations = (new ValidatedMap.Builder())
                .keyHandler(new ValidatedIdentifier(Identifier.withDefaultNamespace("chests/simple_dungeon")))
                .valueHandler(new ValidatedAny<>(new ChanceAndCount()))
                .defaults(Map.<Identifier, ChanceAndCount>ofEntries(
                        Map.entry(Identifier.parse("minecraft:chests/abandoned_mineshaft"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/ancient_city"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/end_city_treasure"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/jungle_temple"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/pillager_outpost"), new ChanceAndCount(100, 2)),
                        Map.entry(Identifier.parse("minecraft:chests/simple_dungeon"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/stronghold_corridor"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/stronghold_crossing"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/stronghold_library"), new ChanceAndCount(100, 2)),
                        Map.entry(Identifier.parse("minecraft:chests/woodland_mansion"), new ChanceAndCount(100, 3)),
                        Map.entry(Identifier.parse("minecraft:chests/desert_pyramid"), new ChanceAndCount(75, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/nether_bridge"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/buried_treasure"), new ChanceAndCount(100, 2)),
                        Map.entry(Identifier.parse("minecraft:chests/underwater_ruin_big"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/underwater_ruin_small"), new ChanceAndCount(50, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/bastion_other"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("minecraft:chests/bastion_treasure"), new ChanceAndCount(100, 3)),
                        Map.entry(Identifier.parse("betteroceanmonuments:chests/upper_side_chamber"), new ChanceAndCount(100, 3)),
                        Map.entry(Identifier.parse("betterjungletemples:chests/treasure"), new ChanceAndCount(100, 3)),
                        Map.entry(Identifier.parse("betterdungeons:spider_dungeon/chests/egg_room"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("betterdungeons:skeleton_dungeon/chests/common"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("betterdungeons:skeleton_dungeon/chests/middle"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("betterdungeons:zombie_dungeon/chests/common"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("betterdungeons:zombie_dungeon/chests/special"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("betterdungeons:zombie_dungeon/chests/tombstone"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("betterdungeons:small_nether_dungeon/chests/common"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("betterfortresses:chests/keep"), new ChanceAndCount(50, 1)),
                        Map.entry(Identifier.parse("betterfortresses:chests/beacon"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/badlands"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/dark_forest"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/deep"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/desert"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/icy"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/jungle"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/mushroom"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/nether"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/ocean"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/snow"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/dungeons/swamp"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/basalt"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/birch"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/crimson"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/dark_forest"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/desert"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/end"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/icy"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/jungle"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/nether"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/ocean"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/savanna"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/soul"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/stone"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/swamp"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/taiga"), new ChanceAndCount(100, 1)),
                        Map.entry(Identifier.parse("repurposed_structures:chests/mineshafts/warped"), new ChanceAndCount(100, 1))
                ))
                .build();
    }

    public static class EnchantmentLimits extends ConfigSection {
        public ValidatedBoolean limitEnchantingTablePower = new ValidatedBoolean();
        public ValidatedInt enchantingTablePowerLimit = new ValidatedInt(10, 15, 1);
        public ValidatedBoolean limitEnchantedLootPower = new ValidatedBoolean();
        public ValidatedInt enchantedLootPowerLimit = new ValidatedInt(20, 50, 1);
        public ValidatedBoolean limitBookTradeLevel = new ValidatedBoolean();
        public ValidatedInt bookTradeLevelLimit = new ValidatedInt(1, Integer.MAX_VALUE, 1);
        public ValidatedBoolean limitBookTradeUses = new ValidatedBoolean();
        public ValidatedInt bookTradeUsesLimit = new ValidatedInt(3, Integer.MAX_VALUE, 1);
    }

	public static class ModIntegration extends ConfigSection {
		public ValidatedBoolean penchant = new ValidatedBoolean();
	}

    public static class Armor extends ConfigSection {
        public ValidatedInt headArmorUnits = new ValidatedInt(5, 9, 1);
        public ValidatedInt chestArmorUnits = new ValidatedInt(8, 9, 1);
        public ValidatedInt legArmorUnits = new ValidatedInt(7, 9, 1);
        public ValidatedInt footArmorUnits = new ValidatedInt(4, 9, 1);
        public ValidatedInt horseArmorUnits = new ValidatedInt(6, 9, 1);
        public ValidatedInt wolfArmorUnits = new ValidatedInt(6, 9, 1);
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
