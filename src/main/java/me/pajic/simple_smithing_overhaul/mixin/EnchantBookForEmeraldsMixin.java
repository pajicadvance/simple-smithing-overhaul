package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(VillagerTrades.EnchantBookForEmeralds.class)
public class EnchantBookForEmeraldsMixin {

    @SuppressWarnings({"rawtypes", "OptionalUsedAsFieldOrParameterType"})
    @WrapOperation(
            method = "getOffer",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/item/trading/ItemCost;Ljava/util/Optional;Lnet/minecraft/world/item/ItemStack;IIF)Lnet/minecraft/world/item/trading/MerchantOffer;"
            )
    )
    private MerchantOffer limitTradeUses(ItemCost baseCostA, Optional costB, ItemStack result, int maxUses, int xp, float priceMultiplier, Operation<MerchantOffer> original) {
        if (SSO.CONFIG.enchantmentLimits.limitBookTradeUses.get()) {
            return original.call(baseCostA, costB, result, SSO.CONFIG.enchantmentLimits.bookTradeUsesLimit.get(), xp, priceMultiplier);
        }
        else {
            return original.call(baseCostA, costB, result, maxUses, xp, priceMultiplier);
        }
    }

    @ModifyExpressionValue(
            method = "getOffer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;nextInt(Lnet/minecraft/util/RandomSource;II)I"
            )
    )
    private int limitMaxEnchantmentLevel(int original, @Local Holder<Enchantment> enchantment, @Local(argsOnly = true) RandomSource randomSource) {
        int value = ModUtil.calculateNewEnchantmentLevel(enchantment.value().getMaxLevel(), randomSource, original);
        if (SSO.CONFIG.enchantmentLimits.limitBookTradeLevel.get() && value > SSO.CONFIG.enchantmentLimits.bookTradeLevelLimit.get()) {
            value = SSO.CONFIG.enchantmentLimits.bookTradeLevelLimit.get();
        }
        return value;
    }
}
