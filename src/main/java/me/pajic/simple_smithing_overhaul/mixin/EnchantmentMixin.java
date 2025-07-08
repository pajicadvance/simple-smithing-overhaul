package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.Main;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @ModifyExpressionValue(
            method = "getFullname",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/ChatFormatting;GRAY:Lnet/minecraft/ChatFormatting;")
    )
    private static ChatFormatting recolorIfPinnacle(ChatFormatting original,
                                                    @Local(argsOnly = true) Holder<Enchantment> enchantment,
                                                    @Local(argsOnly = true) int level
    ) {
        if (Main.CONFIG.pinnacleEnchantment.colorPinnacleItemName.get() && level > enchantment.value().getMaxLevel()) {
            return ChatFormatting.getByName(Main.CONFIG.pinnacleEnchantment.pinnacleItemNameColor.get().replace(" ", "_").toUpperCase());
        }
        return original;
    }
}
