package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? <26.1 {
/*import net.minecraft.util.RandomSource;
*///?} else {
import net.minecraft.world.level.storage.loot.LootContext;
//?}

@Mixin(EnchantRandomlyFunction.class)
public class EnchantRandomlyFunctionMixin {

    @ModifyExpressionValue(
            method = "enchantItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;nextInt(Lnet/minecraft/util/RandomSource;II)I"
            )
    )
    private static int modifyEnchantmentLevel(
            int original,
            @Local(argsOnly = true) Holder<Enchantment> enchantment,
            //~ if >=26.1 'RandomSource random' -> 'LootContext context'
            @Local(argsOnly = true) LootContext context
    ) {
        //~ if >=26.1 'random' -> 'context.getRandom()'
        return ModUtil.calculateNewEnchantmentLevel(enchantment.value().getMaxLevel(), context.getRandom(), original);
    }
}
