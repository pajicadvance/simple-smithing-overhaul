package me.pajic.simple_smithing_overhaul;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.simple_smithing_overhaul.config.ModConfig;
import me.pajic.simple_smithing_overhaul.defaulted.RepairablePatchEvent;
import me.pajic.simple_smithing_overhaul.mixson.AssetPatches;
import me.pajic.simple_smithing_overhaul.mixson.DataPatches;
import me.pajic.simple_smithing_overhaul.mixson.MixsonHelper;
import me.pajic.simple_smithing_overhaul.platform.MultiLoaderUtil;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSO {

    public static final String MOD_ID = /*$ mod_id*/ "simple_smithing_overhaul";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);

    public static void onInitialize() {
        MixsonHelper.setDebugFlags();
        DataPatches.init();
        RepairablePatchEvent.register();
        ModUtil.initItemProperties();
    }

    public static void onInitializeClient() {
        AssetPatches.init();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void debugLog(String message, Object ... args) {
        if (MultiLoaderUtil.INSTANCE.isDevEnv()) LOGGER.info(message, args);
    }
}
