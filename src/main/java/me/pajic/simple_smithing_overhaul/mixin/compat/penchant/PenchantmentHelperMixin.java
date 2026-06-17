package me.pajic.simple_smithing_overhaul.mixin.compat.penchant;

//? fabric {

import archives.tater.penchant.util.PenchantmentHelper;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;

@IfModLoaded("penchant")
@Mixin(PenchantmentHelper.class)
public class PenchantmentHelperMixin {

	@WrapMethod(method = "canEnchantItem")
	private static boolean allowWhetstone(ItemStack stack, Holder<Enchantment> enchantment, Operation<Boolean> original) {
		return stack.is(ModItems.WHETSTONE) || original.call(stack, enchantment);
	}
}
//?}
