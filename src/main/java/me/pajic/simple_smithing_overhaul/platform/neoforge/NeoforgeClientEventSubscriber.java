package me.pajic.simple_smithing_overhaul.platform.neoforge;

//? neoforge {

/*import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.assets.ModAssetPacks;
import me.pajic.simple_smithing_overhaul.repair.RepairableOverrides;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

//? >=26.1 {
import me.pajic.simple_smithing_overhaul.repair.RepairableSyncPayload;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
//?}

@EventBusSubscriber(modid = SSO.MOD_ID, value = Dist.CLIENT)
public class NeoforgeClientEventSubscriber {

	@SubscribeEvent
	public static void onClientSetup(final FMLCommonSetupEvent event) {
        SSO.onInitializeClient();
	}

    @SubscribeEvent
    private static void initPacks(AddPackFindersEvent event) {
        ModAssetPacks.getPacks().forEach(s -> event.addPackFinders(
                SSO.id("resourcepacks/" + s),
                PackType.CLIENT_RESOURCES,
                Component.translatable("pack.simple_smithing_overhaul." + s),
                PackSource.BUILT_IN,
                true,
                Pack.Position.TOP
        ));
    }

    //? >=26.1 {
    @SubscribeEvent
    private static void initNetworking(RegisterClientPayloadHandlersEvent event) {
        event.register(RepairableSyncPayload.TYPE, (payload, c) ->
                RepairableOverrides.set(payload.repairables())
        );
    }
    //?}

    @SubscribeEvent
    private static void onDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        RepairableOverrides.clear();
    }
}
*///?}
