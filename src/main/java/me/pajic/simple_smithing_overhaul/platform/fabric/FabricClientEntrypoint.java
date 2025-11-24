package me.pajic.simple_smithing_overhaul.platform.fabric;

//? fabric {

import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.mixson.ClientResourceModifications;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;

@SuppressWarnings("unused")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientResourceModifications.init();
		SSO.onInitializeClient();
		initConditionalClientResources();
	}

	private void initConditionalClientResources() {
		FabricLoader.getInstance().getModContainer(SSO.MOD_ID).ifPresent(modContainer ->
				ResourceManagerHelper.registerBuiltinResourcePack(
						SSO.id(SSO.PACK_VERSION + "/rp"),
						modContainer,
						ResourcePackActivationType.ALWAYS_ENABLED
				)
		);
	}
}
//?}
