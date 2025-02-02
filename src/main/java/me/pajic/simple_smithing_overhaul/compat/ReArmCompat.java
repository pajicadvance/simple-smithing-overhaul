package me.pajic.simple_smithing_overhaul.compat;

import me.pajic.rearm.Main;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class ReArmCompat {

    public static Optional<Boolean> itemSupportsEnchantment(Holder<Enchantment> enchantment, ItemStack stack) {
        switch (stack.getItem()) {
            case CrossbowItem ignored -> {
                if (Main.CONFIG.crossbow.acceptPower() && enchantment.is(Enchantments.POWER)) return Optional.of(true);
                if (Main.CONFIG.crossbow.acceptInfinity() && enchantment.is(Enchantments.INFINITY)) return Optional.of(true);
                if (Main.CONFIG.crossbow.rejectMultishot() && enchantment.is(Enchantments.MULTISHOT)) return Optional.of(false);
            }
            case AxeItem ignored -> {
                if (Main.CONFIG.axe.acceptLooting() && enchantment.is(Enchantments.LOOTING)) return Optional.of(true);
                if (Main.CONFIG.axe.acceptKnockback() && enchantment.is(Enchantments.KNOCKBACK)) return Optional.of(true);
            }
            case SwordItem ignored -> {
                if (Main.CONFIG.sword.rejectKnockback() && enchantment.is(Enchantments.KNOCKBACK)) return Optional.of(false);
            }
            case BowItem ignored -> {
                if (Main.CONFIG.bow.acceptMultishot() && enchantment.is(Enchantments.MULTISHOT)) return Optional.of(true);
            }
            default -> {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public static Optional<Boolean> areCompatible(Holder<Enchantment> e1, Holder<Enchantment> e2, Collection<EnchantmentInstance> itemEnchantments) {
        if (Main.CONFIG.allowMultipleProtectionEnchantments() && !e1.equals(e2) && e1.is(EnchantmentTags.ARMOR_EXCLUSIVE) && e2.is(EnchantmentTags.ARMOR_EXCLUSIVE)) {
            int itemProtEnchants = 0;
            for (EnchantmentInstance ei : itemEnchantments) {
                if (!ei.enchantment.equals(e1) && ei.enchantment.is(EnchantmentTags.ARMOR_EXCLUSIVE)) {
                    itemProtEnchants++;
                }
            }
            if (itemProtEnchants < Main.CONFIG.maxProtectionEnchantments()) {
                return Optional.of(true);
            }
        }
        return Optional.empty();
    }
}
