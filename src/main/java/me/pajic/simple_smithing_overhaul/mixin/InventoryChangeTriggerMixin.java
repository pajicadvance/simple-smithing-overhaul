package me.pajic.simple_smithing_overhaul.mixin;

import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryChangeTrigger.class)
public class InventoryChangeTriggerMixin {

	@Inject(
			method = "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V",
			at = @At("HEAD")
	)
	private void grantAdvancement(ServerPlayer serverPlayer, Inventory inventory, ItemStack itemStack, CallbackInfo ci) {
		int pinnacleCount = 0;
		for (ItemStack item : inventory) {
			if (item.has(ModDataComponents.PINNACLE_COUNT)) pinnacleCount++;
		}
		for (EquipmentSlot slot : Inventory.EQUIPMENT_SLOT_MAPPING.values()) {
			ItemStack item = inventory.equipment.get(slot);
			if (item.has(ModDataComponents.PINNACLE_COUNT)) pinnacleCount++;
		}
		if (pinnacleCount >= 8) ModCriteria.MAXED_OUT.trigger(serverPlayer);
	}
}
