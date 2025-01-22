package me.pajic.simple_smithing_overhaul.util;

import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import me.pajic.simple_smithing_overhaul.compat.EDCompat;
import me.pajic.simple_smithing_overhaul.config.ModCommonConfig;
import me.pajic.simple_smithing_overhaul.config.ModServerConfig;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.fml.ModList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//? if > 1.21.1
/*import net.minecraft.world.item.enchantment.Repairable;*/

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ModUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger("SimpleSmithingOverhaul-Util");

    public static final boolean ED_LOADED = ModList.get().isLoaded("enchantmentdisabler");
    public static final List<ObjectObjectImmutablePair<Item, Ingredient>> additionalRepairables = new ArrayList<>();
    public static int cost = 0;

    public static int determineUnitCost(ItemStack stack) {
        if (ModServerConfig.modifyAnvilRepairUnitCosts) {

            if (stack.is(ItemTags.HEAD_ARMOR)) return ModServerConfig.headArmorUnits;
            if (stack.is(ItemTags.CHEST_ARMOR)) return ModServerConfig.chestArmorUnits;
            if (stack.is(ItemTags.LEG_ARMOR)) return ModServerConfig.legArmorUnits;
            if (stack.is(ItemTags.FOOT_ARMOR)) return ModServerConfig.footArmorUnits;
            if (stack.getItem() instanceof AnimalArmorItem aai) {
                AnimalArmorItem.BodyType type =
                        //? if <= 1.21.1
                        aai.getBodyType();
                        //? if > 1.21.1
                        /*aai.bodyType;*/
                if (type.equals(AnimalArmorItem.BodyType.EQUESTRIAN)) return ModServerConfig.horseArmorUnits;
                if (type.equals(AnimalArmorItem.BodyType.CANINE)) return ModServerConfig.wolfArmorUnits;
            }

            if (stack.is(ItemTags.PICKAXES)) return ModServerConfig.pickaxeUnits;
            if (stack.is(ItemTags.AXES)) return ModServerConfig.axeUnits;
            if (stack.is(ItemTags.SWORDS)) return ModServerConfig.swordUnits;
            if (stack.is(ItemTags.HOES)) return ModServerConfig.hoeUnits;
            if (stack.is(ItemTags.SHOVELS)) return ModServerConfig.shovelUnits;

            if (stack.is(Items.SHIELD)) return ModServerConfig.shieldUnits;
            if (stack.is(Items.ELYTRA)) return ModServerConfig.elytraUnits;
            if (stack.is(Items.MACE)) return ModServerConfig.maceUnits;
            if (stack.is(ModItems.WHETSTONE)) return ModServerConfig.whetstoneUnits;
            if (stack.is(Items.BOW)) return ModServerConfig.bowUnits;
            if (stack.is(Items.CROSSBOW)) return ModServerConfig.crossbowUnits;
            if (stack.is(Items.FLINT_AND_STEEL)) return ModServerConfig.flintAndSteelUnits;
            if (stack.is(Items.SHEARS)) return ModServerConfig.shearsUnits;
            if (stack.is(Items.TRIDENT)) return ModServerConfig.tridentUnits;
            if (stack.is(Items.BRUSH)) return ModServerConfig.brushUnits;
            if (stack.is(Items.FISHING_ROD)) return ModServerConfig.fishingRodUnits;
            if (stack.is(Items.CARROT_ON_A_STICK)) return ModServerConfig.carrotOnAStickUnits;
            if (stack.is(Items.WARPED_FUNGUS_ON_A_STICK)) return ModServerConfig.warpedFungusOnAStickUnits;

            for (String s : ModCommonConfig.modItemUnitCosts) {
                String[] split = s.split(";");
                if (split.length != 2) {
                    LOGGER.warn("Invalid repair unit cost entry: {}, skipping", s);
                }
                else {
                    try {
                        if (split[0].startsWith("#")) {
                            if (stack.is(TagKey.create(Registries.ITEM, ResourceLocation.parse(split[0].replace("#", ""))))) {
                                return Integer.parseInt(split[1]);
                            }
                        }
                        else if (stack.is(BuiltInRegistries.ITEM.get(ResourceLocation.parse(split[0]))/*? if > 1.21.1 {*//*.orElseThrow()*//*?}*/)) {
                            return Integer.parseInt(split[1]);
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.warn("Unit cost is not a number in unit cost entry: {}, skipping", s);
                    } catch (NoSuchElementException e) {
                        LOGGER.warn("Resource location in unit cost entry does not exist: {}, skipping", s);
                    }
                }
            }
        }

        return 4;
    }

    public static void initAdditionalRepairables() {
        additionalRepairables.add(new ObjectObjectImmutablePair<>(Items.BOW, Ingredient.of(Items.STRING)));
        additionalRepairables.add(new ObjectObjectImmutablePair<>(Items.CROSSBOW, Ingredient.of(Items.STRING)));
        additionalRepairables.add(new ObjectObjectImmutablePair<>(Items.FISHING_ROD, Ingredient.of(Items.STRING)));
        additionalRepairables.add(new ObjectObjectImmutablePair<>(Items.FLINT_AND_STEEL, Ingredient.of(Items.IRON_INGOT)));
        additionalRepairables.add(new ObjectObjectImmutablePair<>(Items.SHEARS, Ingredient.of(Items.IRON_INGOT)));
        additionalRepairables.add(new ObjectObjectImmutablePair<>(Items.BRUSH, Ingredient.of(Items.FEATHER)));
        additionalRepairables.add(new ObjectObjectImmutablePair<>(Items.CARROT_ON_A_STICK, Ingredient.of(Items.CARROT)));
        additionalRepairables.add(new ObjectObjectImmutablePair<>(Items.WARPED_FUNGUS_ON_A_STICK, Ingredient.of(Items.WARPED_FUNGUS)));
        if (ModList.get().isLoaded("bettertridents"))
            additionalRepairables.add(new ObjectObjectImmutablePair<>(Items.TRIDENT, Ingredient.of(Items.PRISMARINE_SHARD)));
        ModCommonConfig.modRepairableItems.forEach(entry -> {
            String[] split = entry.split(";");
            if (split.length != 2) {
                LOGGER.warn("Invalid repairable entry: {}, skipping", entry);
            }
            else {
                try {
                    Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(split[0]))/*? if > 1.21.1 {*//*.orElseThrow().value()*//*?}*/;
                    if (!item.equals(Items.AIR)){
                        if (split[1].startsWith("#")) {
                            //? if <= 1.21.1
                            additionalRepairables.add(new ObjectObjectImmutablePair<>(item, Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.parse(split[1].replace("#", ""))))));
                            //? if > 1.21.1
                            /*additionalRepairables.add(new ObjectObjectImmutablePair<>(item, Ingredient.of(BuiltInRegistries.ITEM.get(TagKey.create(Registries.ITEM, ResourceLocation.parse(split[1].replace("#", "")))).orElseThrow())));*/
                        } else {
                            Item repairMaterial = BuiltInRegistries.ITEM.get(ResourceLocation.parse(split[1]))/*? if > 1.21.1 {*//*.orElseThrow().value()*//*?}*/;
                            additionalRepairables.add(new ObjectObjectImmutablePair<>(item, Ingredient.of(repairMaterial)));
                        }
                    }
                } catch (NoSuchElementException e) {
                    LOGGER.warn("Resource location in repairable entry not exist: {}, skipping", entry);
                }
            }
        });
        //? if > 1.21.1 {
        /*BuiltInRegistries.ITEM.forEach(item -> {
            List<Holder<Item>> items = List.of();
            for (ObjectObjectImmutablePair<Item, Ingredient> repair : ModUtil.additionalRepairables) {
                if (repair.left().equals(item)) {
                    items = repair.right().items()/^? if = 1.21.4 {^//^.toList()^//^?}^/;
                    break;
                }
            }
            if (!items.isEmpty()) {
                item.components = PatchedDataComponentMap.fromPatch(item.components, DataComponentPatch.builder().set(DataComponents.REPAIRABLE, new Repairable(HolderSet.direct(items))).build());
            }
        });
        *///?}
    }

    public static ItemStack applyPinnacleUpgrade(ItemStack original, Slot slot, AbstractContainerMenu container, NonNullList<Slot> slots) {
        if (container instanceof SmithingMenu && ModUtil.isPinnacleEnchantmentRecipe(slots) && slot.equals(slots.get(3))) {
            Component itemName = slots.get(1).getItem().getOrDefault(DataComponents.CUSTOM_NAME, original.getItem().getName(original));
            original.set(DataComponents.CUSTOM_NAME, itemName.copy().withStyle(ChatFormatting.LIGHT_PURPLE));
            List<EnchantmentInstance> possibleUpgrades = new ArrayList<>(original.getEnchantments().entrySet()
                    .stream().map(entry -> new EnchantmentInstance(entry.getKey(), entry.getIntValue())).toList());
            possibleUpgrades.removeIf(ei -> ei.enchantment.value().getMaxLevel() == 1);
            EnchantmentInstance toUpgrade = possibleUpgrades.get(Mth.nextInt(RandomSource.create(), 0, possibleUpgrades.size() - 1));
            EnchantmentHelper.updateEnchantments(original, mutable ->
                    mutable.upgrade(toUpgrade.enchantment, toUpgrade.level + 1)
            );
        }
        return original;
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

    public static boolean enchantmentEnabled(Holder<Enchantment> enchantment) {
        return !ED_LOADED || EDCompat.enchantmentEnabled(enchantment);
    }
}
