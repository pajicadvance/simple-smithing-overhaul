package me.pajic.simple_smithing_overhaul.mixin;

import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if > 1.21.1 {
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
//?}

@Mixin(targets = "net/minecraft/server/level/ServerPlayer$2")
public class ServerPlayerContainerListenerMixin {

	//? if fabric
	@Shadow @Final ServerPlayer field_29183;
	//? if neoforge
	/*@Shadow @Final ServerPlayer this$0;*/

	@Inject(
            method = "slotChanged",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancements/critereon/InventoryChangeTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V"
            )
    )
    private void grantAdvancement(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack, CallbackInfo ci) {
        int pinnacleCount = 0;
        for (ItemStack item : /*? if fabric {*/field_29183/*?} else {*//*this$0*//*?}*/.getInventory()./*? if > 1.21.1 {*/getNonEquipmentItems()/*?} else {*//*items*//*?}*/) {
            if (item.has(ModDataComponents.PINNACLE_COUNT)) pinnacleCount++;
        }
        //? if <= 1.21.1 {
        /*for (ItemStack item : /^? if fabric {^/field_29183/^?} else {^//^this$0^//^?}^/.getInventory().armor) {
            if (item.has(ModDataComponents.PINNACLE_COUNT)) pinnacleCount++;
        }
        *///?} else {
        for (EquipmentSlot slot : Inventory.EQUIPMENT_SLOT_MAPPING.values()) {
            ItemStack item = /*? if fabric {*/field_29183/*?} else {*//*this$0*//*?}*/.getInventory().equipment.get(slot);
            if (item.has(ModDataComponents.PINNACLE_COUNT)) pinnacleCount++;
        }
        //?}
        if (pinnacleCount >= 8) ModCriteria.MAXED_OUT.trigger(/*? if fabric {*/field_29183/*?} else {*//*this$0*//*?}*/);
    }
}
