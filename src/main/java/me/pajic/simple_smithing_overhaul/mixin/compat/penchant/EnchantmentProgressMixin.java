package me.pajic.simple_smithing_overhaul.mixin.compat.penchant;

//? fabric && >=26.1 {

import archives.tater.penchant.component.EnchantmentProgress;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("penchant")
@Mixin(EnchantmentProgress.class)
public class EnchantmentProgressMixin {

	@WrapOperation(
			method = "updateEnchantmentsForStack",
			at = @At(
					value = "INVOKE",
					target = "Larchives/tater/penchant/component/EnchantmentProgress;updateEnchantments(Larchives/tater/penchant/component/EnchantmentProgress$Mutable;Lnet/minecraft/world/item/enchantment/ItemEnchantments$Mutable;Lnet/minecraft/core/component/DataComponentGetter;)Z"
			)
	)
	private static boolean triggerLevelUp(
			EnchantmentProgress.Mutable progress,
			ItemEnchantments.Mutable enchantments,
			DataComponentGetter components,
			Operation<Boolean> original,
            @Local(argsOnly = true) @Nullable LivingEntity user
	) {
		boolean changed = original.call(progress, enchantments, components);
		if (changed && user instanceof ServerPlayer player) ModCriteria.ENCHANTMENT_LEVEL_UP.trigger(player);
		return changed;
	}
}
//?}
