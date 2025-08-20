package me.pajic.simple_smithing_overhaul.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.compat.EDCompat;
import me.pajic.simple_smithing_overhaul.compat.TFLCompat;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
//? if < 1.21.7
import net.minecraft.world.item.AnimalArmorItem;
//? if >= 1.21.7 {
/*import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.entity.EntityType;
*///?}

import java.util.*;

public class ModUtil {
    public static final Map<Ingredient, Ingredient> additionalTagRepairables = new HashMap<>();
    public static final Map<Item, Ingredient> additionalRepairables = new HashMap<>();
    public static final List<String> itemSuggestions = new ArrayList<>();

    public static int determineUnitCost(ItemStack stack) {
        if (Main.CONFIG.streamlinedRepairs.modifyAnvilRepairUnitCosts.get() && !stack.is(Items.AIR)) {
            if (stack.is(ItemTags.HEAD_ARMOR)) return Main.CONFIG.streamlinedRepairs.armor.headArmorUnits.get();
            if (stack.is(ItemTags.CHEST_ARMOR)) return Main.CONFIG.streamlinedRepairs.armor.chestArmorUnits.get();
            if (stack.is(ItemTags.LEG_ARMOR)) return Main.CONFIG.streamlinedRepairs.armor.legArmorUnits.get();
            if (stack.is(ItemTags.FOOT_ARMOR)) return Main.CONFIG.streamlinedRepairs.armor.footArmorUnits.get();
            //? if < 1.21.7 {
            if (stack.getItem() instanceof AnimalArmorItem aai) {
                AnimalArmorItem.BodyType type =
                        //? if <= 1.21.1
                        aai.getBodyType();
                        //? if > 1.21.1
                        /*aai.bodyType;*/
                if (type.equals(AnimalArmorItem.BodyType.EQUESTRIAN)) return Main.CONFIG.streamlinedRepairs.armor.horseArmorUnits.get();
                if (type.equals(AnimalArmorItem.BodyType.CANINE)) return Main.CONFIG.streamlinedRepairs.armor.wolfArmorUnits.get();
            }
            //?}
            //? if >= 1.21.7 {
            /*if (stack.has(DataComponents.EQUIPPABLE)) {
                Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
                if (equippable.canBeEquippedBy(EntityType.WOLF)) return Main.CONFIG.streamlinedRepairs.armor.wolfArmorUnits.get();
                if (equippable.canBeEquippedBy(EntityType.HORSE)) return Main.CONFIG.streamlinedRepairs.armor.horseArmorUnits.get();
            }
            *///?}

            if (stack.is(ItemTags.PICKAXES)) return Main.CONFIG.streamlinedRepairs.tools.pickaxeUnits.get();
            if (stack.is(ItemTags.AXES)) return Main.CONFIG.streamlinedRepairs.tools.axeUnits.get();
            if (stack.is(ItemTags.SWORDS)) return Main.CONFIG.streamlinedRepairs.tools.swordUnits.get();
            if (stack.is(ItemTags.HOES)) return Main.CONFIG.streamlinedRepairs.tools.hoeUnits.get();
            if (stack.is(ItemTags.SHOVELS)) return Main.CONFIG.streamlinedRepairs.tools.shovelUnits.get();

            if (stack.is(Items.SHIELD)) return Main.CONFIG.streamlinedRepairs.uniqueItems.shieldUnits.get();
            if (stack.is(Items.ELYTRA)) return Main.CONFIG.streamlinedRepairs.uniqueItems.elytraUnits.get();
            if (stack.is(Items.MACE)) return Main.CONFIG.streamlinedRepairs.uniqueItems.maceUnits.get();
            if (stack.is(ModItems.WHETSTONE)) return Main.CONFIG.streamlinedRepairs.uniqueItems.whetstoneUnits.get();
            if (stack.is(Items.BOW)) return Main.CONFIG.streamlinedRepairs.uniqueItems.bowUnits.get();
            if (stack.is(Items.CROSSBOW)) return Main.CONFIG.streamlinedRepairs.uniqueItems.crossbowUnits.get();
            if (stack.is(Items.FLINT_AND_STEEL)) return Main.CONFIG.streamlinedRepairs.uniqueItems.flintAndSteelUnits.get();
            if (stack.is(Items.SHEARS)) return Main.CONFIG.streamlinedRepairs.uniqueItems.shearsUnits.get();
            if (stack.is(Items.TRIDENT)) return Main.CONFIG.streamlinedRepairs.uniqueItems.tridentUnits.get();
            if (stack.is(Items.BRUSH)) return Main.CONFIG.streamlinedRepairs.uniqueItems.brushUnits.get();
            if (stack.is(Items.FISHING_ROD)) return Main.CONFIG.streamlinedRepairs.uniqueItems.fishingRodUnits.get();
            if (stack.is(Items.CARROT_ON_A_STICK)) return Main.CONFIG.streamlinedRepairs.uniqueItems.carrotOnAStickUnits.get();
            if (stack.is(Items.WARPED_FUNGUS_ON_A_STICK)) return Main.CONFIG.streamlinedRepairs.uniqueItems.warpedFungusOnAStickUnits.get();

            for (Map.Entry<String, Integer> entry : Main.CONFIG.streamlinedRepairs.modItemUnitCosts.entrySet()) {
                if (entry.getKey().startsWith("#")) {
                    if (stack.is(TagKey.create(Registries.ITEM, ResourceLocation.parse(entry.getKey().replace("#", ""))))) {
                        return entry.getValue();
                    }
                } else {
                    Optional<Item> item = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(entry.getKey()));
                    if (item.isPresent() && stack.is(item.get())) {
                        return entry.getValue();
                    }
                }
            }
        }
        return 4;
    }

    public static int calculateGrindstoneReward(Object2IntMap.Entry<Holder<Enchantment>> entry) {
        Enchantment e = entry.getKey().value();
        int level = entry.getIntValue();
        int min = e.getMinCost(level);
        int max = e.getMaxCost(level);
        return Math.round(min + (max - min) * ((float) level / e.getMaxLevel()));
    }

    public static boolean isEnchantedBookOrWhetstoneUpgradeRecipe(NonNullList<Slot> slots) {
        return slots.get(0).getItem().is(ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE) &&
                (slots.get(1).getItem().is(Items.ENCHANTED_BOOK) || slots.get(1).getItem().is(ModItems.WHETSTONE)) &&
                slots.get(1).getItem().has(DataComponents.STORED_ENCHANTMENTS);
    }

    public static boolean isEnchantedItemUpgradeRecipe(NonNullList<Slot> slots) {
        return slots.get(0).getItem().is(ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE) &&
                slots.get(1).getItem().has(DataComponents.MAX_DAMAGE) &&
                slots.get(1).getItem().getMaxStackSize() == 1 &&
                slots.get(1).getItem().has(DataComponents.ENCHANTMENTS);
    }

    public static boolean isPinnacleEnchantmentRecipe(NonNullList<Slot> slots) {
        return slots.get(0).getItem().is(ModItems.PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE) &&
                slots.get(1).getItem().has(DataComponents.MAX_DAMAGE) &&
                slots.get(1).getItem().getMaxStackSize() == 1 &&
                slots.get(1).getItem().has(DataComponents.ENCHANTMENTS) &&
                slots.get(2).getItem().is(Items.ECHO_SHARD);
    }

    public static boolean enchantmentEligible(Holder<Enchantment> enchantment) {
        return Main.CONFIG.pinnacleEnchantment.excludedFromMaxedOutCheck.stream()
                .noneMatch(enchantment::is) && (!CompatFlags.ED_LOADED || EDCompat.enchantmentEnabled(enchantment));
    }

    public static Item getNetheriteRepairMaterial() {
        return switch (Main.CONFIG.streamlinedRepairs.netheriteRepairMaterial.get()) {
            case DIAMOND -> Items.DIAMOND;
            case NETHERITE_SCRAP -> Items.NETHERITE_SCRAP;
            default -> Items.NETHERITE_INGOT;
        };
    }

    public static boolean hasAdditionalRepair(ItemStack stack, ItemStack repairCandidate) {
        for (Map.Entry<Ingredient, Ingredient> tagRepair : additionalTagRepairables.entrySet()) {
            if (tagRepair.getKey().test(stack)) return tagRepair.getValue().test(repairCandidate);
        }
        for (Map.Entry<Item, Ingredient> repair : additionalRepairables.entrySet()) {
            if (stack.is(repair.getKey())) return repair.getValue().test(repairCandidate);
        }
        return false;
    }

    public static boolean isBroken(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.BROKEN, false);
    }

    public static void payXpCost(Player player, int cost) {
        if (CompatFlags.TAX_FREE_LEVELS_LOADED) TFLCompat.payXpCost(player, cost);
        else player.giveExperienceLevels(-cost);
    }

    public static int calculateNewEnchantmentLevel(int maxLevel, RandomSource randomSource, int original) {
        if (Main.CONFIG.enchantedBookLootTweaks.weightedLevels.get()) {
            // fills up a pool with enchantment levels and picks a level randomly
            // for level 5 the pool would look like this
            // 1 x lv5, 9 x lv4, 25 x lv3, 49 x lv2, 81 x lv1
            if (maxLevel == 1) return 1;
            IntList pool = new IntArrayList();
            for (int i = maxLevel, j = 1; i > 0; i--, j += 2) {
                for (int k = 0; k < j * j; k++) {
                    pool.add(i);
                }
            }
            return pool.getInt(randomSource.nextInt(pool.size()));
        }
        return original;
    }

    public static List<String> nameColors = List.of(
            "Black",
            "Dark Blue",
            "Dark Green",
            "Dark Aqua",
            "Dark Red",
            "Dark Purple",
            "Gold",
            "Gray",
            "Dark Gray",
            "Blue",
            "Green",
            "Aqua",
            "Red",
            "Light Purple",
            "Yellow",
            "White"
    );
}
