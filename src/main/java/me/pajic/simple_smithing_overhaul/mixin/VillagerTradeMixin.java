package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerTrade.class)
public class VillagerTradeMixin {

	@Shadow @Final private ItemStackTemplate gives;

	@ModifyExpressionValue(
			method = "getOffer",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/storage/loot/providers/number/NumberProvider;getInt(Lnet/minecraft/world/level/storage/loot/LootContext;)I",
					ordinal = 0
			)
	)
	private int modifyMaxUses(int original) {
		if (gives.is(Items.ENCHANTED_BOOK) && SSO.CONFIG.enchantmentLimits.limitBookTradeUses.get() && original > SSO.CONFIG.enchantmentLimits.bookTradeUsesLimit.get()) {
			return SSO.CONFIG.enchantmentLimits.bookTradeUsesLimit.get();
		}
		return original;
	}

	@Inject(
			method = "getOffer",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;remove(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
			)
	)
	private void limitBookEnchantmentLevel(
			LootContext lootContext,
			CallbackInfoReturnable<MerchantOffer> cir,
			@Local(name = "result") ItemStack result
	) {
		if (result.is(Items.ENCHANTED_BOOK)) {
			ItemEnchantments ie = result.get(DataComponents.STORED_ENCHANTMENTS);
			if (ie != null) {
				ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ie);
				enchantments.keySet().forEach(e -> {
					int value = ModUtil.calculateNewEnchantmentLevel(e.value().getMaxLevel(), lootContext.getRandom(), enchantments.getLevel(e));
					if (SSO.CONFIG.enchantmentLimits.limitBookTradeLevel.get() && value > SSO.CONFIG.enchantmentLimits.bookTradeLevelLimit.get()) {
						value = SSO.CONFIG.enchantmentLimits.bookTradeLevelLimit.get();
					}
					enchantments.set(e, value);
				});
				result.set(DataComponents.STORED_ENCHANTMENTS, enchantments.toImmutable());
			}
		}
	}
}
