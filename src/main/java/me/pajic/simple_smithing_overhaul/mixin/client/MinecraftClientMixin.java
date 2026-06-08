package me.pajic.simple_smithing_overhaul.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftClientMixin {

	@Inject(
			method = "updateLevelInEngines(Lnet/minecraft/client/multiplayer/ClientLevel;Z)V",
			at = @At("TAIL")
	)
	private void afterClientWorldChange( CallbackInfo ci) {
		ModUtil.itemSuggestions.clear();
		ModUtil.itemSuggestions.addAll(BuiltInRegistries.ITEM.keySet().stream().map(Identifier::toString).toList());
		BuiltInRegistries.ITEM.listTagIds().forEach(tag -> ModUtil.itemSuggestions.add("#" + tag.location()));
	}
}
