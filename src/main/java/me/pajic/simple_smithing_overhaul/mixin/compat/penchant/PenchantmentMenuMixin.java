package me.pajic.simple_smithing_overhaul.mixin.compat.penchant;

//? fabric && >=26.1 {

import archives.tater.penchant.menu.PenchantmentMenu;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@IfModLoaded("penchant")
@Mixin(value = PenchantmentMenu.class, priority = 1500)
public abstract class PenchantmentMenuMixin {

	@Shadow @Final private Player player;

	@Shadow public abstract ItemStack getEnchantingStack();

	@WrapMethod(method = "handleEnchant")
	private void triggerLevelUp(Holder<Enchantment> enchantment, Operation<Void> original) {
		int levelBefore = getEnchantingStack().getEnchantments().getLevel(enchantment);
		original.call(enchantment);
		ItemEnchantments enchantmentsAfter = getEnchantingStack().getEnchantments();
		if (levelBefore > 0 && enchantmentsAfter.getLevel(enchantment) > levelBefore && player instanceof ServerPlayer serverPlayer) {
			ModCriteria.ENCHANTMENT_LEVEL_UP.trigger(serverPlayer);
		}
	}
}
//?}
