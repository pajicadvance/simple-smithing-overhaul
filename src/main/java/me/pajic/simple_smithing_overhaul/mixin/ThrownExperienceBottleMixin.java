package me.pajic.simple_smithing_overhaul.mixin;

import me.pajic.simple_smithing_overhaul.Main;
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

    @SuppressWarnings("resource")
    @ModifyArg(
            method = "onHit",
            at = @At(
                    value = "INVOKE",
                    //? if <= 1.21.1
                    target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
                    //? if > 1.21.1
                    /*target = "Lnet/minecraft/world/entity/ExperienceOrb;awardWithDirection(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;I)V"*/
            ),
            index = /*? if <= 1.21.1 {*/2/*?} else {*//*3*//*?}*/
    )
    private int setXpDropAmount(int original) {
        if (Main.CONFIG.improvedExperienceBottle.modifyXpReward.get()) {
            return level().random.nextIntBetweenInclusive(
                    Main.CONFIG.improvedExperienceBottle.minXp.get(),
                    Main.CONFIG.improvedExperienceBottle.maxXp.get()
            );
        }
        return original;
    }
}
