package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.commons.lang3.function.TriConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {

    @Unique private final ItemStack sso$thisStack = (ItemStack) (Object) this;

    @SuppressWarnings("ConstantValue")
    @Inject(
            method = "set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;",
            at = @At("HEAD")
    )
    private <T> void manageBrokenState(DataComponentType<? super T> type, T value, CallbackInfoReturnable<T> cir) {
        if (type == DataComponents.DAMAGE && sso$thisStack.isDamageableItem()) {
            if ((int) value < sso$thisStack.getMaxDamage()) sso$thisStack.remove(ModDataComponents.BROKEN);
            else if (ModUtil.shouldPreventDestruction(sso$thisStack)) switch (SSO.CONFIG.itemDestructionPrevention.mode.get()) {
                case ALL -> sso$thisStack.set(ModDataComponents.BROKEN, true);
                case ENCHANTED -> {
                    if (sso$thisStack.isEnchanted()) sso$thisStack.set(ModDataComponents.BROKEN, true);
                }
            }
        }
    }

    @WrapWithCondition(
            //? if fabric
            method = "applyDamage",
			//? if neoforge
			//method = "applyDamage(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"
            )
    )
    private boolean preventDestruction(ItemStack instance, int amount) {
        return !ModUtil.isBroken(instance);
    }

    @WrapMethod(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V")
    private void noAttributesIfBroken(EquipmentSlot slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer, Operation<Void> original) {
        if (!ModUtil.isBroken(sso$thisStack)) original.call(slot, consumer);
    }

    @WrapMethod(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Lorg/apache/commons/lang3/function/TriConsumer;)V")
    private void noAttributesIfBroken(
			EquipmentSlotGroup slot, TriConsumer<Holder<Attribute>, AttributeModifier, ItemAttributeModifiers.Display> consumer, Operation<Void> original
    ) {
        if (!ModUtil.isBroken(sso$thisStack)) original.call(slot, consumer);
    }

    @ModifyReturnValue(
            method = "getHoverName",
            at = @At("RETURN")
    )
    private Component changeNameIfBroken(Component original) {
        if (ModUtil.isBroken(sso$thisStack) || (sso$thisStack.is(Items.ELYTRA) && sso$thisStack.getMaxDamage() - sso$thisStack.getDamageValue() == 1)) {
            MutableComponent name = Component.translatable("item.simple_smithing_overhaul.broken", original);
            name.withStyle(ChatFormatting.RED);
            return name;
        }
        return original;
    }
}
