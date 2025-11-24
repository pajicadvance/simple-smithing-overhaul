package me.pajic.simple_smithing_overhaul;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.simple_smithing_overhaul.config.ModConfig;
import me.pajic.simple_smithing_overhaul.platform.Platform;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.resources.ResourceLocation;
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
	public static final String MOD_VERSION = /*$ mod_version*/ "2.5.7";
	public static final String MOD_FRIENDLY_NAME = /*$ mod_name*/ "Simple Smithing Overhaul";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ResourceLocation CONFIG_RL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "config");
	public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);
	private static final Platform PLATFORM = createPlatformInstance();
	public static final String PACK_VERSION = PLATFORM.mcVersion().replace(".", "_");

	public static void onInitialize() {
	}

	public static void onInitializeClient() {
		ModUtil.initItemProperties();
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

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void debugLog(String message, Object ... args) {
		if (PLATFORM.isDebug()) LOGGER.info(message, args);
	}
}
