package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuMixin {

    @ModifyExpressionValue(
            method = "getEnchantmentList",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private boolean getEnchantmentList_handleWhetstoneEnchanting(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (SSO.CONFIG.whetstone.enableWhetstone.get()) {
            return original || stack.is(ModItems.WHETSTONE);
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "slotsChanged",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isEnchantable()Z"
            )
    )
    private boolean slotsChanged_handleWhetstoneEnchanting(boolean original, @Local ItemStack stack) {
        if (SSO.CONFIG.whetstone.enableWhetstone.get() && stack.is(ModItems.WHETSTONE)) {
            return original && stack.get(DataComponents.STORED_ENCHANTMENTS).isEmpty();
        }
        return original;
    }

    @ModifyExpressionValue(
			//? if fabric
            method = "method_17411",
			//? if neoforge
			//method = "lambda$slotsChanged$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/EnchantingTableBlock;isValidBookShelf(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean limitTablePower(boolean original, @Local /*? if fabric {*/int/*?} else {*//*float*//*?}*/ ix) {
        if (SSO.CONFIG.enchantmentLimits.limitEnchantingTablePower.get() && ix >= SSO.CONFIG.enchantmentLimits.enchantingTablePowerLimit.get()) {
            return false;
        }
        return original;
    }
}
