package me.pajic.simple_smithing_overhaul.util;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.fabricmc.loader.api.FabricLoader;
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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//? if > 1.21.1
/*import net.minecraft.world.item.enchantment.Repairable;*/

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ModUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger("SimpleSmithingOverhaul-Util");

    public static final List<ObjectObjectImmutablePair<Item, Ingredient>> additionalRepairables = new ArrayList<>();

    public static int determineUnitCost(ItemStack stack) {
        if (Main.CONFIG.streamlinedRepairs.modifyAnvilRepairUnitCosts()) {

            if (stack.is(ItemTags.HEAD_ARMOR)) return Main.CONFIG.streamlinedRepairs.armor.headArmorUnits();
            if (stack.is(ItemTags.CHEST_ARMOR)) return Main.CONFIG.streamlinedRepairs.armor.chestArmorUnits();
            if (stack.is(ItemTags.LEG_ARMOR)) return Main.CONFIG.streamlinedRepairs.armor.legArmorUnits();
            if (stack.is(ItemTags.FOOT_ARMOR)) return Main.CONFIG.streamlinedRepairs.armor.footArmorUnits();
            if (stack.getItem() instanceof AnimalArmorItem aai) {
                AnimalArmorItem.BodyType type =
                        //? if <= 1.21.1
                        aai.getBodyType();
                        //? if > 1.21.1
                        /*aai.bodyType;*/
                if (type.equals(AnimalArmorItem.BodyType.EQUESTRIAN)) return Main.CONFIG.streamlinedRepairs.armor.horseArmorUnits();
                if (type.equals(AnimalArmorItem.BodyType.CANINE)) return Main.CONFIG.streamlinedRepairs.armor.wolfArmorUnits();
            }

            if (stack.is(ItemTags.PICKAXES)) return Main.CONFIG.streamlinedRepairs.tools.pickaxeUnits();
            if (stack.is(ItemTags.AXES)) return Main.CONFIG.streamlinedRepairs.tools.axeUnits();
            if (stack.is(ItemTags.SWORDS)) return Main.CONFIG.streamlinedRepairs.tools.swordUnits();
            if (stack.is(ItemTags.HOES)) return Main.CONFIG.streamlinedRepairs.tools.hoeUnits();
            if (stack.is(ItemTags.SHOVELS)) return Main.CONFIG.streamlinedRepairs.tools.shovelUnits();

            if (stack.is(Items.SHIELD)) return Main.CONFIG.streamlinedRepairs.uniqueItems.shieldUnits();
            if (stack.is(Items.ELYTRA)) return Main.CONFIG.streamlinedRepairs.uniqueItems.elytraUnits();
            if (stack.is(Items.MACE)) return Main.CONFIG.streamlinedRepairs.uniqueItems.maceUnits();
            if (stack.is(ModItems.WHETSTONE)) return Main.CONFIG.streamlinedRepairs.uniqueItems.whetstoneUnits();
            if (stack.is(Items.BOW)) return Main.CONFIG.streamlinedRepairs.uniqueItems.bowUnits();
            if (stack.is(Items.CROSSBOW)) return Main.CONFIG.streamlinedRepairs.uniqueItems.crossbowUnits();
            if (stack.is(Items.FLINT_AND_STEEL)) return Main.CONFIG.streamlinedRepairs.uniqueItems.flintAndSteelUnits();
            if (stack.is(Items.SHEARS)) return Main.CONFIG.streamlinedRepairs.uniqueItems.shearsUnits();
            if (stack.is(Items.TRIDENT)) return Main.CONFIG.streamlinedRepairs.uniqueItems.tridentUnits();
            if (stack.is(Items.BRUSH)) return Main.CONFIG.streamlinedRepairs.uniqueItems.brushUnits();
            if (stack.is(Items.FISHING_ROD)) return Main.CONFIG.streamlinedRepairs.uniqueItems.fishingRodUnits();
            if (stack.is(Items.CARROT_ON_A_STICK)) return Main.CONFIG.streamlinedRepairs.uniqueItems.carrotOnAStickUnits();
            if (stack.is(Items.WARPED_FUNGUS_ON_A_STICK)) return Main.CONFIG.streamlinedRepairs.uniqueItems.warpedFungusOnAStickUnits();

            for (String s : Main.CONFIG.streamlinedRepairs.modItemUnitCosts()) {
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
        if (!FabricLoader.getInstance().isModLoaded("bettertridents"))
            additionalRepairables.add(new ObjectObjectImmutablePair<>(Items.TRIDENT, Ingredient.of(Items.PRISMARINE_SHARD)));
        Main.CONFIG.streamlinedRepairs.modRepairableItems().forEach(entry -> {
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
}
