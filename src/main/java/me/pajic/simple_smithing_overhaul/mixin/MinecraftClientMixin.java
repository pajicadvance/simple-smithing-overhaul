package me.pajic.simple_smithing_overhaul.mixin;

import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

	@Inject(
			method = "updateLevelInEngines",
			at = @At("TAIL")
	)
	private void afterClientWorldChange(ClientLevel level, CallbackInfo ci) {
		ModUtil.itemSuggestions.clear();
		ModUtil.itemSuggestions.addAll(BuiltInRegistries.ITEM.keySet().stream().map(Identifier::toString).toList());
		BuiltInRegistries.ITEM./*? if > 1.21.1 {*/listTagIds()/*?} else {*//*getTagNames()*//*?}*/.forEach(tag -> ModUtil.itemSuggestions.add("#" + tag.location()));
	}
}
