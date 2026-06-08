package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Shadow public abstract ItemStack getUseItem();

	@WrapWithCondition(
            method = "breakItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"
            )
    )
    private boolean noBreakSoundIfBrokenAlready(Level instance, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay, @Local(argsOnly = true, name = "itemStack") ItemStack itemStack) {
        return !ModUtil.isBroken(itemStack);
    }

	@WrapMethod(method = "getItemBlockingWith")
	private ItemStack preventShieldBlockIfBroken(Operation<ItemStack> original) {
		return ModUtil.isBroken(getUseItem()) ? null : original.call();
	}
}
