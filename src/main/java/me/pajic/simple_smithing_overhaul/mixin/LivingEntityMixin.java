package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    @WrapWithCondition(
            method = "breakItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"
            )
    )
    private boolean noBreakSoundIfBrokenAlready(Level instance, double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch, boolean distanceDelay, @Local(argsOnly = true) ItemStack itemStack) {
        return !ModUtil.isBroken(itemStack);
    }

    @WrapWithCondition(
            method = "onEquippedItemBroken",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"
            )
    )
    private boolean noBreakEventIfBrokenAlready(Level instance, Entity entity, byte state, @Local(argsOnly = true) EquipmentSlot slot) {
        return !ModUtil.isBroken(getItemBySlot(slot));
    }
}
