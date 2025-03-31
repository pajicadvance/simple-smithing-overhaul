package me.pajic.simple_smithing_overhaul.mixin;

import me.pajic.simple_smithing_overhaul.config.ModServerConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ThrownExperienceBottle.class)
public abstract class ThrownExperienceBottleMixin extends ThrowableItemProjectile {

    public ThrownExperienceBottleMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyArg(
            method = "onHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
            ),
            index = 2
    )
    private int setXpDropAmount(int original) {
        if (ModServerConfig.modifyXpReward) {
            return level().random.nextIntBetweenInclusive(
                    ModServerConfig.minXp,
                    ModServerConfig.maxXp
            );
        }
        return original;
    }
}
