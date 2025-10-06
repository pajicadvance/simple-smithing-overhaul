package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_smithing_overhaul.Main;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if > 1.21.1 {
/*import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.commons.lang3.function.TriConsumer;
*///?}

import java.util.function.BiConsumer;

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

    @Shadow
    public abstract boolean isDamageableItem();

    @Shadow
    public abstract boolean is(Item item);

    @Shadow
    public abstract int getDamageValue();

    @Unique
    private final ItemStack thisStack = (ItemStack) (Object) this;

    @SuppressWarnings("ConstantValue")
    @Inject(
            method = "set",
            at = @At("HEAD")
    )
    private <T> void manageBrokenState(DataComponentType<? super T> component, T value, CallbackInfoReturnable<T> cir) {
        if (component == DataComponents.DAMAGE && isDamageableItem()) {
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

    @WrapMethod(method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V")
    private void noAttributesIfBroken(EquipmentSlot equipmentSLot, BiConsumer<Holder<Attribute>, AttributeModifier> action, Operation<Void> original) {
        if (!ModUtil.isBroken(thisStack)) original.call(equipmentSLot, action);
    }

    @WrapMethod(
            //? if <= 1.21.1
            method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V"
            //? if > 1.21.1
            /*method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Lorg/apache/commons/lang3/function/TriConsumer;)V"*/
    )
    private void noAttributesIfBroken(
            //? if <= 1.21.1
            EquipmentSlotGroup slotGroup, BiConsumer<Holder<Attribute>, AttributeModifier> action, Operation<Void> original
            //? if > 1.21.1
            /*EquipmentSlotGroup slotGroup, TriConsumer<Holder<Attribute>, AttributeModifier, ItemAttributeModifiers.Display> action, Operation<Void> original*/
    ) {
        if (!ModUtil.isBroken(thisStack)) original.call(slotGroup, action);
    }

    @ModifyReturnValue(
            method = "getHoverName",
            at = @At("RETURN")
    )
    private Component changeNameIfBroken(Component original) {
        if (ModUtil.isBroken(thisStack) || (is(Items.ELYTRA) && getMaxDamage() - getDamageValue() == 1)) {
            MutableComponent name = Component.translatable("item.simple_smithing_overhaul.broken", original);
            name.withStyle(ChatFormatting.RED);
            return name;
        }
        return original;
    }
}
