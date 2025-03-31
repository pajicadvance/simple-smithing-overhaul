package me.pajic.simple_smithing_overhaul.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = "simple_smithing_overhaul", bus = EventBusSubscriber.Bus.MOD)
public class ModCommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ENABLE_ENCHANTMENT_UPGRADING = BUILDER
            .translation("text.config.simple_smithing_overhaul.option.enchantmentUpgrading.enableEnchantmentUpgrading")
            .gameRestart()
            .define("enableEnchantmentUpgrading", true);
    private static final ModConfigSpec.BooleanValue ENABLE_PINNACLE_ENCHANTMENT = BUILDER
            .translation("text.config.simple_smithing_overhaul.option.pinnacleEnchantment.enablePinnacleEnchantment")
            .gameRestart()
            .define("enablePinnacleEnchantment", true);
    private static final ModConfigSpec.BooleanValue ENABLE_WHETSTONE = BUILDER
            .translation("text.config.simple_smithing_overhaul.option.whetstone.enableWhetstone")
            .gameRestart()
            .define("enableWhetstone", true);
    private static final ModConfigSpec.ConfigValue<NetheriteRepairMaterials> NETHERITE_REPAIR_MATERIAL = BUILDER
            .translation("text.config.simple_smithing_overhaul.option.streamlinedRepairs.netheriteRepairMaterial")
            .gameRestart()
            .defineEnum("netheriteRepairMaterial", NetheriteRepairMaterials.NETHERITE_SCRAP);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> MOD_REPAIRABLE_ITEMS = BUILDER
            .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.modRepairableItems")
            .gameRestart()
            .defineListAllowEmpty("modRepairableItems",List.of(
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
    ), () -> "", o -> true);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> MOD_ITEM_UNIT_COSTS = BUILDER
            .translation("text.config.simple_smithing_overhaul.option.anvilImprovements.modItemUnitCosts")
            .gameRestart()
            .defineListAllowEmpty("modItemUnitCosts", List.of(
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
    ), () -> "", o -> true);
    private static final ModConfigSpec.BooleanValue ADDITIONAL_BOOK_CHEST_LOOT = BUILDER
            .translation("text.config.simple_smithing_overhaul.option.enchantedBookLootTweaks.additionalChestLoot")
            .gameRestart()
            .define("additionalBookChestLoot", true);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> BOOK_LOOT_LOCATIONS = BUILDER
            .translation("text.config.simple_smithing_overhaul.option.enchantedBookLootTweaks.bookLootLocations")
            .gameRestart()
            .defineListAllowEmpty("bookLootLocations",List.of(
                    "minecraft:chests/abandoned_mineshaft;50",
                    "minecraft:chests/ancient_city;50",
                    "minecraft:chests/bastion_other;50",
                    "minecraft:chests/bastion_treasure;100;3",
                    "minecraft:chests/buried_treasure;100",
                    "minecraft:chests/desert_pyramid;50",
                    "minecraft:chests/jungle_temple;50",
                    "minecraft:chests/pillager_outpost;50",
                    "minecraft:chests/nether_bridge;50",
                    "minecraft:chests/simple_dungeon;50",
                    "minecraft:chests/stronghold_corridor;50",
                    "minecraft:chests/stronghold_crossing;50",
                    "minecraft:chests/stronghold_library;50",
                    "minecraft:chests/underwater_ruin_big;50",
                    "minecraft:chests/underwater_ruin_small;25",
                    "minecraft:chests/woodland_mansion;50",
                    "betteroceanmonuments:chests/upper_side_chamber;100",
                    "betterjungletemples:chests/treasure;100;2",
                    "betterdungeons:spider_dungeon/chests/egg_room;50",
                    "betterdungeons:skeleton_dungeon/chests/common;50",
                    "betterdungeons:skeleton_dungeon/chests/middle;50",
                    "betterdungeons:zombie_dungeon/chests/common;50",
                    "betterdungeons:zombie_dungeon/chests/special;100",
                    "betterdungeons:zombie_dungeon/chests/tombstone;100",
                    "betterdungeons:small_nether_dungeon/chests/common;50",
                    "betterfortresses:chests/keep;20",
                    "betterfortresses:chests/beacon;100",
                    "repurposed_structures:chests/dungeons/badlands;50",
                    "repurposed_structures:chests/dungeons/dark_forest;50",
                    "repurposed_structures:chests/dungeons/deep;50",
                    "repurposed_structures:chests/dungeons/desert;50",
                    "repurposed_structures:chests/dungeons/icy;50",
                    "repurposed_structures:chests/dungeons/jungle;50",
                    "repurposed_structures:chests/dungeons/mushroom;50",
                    "repurposed_structures:chests/dungeons/nether;50",
                    "repurposed_structures:chests/dungeons/ocean;50",
                    "repurposed_structures:chests/dungeons/snow;50",
                    "repurposed_structures:chests/dungeons/swamp;50",
                    "repurposed_structures:chests/mineshafts/basalt;50",
                    "repurposed_structures:chests/mineshafts/birch;50",
                    "repurposed_structures:chests/mineshafts/crimson;50",
                    "repurposed_structures:chests/mineshafts/dark_forest;50",
                    "repurposed_structures:chests/mineshafts/desert;50",
                    "repurposed_structures:chests/mineshafts/end;50",
                    "repurposed_structures:chests/mineshafts/icy;50",
                    "repurposed_structures:chests/mineshafts/jungle;50",
                    "repurposed_structures:chests/mineshafts/nether;50",
                    "repurposed_structures:chests/mineshafts/ocean;50",
                    "repurposed_structures:chests/mineshafts/savanna;50",
                    "repurposed_structures:chests/mineshafts/soul;50",
                    "repurposed_structures:chests/mineshafts/stone;50",
                    "repurposed_structures:chests/mineshafts/swamp;50",
                    "repurposed_structures:chests/mineshafts/taiga;50",
                    "repurposed_structures:chests/mineshafts/warped;50"
    ), () -> "", o -> true);
    private static final ModConfigSpec.BooleanValue ADDITIONAL_BOTTLE_CHEST_LOOT = BUILDER
            .translation("text.config.simple_smithing_overhaul.option.improvedExperienceBottle.additionalChestLoot")
            .gameRestart()
            .define("additionalBottleChestLoot", true);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> BOTTLE_LOOT_LOCATIONS = BUILDER
            .translation("text.config.simple_smithing_overhaul.option.improvedExperienceBottle.bottleLootLocations")
            .gameRestart()
            .defineListAllowEmpty("bottleLootLocations",List.of(
                    "minecraft:chests/abandoned_mineshaft;100",
                    "minecraft:chests/ancient_city;100",
                    "minecraft:chests/end_city_treasure;100",
                    "minecraft:chests/jungle_temple;100",
                    "minecraft:chests/pillager_outpost;100;2",
                    "minecraft:chests/simple_dungeon;100",
                    "minecraft:chests/stronghold_corridor;100",
                    "minecraft:chests/stronghold_crossing;100",
                    "minecraft:chests/stronghold_library;100;2",
                    "minecraft:chests/woodland_mansion;100;3",
                    "minecraft:chests/desert_pyramid;75",
                    "minecraft:chests/nether_bridge;100",
                    "minecraft:chests/buried_treasure;100;2",
                    "minecraft:chests/underwater_ruin_big;100",
                    "minecraft:chests/underwater_ruin_small;50",
                    "minecraft:chests/bastion_other;100",
                    "minecraft:chests/bastion_treasure;100;3",
                    "betteroceanmonuments:chests/upper_side_chamber;100;3",
                    "betterjungletemples:chests/treasure;100;3",
                    "betterdungeons:spider_dungeon/chests/egg_room;100",
                    "betterdungeons:skeleton_dungeon/chests/common;100",
                    "betterdungeons:skeleton_dungeon/chests/middle;100",
                    "betterdungeons:zombie_dungeon/chests/common;100",
                    "betterdungeons:zombie_dungeon/chests/special;100",
                    "betterdungeons:zombie_dungeon/chests/tombstone;100",
                    "betterdungeons:small_nether_dungeon/chests/common;100",
                    "betterfortresses:chests/keep;50",
                    "betterfortresses:chests/beacon;100",
                    "repurposed_structures:chests/dungeons/badlands;100",
                    "repurposed_structures:chests/dungeons/dark_forest;100",
                    "repurposed_structures:chests/dungeons/deep;100",
                    "repurposed_structures:chests/dungeons/desert;100",
                    "repurposed_structures:chests/dungeons/icy;100",
                    "repurposed_structures:chests/dungeons/jungle;100",
                    "repurposed_structures:chests/dungeons/mushroom;100",
                    "repurposed_structures:chests/dungeons/nether;100",
                    "repurposed_structures:chests/dungeons/ocean;100",
                    "repurposed_structures:chests/dungeons/snow;100",
                    "repurposed_structures:chests/dungeons/swamp;100",
                    "repurposed_structures:chests/mineshafts/basalt;100",
                    "repurposed_structures:chests/mineshafts/birch;100",
                    "repurposed_structures:chests/mineshafts/crimson;100",
                    "repurposed_structures:chests/mineshafts/dark_forest;100",
                    "repurposed_structures:chests/mineshafts/desert;100",
                    "repurposed_structures:chests/mineshafts/end;100",
                    "repurposed_structures:chests/mineshafts/icy;100",
                    "repurposed_structures:chests/mineshafts/jungle;100",
                    "repurposed_structures:chests/mineshafts/nether;100",
                    "repurposed_structures:chests/mineshafts/ocean;100",
                    "repurposed_structures:chests/mineshafts/savanna;100",
                    "repurposed_structures:chests/mineshafts/soul;100",
                    "repurposed_structures:chests/mineshafts/stone;100",
                    "repurposed_structures:chests/mineshafts/swamp;100",
                    "repurposed_structures:chests/mineshafts/taiga;100",
                    "repurposed_structures:chests/mineshafts/warped;100"
            ), () -> "", o -> true);

    public static final ModConfigSpec COMMON_SPEC = BUILDER.build();

    public static boolean enableEnchantmentUpgrading;
    public static boolean enablePinnacleEnchantment;
    public static boolean enableWhetstone;
    public static NetheriteRepairMaterials netheriteRepairMaterial;
    public static List<String> modRepairableItems;
    public static List<String> modItemUnitCosts;
    public static boolean additionalBookChestLoot;
    public static List<String> bookLootLocations;
    public static boolean additionalBottleChestLoot;
    public static List<String> bottleLootLocations;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        updateConfig(event);
    }

    @SubscribeEvent
    static void onChange(final ModConfigEvent.Reloading event) {
        updateConfig(event);
    }

    private static void updateConfig(ModConfigEvent event) {
        if (event.getConfig().getSpec() == COMMON_SPEC) {
            enableEnchantmentUpgrading = ENABLE_ENCHANTMENT_UPGRADING.get();
            enablePinnacleEnchantment = ENABLE_PINNACLE_ENCHANTMENT.get();
            enableWhetstone = ENABLE_WHETSTONE.get();
            netheriteRepairMaterial = NETHERITE_REPAIR_MATERIAL.get();
            modRepairableItems = new ArrayList<>(MOD_REPAIRABLE_ITEMS.get());
            modItemUnitCosts = new ArrayList<>(MOD_ITEM_UNIT_COSTS.get());
            additionalBookChestLoot = ADDITIONAL_BOOK_CHEST_LOOT.get();
            bookLootLocations = new ArrayList<>(BOOK_LOOT_LOCATIONS.get());
            additionalBottleChestLoot = ADDITIONAL_BOTTLE_CHEST_LOOT.get();
            bottleLootLocations = new ArrayList<>(BOTTLE_LOOT_LOCATIONS.get());
        }
    }

    public enum NetheriteRepairMaterials {
        NETHERITE_INGOT, NETHERITE_SCRAP, DIAMOND
    }
}
