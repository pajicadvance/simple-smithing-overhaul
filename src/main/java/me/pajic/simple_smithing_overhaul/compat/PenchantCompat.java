package me.pajic.simple_smithing_overhaul.compat;

//? fabric && >=26.1 {

import archives.tater.penchant.api.CanEnchantCallback;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class PenchantCompat {
	public static void init() {
		CanEnchantCallback.ITEM.register((stack, _) -> {
			if (stack.is(ModItems.WHETSTONE)) return TriState.TRUE;
			return TriState.DEFAULT;
		});
		CanEnchantCallback.STACK.register((stack, enchantment) -> {
			if (stack.is(ModItems.WHETSTONE)) return TriState.of(EnchantmentHelper.isEnchantmentCompatible(
					EnchantmentHelper.getEnchantmentsForCrafting(stack).keySet(),
					enchantment
			));
			return TriState.DEFAULT;
		});
	}
}
//?}
