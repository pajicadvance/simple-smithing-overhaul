package me.pajic.simple_smithing_overhaul.compat;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import me.pajic.enchantmentdisabler.util.ModUtil;

public class EDCompat {
    public static boolean enchantmentEnabled(Holder<Enchantment> enchantment) {
		return ModUtil.filter(enchantment, ModUtil::allSourcesDisabled);
    }
}
