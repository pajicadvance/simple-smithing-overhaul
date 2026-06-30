package me.pajic.simple_smithing_overhaul.mixin;

import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.util.CompatFlags;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EnchantWithLevelsFunction.class)
public class EnchantWithLevelsFunctionMixin {

    @ModifyArg(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;enchantItem(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/RegistryAccess;Ljava/util/Optional;)Lnet/minecraft/world/item/ItemStack;"
            ),
            index = 2
    )
    private int limitMaxEnchantmentLevel(int level) {
        if (
				SSO.CONFIG.enchantmentLimits.limitEnchantedLootPower.get() &&
				level > SSO.CONFIG.enchantmentLimits.enchantedLootPowerLimit.get()
		) {
            return SSO.CONFIG.enchantmentLimits.enchantedLootPowerLimit.get();
        }
        return level;
    }
}
