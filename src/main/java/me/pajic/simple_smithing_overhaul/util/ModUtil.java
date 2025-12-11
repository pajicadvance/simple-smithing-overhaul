package me.pajic.simple_smithing_overhaul.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.compat.EDCompat;
import me.pajic.simple_smithing_overhaul.compat.TFLCompat;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
//? if <= 1.21.1 {
/*import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.client.renderer.item.ItemProperties;
*///?} else {
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.HolderSet;
//?}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModUtil {
    public static final Map<Ingredient, Ingredient> additionalRepairables = new HashMap<>();
    public static final List<String> itemSuggestions = new ArrayList<>();

	//? if > 1.21.1
	@SuppressWarnings("deprecation")
	public static void updateAdditionalRepairables(HolderLookup.Provider provider) {
		// Custom repairs 101
		// 2 maps, item to ingredient and ingredient to ingredient, populated in the initializer
		// In 1.21.1, ModUtil.hasAdditionalRepairables contains logic to determine if an item has a custom repair based on the two maps
		// This method is injected via mixin to vanilla methods which determine if an item has a repair recipe
		// In 1.21.1+, the components of each item are patched to add a repairable component containing the repair materials,
		// so ModUtil.hasAdditionalRepairables isn't required and is not injected anywhere
		ModUtil.additionalRepairables.clear();
		HolderLookup<Item> registry = provider.lookupOrThrow(Registries.ITEM);

		// Vanilla repairs
		ModUtil.additionalRepairables.put(Ingredient.of(Items.BOW), Ingredient.of(Items.STRING));
		ModUtil.additionalRepairables.put(Ingredient.of(Items.CROSSBOW), Ingredient.of(Items.STRING));
		ModUtil.additionalRepairables.put(Ingredient.of(Items.FISHING_ROD), Ingredient.of(Items.STRING));
		ModUtil.additionalRepairables.put(Ingredient.of(Items.FLINT_AND_STEEL), Ingredient.of(Items.IRON_INGOT));
		ModUtil.additionalRepairables.put(Ingredient.of(Items.SHEARS), Ingredient.of(Items.IRON_INGOT));
		ModUtil.additionalRepairables.put(Ingredient.of(Items.BRUSH), Ingredient.of(Items.FEATHER));
		ModUtil.additionalRepairables.put(Ingredient.of(Items.CARROT_ON_A_STICK), Ingredient.of(Items.CARROT));
		ModUtil.additionalRepairables.put(Ingredient.of(Items.WARPED_FUNGUS_ON_A_STICK), Ingredient.of(Items.WARPED_FUNGUS));
		if (!CompatFlags.BETTER_TRIDENTS_LOADED) ModUtil.additionalRepairables.put(Ingredient.of(Items.TRIDENT), Ingredient.of(Items.PRISMARINE_SHARD));
		// Modded repairs
		SSO.CONFIG.streamlinedRepairs.modRepairableItems.forEach((repairItem, repairMaterial) -> {
			try {
				if (repairItem.startsWith("#")) {
					if (repairMaterial.startsWith("#")) {
						ModUtil.additionalRepairables.put(
								ingredientFromItemTag(repairItem, registry),
								ingredientFromItemTag(repairMaterial, registry)
						);
					} else {
						registry.get(ResourceKey.create(Registries.ITEM, Identifier.tryParse(repairMaterial))).ifPresent(value ->
								ModUtil.additionalRepairables.put(
										ingredientFromItemTag(repairItem, registry),
										Ingredient.of(value.value())
								)
						);
					}
				} else {
					Optional<Holder.Reference<Item>> item = registry.get(ResourceKey.create(Registries.ITEM, Identifier.parse(repairItem)));
					if (item.isPresent()) {
						if (repairMaterial.startsWith("#")) {
							ModUtil.additionalRepairables.put(
									Ingredient.of(item.get().value()),
									ingredientFromItemTag(repairMaterial, registry)
							);
						} else {
							registry.get(ResourceKey.create(Registries.ITEM, Identifier.parse(repairMaterial))).ifPresent(value ->
									ModUtil.additionalRepairables.put(Ingredient.of(item.get().value()), Ingredient.of(value.value()))
							);
						}
					}
				}
				// Catch anything that explodes above because I cannot be bothered
			} catch (Throwable t) {
				SSO.LOGGER.warn("Unable to load additional repair {} with {}, skipping: {}", repairItem, repairMaterial, t.getMessage());
			}
		});
		// Patch item components to add the repairable component
		//? if > 1.21.1 {
        ModUtil.additionalRepairables.forEach((itemIngredient, materialIngredient) ->
                itemIngredient.items().forEach(itemHolder ->
                        itemHolder.value().components = PatchedDataComponentMap.fromPatch(
                                itemHolder.value().components,
                                DataComponentPatch.builder().set(
                                        DataComponents.REPAIRABLE,
                                        new Repairable(HolderSet.direct(materialIngredient.items().toList()))
                                ).build()
                        )
                )
        );
        //?}
	}

    //? if > 1.21.1
    @SuppressWarnings("DataFlowIssue")
    public static int determineUnitCost(ItemStack stack) {
        if (SSO.CONFIG.streamlinedRepairs.modifyAnvilRepairUnitCosts.get() && !stack.is(Items.AIR)) {
            if (stack.is(ItemTags.HEAD_ARMOR)) return SSO.CONFIG.streamlinedRepairs.armor.headArmorUnits.get();
            if (stack.is(ItemTags.CHEST_ARMOR)) return SSO.CONFIG.streamlinedRepairs.armor.chestArmorUnits.get();
            if (stack.is(ItemTags.LEG_ARMOR)) return SSO.CONFIG.streamlinedRepairs.armor.legArmorUnits.get();
            if (stack.is(ItemTags.FOOT_ARMOR)) return SSO.CONFIG.streamlinedRepairs.armor.footArmorUnits.get();
            //? if <= 1.21.1 {
            /*if (stack.getItem() instanceof AnimalArmorItem aai) {
                if (aai.getBodyType().equals(AnimalArmorItem.BodyType.EQUESTRIAN)) return SSO.CONFIG.streamlinedRepairs.armor.horseArmorUnits.get();
                if (aai.getBodyType().equals(AnimalArmorItem.BodyType.CANINE)) return SSO.CONFIG.streamlinedRepairs.armor.wolfArmorUnits.get();
            }
            *///?} else {
            if (stack.has(DataComponents.EQUIPPABLE)) {
                Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
                if (equippable.canBeEquippedBy(EntityType.WOLF)) return SSO.CONFIG.streamlinedRepairs.armor.wolfArmorUnits.get();
                if (equippable.canBeEquippedBy(EntityType.HORSE)) return SSO.CONFIG.streamlinedRepairs.armor.horseArmorUnits.get();
            }
            //?}

            if (stack.is(ItemTags.PICKAXES)) return SSO.CONFIG.streamlinedRepairs.tools.pickaxeUnits.get();
            if (stack.is(ItemTags.AXES)) return SSO.CONFIG.streamlinedRepairs.tools.axeUnits.get();
            if (stack.is(ItemTags.SWORDS)) return SSO.CONFIG.streamlinedRepairs.tools.swordUnits.get();
            if (stack.is(ItemTags.HOES)) return SSO.CONFIG.streamlinedRepairs.tools.hoeUnits.get();
            if (stack.is(ItemTags.SHOVELS)) return SSO.CONFIG.streamlinedRepairs.tools.shovelUnits.get();

            if (stack.is(Items.SHIELD)) return SSO.CONFIG.streamlinedRepairs.uniqueItems.shieldUnits.get();
            if (stack.is(Items.ELYTRA)) return SSO.CONFIG.streamlinedRepairs.uniqueItems.elytraUnits.get();
            if (stack.is(Items.MACE)) return SSO.CONFIG.streamlinedRepairs.uniqueItems.maceUnits.get();
            if (stack.is(ModItems.WHETSTONE)) return SSO.CONFIG.streamlinedRepairs.uniqueItems.whetstoneUnits.get();
            if (stack.is(Items.BOW)) return SSO.CONFIG.streamlinedRepairs.uniqueItems.bowUnits.get();
            if (stack.is(Items.CROSSBOW)) return SSO.CONFIG.streamlinedRepairs.uniqueItems.crossbowUnits.get();
            if (stack.is(Items.FLINT_AND_STEEL)) return SSO.CONFIG.streamlinedRepairs.uniqueItems.flintAndSteelUnits.get();
            if (stack.is(Items.SHEARS)) return SSO.CONFIG.streamlinedRepairs.uniqueItems.shearsUnits.get();
            if (stack.is(Items.TRIDENT)) return SSO.CONFIG.streamlinedRepairs.uniqueItems.tridentUnits.get();
            if (stack.is(Items.BRUSH)) return SSO.CONFIG.streamlinedRepairs.uniqueItems.brushUnits.get();
            if (stack.is(Items.FISHING_ROD)) return SSO.CONFIG.streamlinedRepairs.uniqueItems.fishingRodUnits.get();
            if (stack.is(Items.CARROT_ON_A_STICK)) return SSO.CONFIG.streamlinedRepairs.uniqueItems.carrotOnAStickUnits.get();
            if (stack.is(Items.WARPED_FUNGUS_ON_A_STICK)) return SSO.CONFIG.streamlinedRepairs.uniqueItems.warpedFungusOnAStickUnits.get();

            for (Map.Entry<String, Integer> entry : SSO.CONFIG.streamlinedRepairs.modItemUnitCosts.entrySet()) {
                if (entry.getKey().startsWith("#")) {
                    if (stack.is(TagKey.create(Registries.ITEM, Identifier.parse(entry.getKey().replace("#", ""))))) {
                        return entry.getValue();
                    }
                } else {
                    Optional<Item> item = BuiltInRegistries.ITEM.getOptional(Identifier.parse(entry.getKey()));
                    if (item.isPresent() && stack.is(item.get())) {
                        return entry.getValue();
                    }
                }
            }
        }
        return 4;
    }

	public static void initItemProperties() {
		//? if <= 1.21.1 {
		/*ItemProperties.register(
				ModItems.WHETSTONE,
				SSO.id("damage_state"),
				(stack, level, entity, i) -> (float) stack.getDamageValue() / stack.getMaxDamage()
		);
		*///?}
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
        return SSO.CONFIG.pinnacleEnchantment.excludedFromMaxedOutCheck.stream()
                .noneMatch(enchantment::is) && (!CompatFlags.ED_LOADED || EDCompat.enchantmentEnabled(enchantment));
    }

    public static Item getNetheriteRepairMaterial() {
        return switch (SSO.CONFIG.streamlinedRepairs.netheriteRepairMaterial.get()) {
            case DIAMOND -> Items.DIAMOND;
            case NETHERITE_SCRAP -> Items.NETHERITE_SCRAP;
            default -> Items.NETHERITE_INGOT;
        };
    }

    public static boolean hasAdditionalRepair(ItemStack stack, ItemStack repairCandidate) {
        for (Map.Entry<Ingredient, Ingredient> tagRepair : additionalRepairables.entrySet()) {
            if (tagRepair.getKey().test(stack)) return tagRepair.getValue().test(repairCandidate);
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
        if (SSO.CONFIG.enchantedBookLootTweaks.weightedLevels.get()) {
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

	public static InteractionResult canUse(Player player, InteractionHand hand) {
		return ModUtil.isBroken(player.getItemInHand(hand)) ? InteractionResult.FAIL : InteractionResult.PASS;
	}

	private static Ingredient ingredientFromItemTag(String s, HolderLookup<Item> registry) {
		return Ingredient.of(/*? if > 1.21.1 {*/registry.get(/*?}*/TagKey.create(Registries.ITEM, Identifier.tryParse(s.substring(1)))/*? if > 1.21.1 {*/).orElseThrow()/*?}*/);
	}

    public static List<String> colorNames = List.of(
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
