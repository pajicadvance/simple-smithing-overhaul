package me.pajic.simple_smithing_overhaul.platform.neoforge;

//? neoforge {

/*import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.mixson.ClientResourceModifications;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@EventBusSubscriber(modid = SSO.MOD_ID, value = Dist.CLIENT)
public class NeoforgeClientEventSubscriber {

	@SubscribeEvent
	public static void onClientSetup(final FMLClientSetupEvent event) {
		ClientResourceModifications.init();
		SSO.onInitializeClient();
	}

	@SubscribeEvent
	private static void initClientResources(AddPackFindersEvent event) {
		event.addPackFinders(
				SSO.id("resourcepacks/" + SSO.PACK_VERSION + "/rp"),
				PackType.CLIENT_RESOURCES,
				Component.literal("Mod " + SSO.PACK_VERSION + " Resource Pack"),
				PackSource.BUILT_IN,
				true,
				Pack.Position.TOP
		);
	}
}
*///?}
