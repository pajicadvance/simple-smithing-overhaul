package me.pajic.simple_smithing_overhaul.platform.fabric;

//? fabric {

//~ if <26.1 'ClientLevelEvents' -> 'ClientWorldEvents' {
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.assets.ModAssetPacks;
import me.pajic.simple_smithing_overhaul.config.ItemSuggestions;
import me.pajic.simple_smithing_overhaul.repair.RepairableOverrides;
import me.pajic.simple_smithing_overhaul.repair.RepairableSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

//? <26.1 {
/*import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
*///?} else {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
//?}

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SSO.onInitializeClient();
        FabricLoader.getInstance().getModContainer(SSO.MOD_ID).ifPresent(container ->
                //~ if <26.1 'ResourceLoader.registerBuiltinPack' -> 'ResourceManagerHelper.registerBuiltinResourcePack'
                ModAssetPacks.getPacks().forEach(s -> ResourceLoader.registerBuiltinPack(
                        SSO.id(s),
                        container,
                        Component.translatable("pack.simple_smithing_overhaul." + s),
                        //~ if <26.1 'PackActivationType' -> 'ResourcePackActivationType'
                        PackActivationType.ALWAYS_ENABLED
                ))
        );
        ClientPlayNetworking.registerGlobalReceiver(
                RepairableSyncPayload.TYPE,
                (payload, c) -> RepairableOverrides.set(payload.repairables())
        );
        //~ if <26.1 'AFTER_CLIENT_LEVEL_CHANGE' -> 'AFTER_CLIENT_WORLD_CHANGE'
        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((c, level) -> ItemSuggestions.update(level));
        ClientPlayConnectionEvents.DISCONNECT.register((l, c) -> RepairableOverrides.clear());
	}
}
//~}
//?}
