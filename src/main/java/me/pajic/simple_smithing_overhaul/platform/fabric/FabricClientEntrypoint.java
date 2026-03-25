package me.pajic.simple_smithing_overhaul.platform.fabric;

//? fabric {

import me.pajic.simple_smithing_overhaul.SSO;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SSO.onInitializeClient();
	}
}
//?}
