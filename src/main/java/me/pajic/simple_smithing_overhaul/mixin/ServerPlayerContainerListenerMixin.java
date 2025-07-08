package me.pajic.simple_smithing_overhaul.mixin;

import me.pajic.simple_smithing_overhaul.Initializer;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if > 1.21.4 {
/*import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
*///?}

@Mixin(targets = "net/minecraft/server/level/ServerPlayer$2")
public class ServerPlayerContainerListenerMixin {

    @Shadow @Final ServerPlayer field_29183;

    @Inject(
            method = "slotChanged",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancements/critereon/InventoryChangeTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V"
            )
    )
    private void grantAdvancement(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack, CallbackInfo ci) {
        int pinnacleCount = 0;
        for (ItemStack item : field_29183.getInventory()./*? if > 1.21.4 {*//*getNonEquipmentItems()*//*?}*//*? if <= 1.21.4 {*/items/*?}*/) {
            if (item.has(Initializer.PINNACLE_COUNT)) pinnacleCount++;
        }
        //? if <= 1.21.4 {
        for (ItemStack item : field_29183.getInventory().armor) {
            if (item.has(Initializer.PINNACLE_COUNT)) pinnacleCount++;
        }
        //?}
        //? if > 1.21.4 {
        /*for (EquipmentSlot slot : Inventory.EQUIPMENT_SLOT_MAPPING.values()) {
            ItemStack item = field_29183.getInventory().equipment.get(slot);
            if (item.has(Initializer.PINNACLE_COUNT)) pinnacleCount++;
        }
        *///?}
        if (pinnacleCount >= 8) ModCriteria.MAXED_OUT.trigger(field_29183);
    }
}
