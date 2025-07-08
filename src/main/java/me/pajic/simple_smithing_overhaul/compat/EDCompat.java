package me.pajic.simple_smithing_overhaul.compat;

import me.pajic.enchantmentdisabler.Main;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

public class EDCompat {
    public static boolean enchantmentEnabled(Holder<Enchantment> enchantment) {
        return Main.CONFIG.disabler.disabledEnchantments.stream().noneMatch(enchantment::is);
    }
}
