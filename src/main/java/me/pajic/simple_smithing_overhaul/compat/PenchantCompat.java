package me.pajic.simple_smithing_overhaul.compat;

//? fabric {

import archives.tater.penchant.api.CanEnchantCallback;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.fabricmc.fabric.api.util.TriState;

public class PenchantCompat {
	public static void init() {
		CanEnchantCallback.ITEM.register((stack, _) -> TriState.of(stack.is(ModItems.WHETSTONE)));
	}
}
//?}
