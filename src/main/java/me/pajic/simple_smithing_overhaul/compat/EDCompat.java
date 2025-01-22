package me.pajic.simple_smithing_overhaul.compat;

import me.pajic.enchantmentdisabler.Main;
import me.pajic.enchantmentdisabler.util.ModUtil;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class EDCompat {
    public static boolean enchantmentEnabled(Holder<Enchantment> enchantment) {
        return Main.CONFIG.disabledEnchantments().stream().noneMatch(entry -> {
            try {
                return enchantment.is(ResourceLocation.parse(entry));
            } catch (ResourceLocationException e) {
                ModUtil.handleResourceLocationException(entry ,e);
                return false;
            }
        });
    }
}
