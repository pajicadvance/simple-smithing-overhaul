package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @ModifyExpressionValue(
            method = "getFullname",
            at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/chat/ComponentUtils;mergeStyles(Lnet/minecraft/network/chat/MutableComponent;Lnet/minecraft/network/chat/Style;)Lnet/minecraft/network/chat/MutableComponent;",
					ordinal = 1
			)
    )
    private static MutableComponent recolorIfPinnacle(
			MutableComponent original,
			@Local(argsOnly = true, name = "enchantment") Holder<Enchantment> enchantment,
			@Local(argsOnly = true, name = "level") int level
    ) {
        if (SSO.CONFIG.pinnacleEnchantment.colorPinnacleItemName.get() && level > enchantment.value().getMaxLevel()) {
            return ModUtil.colorText(original);
        }
        return original;
    }
}
