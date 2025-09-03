package me.pajic.simple_smithing_overhaul.recipe;

import me.pajic.simple_smithing_overhaul.Initializer;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PortableItemRepairRecipe extends CustomRecipe {
    private ItemStack itemToRepair;
    private List<ItemStack> repairMaterials;
    private List<ItemStack> repairableItems;
    private int unitCost;
    private RandomSource random;

    public PortableItemRepairRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        random = level.getRandom();
        List<ItemStack> whetstones = input.items().stream().filter(itemStack -> itemStack.is(ModItems.WHETSTONE)).toList();
        List<ItemStack> flint = input.items().stream().filter(itemStack -> itemStack.is(Items.FLINT)).toList();
        if (whetstones.isEmpty() ^ flint.isEmpty()) {
            if (whetstones.size() == 1) {
                ItemStack whetstone = whetstones.getFirst();
                repairableItems = input.items().stream().filter(itemStack ->
                        itemStack.isDamageableItem() && !itemStack.is(ModItems.WHETSTONE)).toList();
                if (!repairableItems.isEmpty()) {
                    itemToRepair = repairableItems.getFirst();
                    if (itemToRepair.isDamaged() && whetstone.getDamageValue() < whetstone.getMaxDamage()) {
                        ItemEnchantments whetstoneEnchantments = whetstone.getOrDefault(
                                DataComponents.STORED_ENCHANTMENTS,
                                ItemEnchantments.EMPTY
                        );
                        if (itemToRepair.getEnchantments().entrySet().stream().allMatch(
                                entry -> whetstoneEnchantments.getLevel(entry.getKey()) >= Math.min(
                                        entry.getIntValue(),
                                        entry.getKey().value().getMaxLevel()
                                )
                        )) {
                            return processRepair(input, itemToRepair);
                        }
                    }
                }
            } else if (flint.size() == 1) {
                repairableItems = input.items().stream().filter(itemStack ->
                        itemStack.isDamageableItem() && !itemStack.is(Items.FLINT)).toList();
                if (!repairableItems.isEmpty()) {
                    itemToRepair = repairableItems.getFirst();
                    if (itemToRepair.isDamaged() && !itemToRepair.isEnchanted()) {
                        return processRepair(input, itemToRepair);
                    }
                }
            }
        }
        return false;
    }

    private boolean processRepair(CraftingInput input, ItemStack itemToRepair) {
        unitCost = ModUtil.determineUnitCost(itemToRepair);
        int damageRepairedPerUnit = Math.round((float) itemToRepair.getMaxDamage() / unitCost);
        int unitsToMaxRepair = itemToRepair.getDamageValue() / damageRepairedPerUnit;
        repairMaterials = input.items().stream().filter(itemStack ->
            //? if <= 1.21.1 {
            ModUtil.hasAdditionalRepair(itemToRepair, itemStack) ||
            itemToRepair.getItem().isValidRepairItem(itemToRepair, itemStack)).toList();
            //?}
            //? if > 1.21.1 {
            /*{
                if (itemToRepair.has(DataComponents.REPAIRABLE)) {
                    return itemToRepair.get(DataComponents.REPAIRABLE).isValidRepairItem(itemStack);
                }
                return false;
            }).toList();
        *///?}
        return !repairMaterials.isEmpty() && repairMaterials.size() <= unitsToMaxRepair + 1;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input, HolderLookup.@NotNull Provider registries) {
        ItemStack outputItem = itemToRepair.copy();
        outputItem.setDamageValue(
                outputItem.getDamageValue() - (Math.round((float) outputItem.getMaxDamage() / unitCost) * repairMaterials.size())
        );
        outputItem.set(ModDataComponents.REPAIR_COUNT, outputItem.getOrDefault(ModDataComponents.REPAIR_COUNT, 0) + 1);
        return outputItem;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull CraftingInput input) {
        List<ItemStack> otherGear = repairableItems.subList(1, repairableItems.size());
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < remainingItems.size(); i++) {
            ItemStack itemStack = input.getItem(i);
            if (itemStack.is(ModItems.WHETSTONE)) {
                float degradationChance = Main.CONFIG.anvilImprovements.modifyDegradationChance.get() ?
                        Main.CONFIG.anvilImprovements.degradationChance.get() / 50 : 0.24F;
                if (random.nextFloat() < degradationChance) {
                    itemStack.setDamageValue(itemStack.getDamageValue() + 1);
                }
                remainingItems.set(i, itemStack.copy());
            } else if (otherGear.contains(itemStack)) {
                remainingItems.set(i, itemStack.copy());
            } else if (!itemStack.is(Items.FLINT) && !itemStack.is(repairMaterials.getFirst().getItem()) && !itemStack.is(repairableItems.getFirst().getItem())) {
                remainingItems.set(i, new ItemStack(itemStack.getItem()));
            }
        }
        return remainingItems;
    }

    //? if <= 1.21.1 {
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 4;
    }
    //?}

    @Override
    public @NotNull RecipeSerializer<?/*? if > 1.21.1 {*/ /*extends CustomRecipe*//*?}*/> getSerializer() {
        return Initializer.PORTABLE_ITEM_REPAIR;
    }
}
