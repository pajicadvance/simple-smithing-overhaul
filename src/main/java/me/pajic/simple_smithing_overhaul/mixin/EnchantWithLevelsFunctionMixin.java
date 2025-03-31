package me.pajic.simple_smithing_overhaul.mixin;

import me.pajic.simple_smithing_overhaul.config.ModServerConfig;
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
        if (ModServerConfig.limitEnchantedLootPower && level > ModServerConfig.enchantedLootPowerLimit) {
            return ModServerConfig.enchantedLootPowerLimit;
        }
        return level;
    }
}
