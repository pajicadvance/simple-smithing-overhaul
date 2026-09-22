package me.pajic.simple_smithing_overhaul.mixin;

import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? >=26.2 {
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
//?} else >=26.1 {
/*import net.minecraft.advancements.criterion.InventoryChangeTrigger;
*///?} else {
/*import net.minecraft.advancements.critereon.InventoryChangeTrigger;
*///?}

//? >=26.1
import net.minecraft.world.entity.EquipmentSlot;

@Mixin(InventoryChangeTrigger.class)
public class InventoryChangeTriggerMixin {

	@Inject(
			method = "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V",
			at = @At("HEAD")
	)
	private void grantAdvancement(ServerPlayer player, Inventory inventory, ItemStack changedItem, CallbackInfo ci) {
		int pinnacleCount = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
			if (inventory.getItem(i).has(ModDataComponents.PINNACLE_COUNT)) pinnacleCount++;
		}
        //? >=26.1 {
		for (EquipmentSlot slot : Inventory.EQUIPMENT_SLOT_MAPPING.values()) {
			ItemStack item = inventory.equipment.get(slot);
			if (item.has(ModDataComponents.PINNACLE_COUNT)) pinnacleCount++;
		}
        //?}
		if (pinnacleCount >= 8) ModCriteria.MAXED_OUT.trigger(player);
	}
}
