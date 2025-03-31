package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.config.ModServerConfig;
import me.pajic.simple_smithing_overhaul.loot.LootUtil;
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

    @WrapOperation(
            method = "getOffer",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/item/trading/ItemCost;Ljava/util/Optional;Lnet/minecraft/world/item/ItemStack;IIF)Lnet/minecraft/world/item/trading/MerchantOffer;"
            )
    )
    private MerchantOffer limitTradeUses(ItemCost baseCostA, Optional costB, ItemStack result, int maxUses, int xp, float priceMultiplier, Operation<MerchantOffer> original) {
        if (ModServerConfig.limitBookTradeUses) {
            return original.call(baseCostA, costB, result, ModServerConfig.bookTradeUsesLimit, xp, priceMultiplier);
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
        int value = LootUtil.calculateNewEnchantmentLevel(enchantment.value().getMaxLevel(), randomSource, original);
        if (ModServerConfig.limitBookTradeLevel && value > ModServerConfig.bookTradeLevelLimit) {
            value = ModServerConfig.bookTradeLevelLimit;
        }
        return value;
    }
}