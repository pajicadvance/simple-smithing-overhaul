package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuMixin {

    @ModifyExpressionValue(
            method = "getEnchantmentList",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
            )
    )
    private boolean getEnchantmentList_handleWhetstoneEnchanting(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (SSO.CONFIG.portableItemRepair.enableWhetstone.get()) {
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
    private boolean slotsChanged_handleWhetstoneEnchanting(boolean original, @Local(name = "itemStack") ItemStack itemStack) {
        if (SSO.CONFIG.portableItemRepair.enableWhetstone.get() && itemStack.is(ModItems.WHETSTONE)) {
            return original && itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty();
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "lambda$slotsChanged$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/EnchantingTableBlock;isValidBookShelf(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean limitTablePower(boolean original, @Local(name = "bookcases") /*? if fabric {*/int/*?} else {*//*float*//*?}*/ bookcases) {
        if (SSO.CONFIG.enchantmentLimits.limitEnchantingTablePower.get() && bookcases >= SSO.CONFIG.enchantmentLimits.enchantingTablePowerLimit.get()) {
            return false;
        }
        return original;
    }
}
