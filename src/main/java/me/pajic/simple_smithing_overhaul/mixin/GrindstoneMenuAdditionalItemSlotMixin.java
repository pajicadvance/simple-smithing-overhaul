package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.SSO;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/world/inventory/GrindstoneMenu$3")
public class GrindstoneMenuAdditionalItemSlotMixin {

    @ModifyReturnValue(
            method = "mayPlace",
            at = @At("RETURN")
    )
    private boolean modifyMayPlace(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (SSO.CONFIG.grindstoneImprovements.repairCostReductionRecipe.get()) {
            return original || stack.is(Items.NETHERITE_SCRAP);
        }
        return original;
    }
}
