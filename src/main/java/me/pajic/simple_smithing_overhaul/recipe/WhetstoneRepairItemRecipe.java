package me.pajic.simple_smithing_overhaul.recipe;

import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WhetstoneRepairItemRecipe extends CustomRecipe {
    private ItemStack itemToRepair;
    private List<ItemStack> repairMaterials;
    private List<ItemStack> repairableItems;
    private int unitCost;

    public WhetstoneRepairItemRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        List<ItemStack> whetstones = input.items().stream().filter(itemStack -> itemStack.is(ModItems.WHETSTONE)).toList();
        if (whetstones.size() == 1) {
            repairableItems = input.items().stream().filter(itemStack ->
                    itemStack.isDamageableItem() && !itemStack.is(ModItems.WHETSTONE)).toList();
            if (!repairableItems.isEmpty()) {
                itemToRepair = repairableItems.getFirst();
                if (itemToRepair.isDamaged()) {
                    ItemEnchantments whetstoneEnchantments = whetstones.getFirst().getOrDefault(
                            DataComponents.STORED_ENCHANTMENTS,
                            ItemEnchantments.EMPTY
                    );
                    if (itemToRepair.getEnchantments().entrySet().stream().allMatch(
                            entry -> whetstoneEnchantments.getLevel(entry.getKey()) >= Math.min(
                                    entry.getIntValue(),
                                    entry.getKey().value().getMaxLevel()
                            )
                    )) {
                        unitCost = ModUtil.determineUnitCost(itemToRepair);
                        int damageRepairedPerUnit = itemToRepair.getMaxDamage() / unitCost;
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
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input, HolderLookup.@NotNull Provider registries) {
        ItemStack outputItem = itemToRepair.copy();
        outputItem.setDamageValue(outputItem.getDamageValue() - ((outputItem.getMaxDamage() / unitCost) * repairMaterials.size()));
        outputItem.set(Main.REPAIR_COUNT, outputItem.getOrDefault(Main.REPAIR_COUNT, 0) + 1);
        return outputItem;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull CraftingInput input) {
        List<ItemStack> otherGear = repairableItems.subList(1, repairableItems.size());
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < remainingItems.size(); i++) {
            ItemStack itemStack = input.getItem(i);
            if (itemStack.is(ModItems.WHETSTONE)) {
                itemStack.setDamageValue(itemStack.getDamageValue() + repairMaterials.size());
                if (itemStack.getDamageValue() < itemStack.getMaxDamage()) {
                    remainingItems.set(i, itemStack.copy());
                }
            } else if (otherGear.contains(itemStack)) {
                remainingItems.set(i, itemStack.copy());
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
        return Main.WHETSTONE_REPAIR_ITEM;
    }
}
