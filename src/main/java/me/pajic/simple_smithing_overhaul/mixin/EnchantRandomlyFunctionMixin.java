package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantRandomlyFunction.class)
public class EnchantRandomlyFunctionMixin {

    @ModifyExpressionValue(
            method = "enchantItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;nextInt(Lnet/minecraft/util/RandomSource;II)I"
            )
    )
    private static int modifyEnchantmentLevel(int original,
                                              @Local(argsOnly = true) Holder<Enchantment> enchantmentHolder,
                                              @Local(argsOnly = true) RandomSource randomSource
    ) {
        return ModUtil.calculateNewEnchantmentLevel(enchantmentHolder.value().getMaxLevel(), randomSource, original);
    }
}
