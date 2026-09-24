package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.SSO;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? <26.1 {
/*import net.minecraft.ChatFormatting;
import org.objectweb.asm.Opcodes;
*///?} else {
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.network.chat.MutableComponent;
//?}

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @ModifyExpressionValue(
            method = "getFullname",
            at = @At(
                    //? <26.1 {
                    /*value = "FIELD",
                    target = "Lnet/minecraft/ChatFormatting;GRAY:Lnet/minecraft/ChatFormatting;",
                    opcode = Opcodes.GETSTATIC
                    *///?} else {
                    value = "INVOKE",
					target = "Lnet/minecraft/network/chat/ComponentUtils;mergeStyles(Lnet/minecraft/network/chat/MutableComponent;Lnet/minecraft/network/chat/Style;)Lnet/minecraft/network/chat/MutableComponent;",
					ordinal = 1
                    //?}
            )
    )
    //~ if <26.1 'MutableComponent' -> 'ChatFormatting' {
    private static MutableComponent recolorIfPinnacle(
            MutableComponent original,
			@Local(argsOnly = true) Holder<Enchantment> enchantment,
			@Local(argsOnly = true) int level
    ) {
        if (SSO.CONFIG.pinnacleEnchantment.colorPinnacleItemName.get() && level > enchantment.value().getMaxLevel()) {
            //? <26.1 {
            /*return MutableComponent.getByName(SSO.CONFIG.pinnacleEnchantment.pinnacleItemNameColor.get().replace(" ", "_").toUpperCase());
            *///?} else {
            return ModUtil.colorText(original);
            //?}
        }
        return original;
    }
    //~}
}
