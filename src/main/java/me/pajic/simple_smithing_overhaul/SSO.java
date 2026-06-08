package me.pajic.simple_smithing_overhaul;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.simple_smithing_overhaul.config.ModConfig;
import me.pajic.simple_smithing_overhaul.defaulted.RepairablePatchEvent;
import me.pajic.simple_smithing_overhaul.mixson.AssetPatches;
import me.pajic.simple_smithing_overhaul.mixson.DataPatches;
import me.pajic.simple_smithing_overhaul.mixson.MixsonHelper;
import me.pajic.simple_smithing_overhaul.platform.Platform;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? fabric {
import me.pajic.simple_smithing_overhaul.platform.fabric.FabricPlatform;
//?} neoforge {
/*import me.pajic.simple_smithing_overhaul.platform.neoforge.NeoforgePlatform;
 *///?}

@SuppressWarnings("LoggingSimilarMessage")
public class SSO {

	public static final String MOD_ID = /*$ mod_id*/ "simple_smithing_overhaul";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Platform PLATFORM = createPlatformInstance();
	public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);

	public static void onInitialize() {
		MixsonHelper.setDebugFlags();
		DataPatches.init();
		RepairablePatchEvent.register();
	}

	public static void onInitializeClient() {
		AssetPatches.init();
	}

	public static Platform xplat() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new FabricPlatform();
		//?} neoforge {
		/*return new NeoforgePlatform();
		 *///?}
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void debugLog(String message, Object ... args) {
		if (PLATFORM.isDebug()) LOGGER.info(message, args);
	}
}
