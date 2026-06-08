package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_smithing_overhaul.SSO;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {

    @WrapMethod(method = "computeResult")
    private ItemStack tryHalveRepairCost(ItemStack input, ItemStack additional, Operation<ItemStack> original) {
        if (SSO.CONFIG.grindstoneImprovements.repairCostReductionRecipe.get() && additional.is(Items.NETHERITE_SCRAP)) {
            if (input.isEmpty() || !input.has(DataComponents.REPAIR_COST) || additional.getCount() > 1) return ItemStack.EMPTY;
            ItemStack updatedStack = input.copy();
            updatedStack.set(DataComponents.REPAIR_COST, input.getOrDefault(DataComponents.REPAIR_COST, 0) / 2);
            return updatedStack;
        }
        return original.call(input, additional);
    }
}
