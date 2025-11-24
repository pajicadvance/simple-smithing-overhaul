package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @ModifyExpressionValue(
            method = "getComponentType",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private static boolean storeEnchantmentsIfWhetstone(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (SSO.CONFIG.whetstone.enableWhetstone.get()) {
            return original || stack.is(ModItems.WHETSTONE);
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "getAvailableEnchantmentResults",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private static boolean handleWhetstoneEnchanting(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (SSO.CONFIG.whetstone.enableWhetstone.get()) {
            return original || stack.is(ModItems.WHETSTONE);
        }
        return original;
    }
}
