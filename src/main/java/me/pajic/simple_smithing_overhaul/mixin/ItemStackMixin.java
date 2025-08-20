package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {

    @Shadow @Nullable
    public abstract <T> T set(DataComponentType<? super T> component, @Nullable T value);

    @Shadow @Nullable
    public abstract <T> T remove(DataComponentType<? extends T> component);

    @Shadow
    public abstract int getMaxDamage();

    @Shadow
    public abstract boolean isEnchanted();

    @Unique
    private final ItemStack thisStack = (ItemStack) (Object) this;

    @Inject(
            method = "set",
            at = @At("HEAD")
    )
    private <T> void manageBrokenState(DataComponentType<? super T> component, T value, CallbackInfoReturnable<T> cir) {
        if (component == DataComponents.DAMAGE) {
            if ((int) value < getMaxDamage()) remove(ModDataComponents.BROKEN);
            else switch (Main.CONFIG.streamlinedRepairs.preventItemDestruction.get()) {
                case ALL -> set(ModDataComponents.BROKEN, true);
                case ENCHANTED -> {
                    if (isEnchanted()) set(ModDataComponents.BROKEN, true);
                }
            }
        }
    }

    @WrapWithCondition(
            //? if <= 1.21.1
            method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V",
            //? if > 1.21.1
            /*method = "applyDamage",*/
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"
            )
    )
    private boolean preventDestruction(ItemStack instance, int decrement) {
        return !ModUtil.isBroken(instance);
    }

    @ModifyReturnValue(
            method = "getHoverName",
            at = @At("RETURN")
    )
    private Component changeNameIfBroken(Component original) {
        if (ModUtil.isBroken(thisStack)) {
            MutableComponent name = Component.translatable("item.simple_smithing_overhaul.broken");
            name.append(original);
            name.withStyle(ChatFormatting.RED);
            return name;
        }
        return original;
    }
}
