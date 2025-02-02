package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_smithing_overhaul.config.ModServerConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {

    @WrapMethod(method = "computeResult")
    private ItemStack tryHalveRepairCost(ItemStack inputItem, ItemStack additionalItem, Operation<ItemStack> original) {
        if (ModServerConfig.repairCostReductionRecipe && additionalItem.is(Items.NETHERITE_SCRAP)) {
            if (inputItem.isEmpty() || !inputItem.has(DataComponents.REPAIR_COST) || additionalItem.getCount() > 1) return ItemStack.EMPTY;
            ItemStack updatedStack = inputItem.copy();
            updatedStack.set(DataComponents.REPAIR_COST, inputItem.get(DataComponents.REPAIR_COST) / 2);
            return updatedStack;
        }
        return original.call(inputItem, additionalItem);
    }
}
