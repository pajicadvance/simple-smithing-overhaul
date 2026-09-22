package me.pajic.simple_smithing_overhaul.platform;

//$ loader_util_import
import me.pajic.simple_smithing_overhaul.platform.fabric.FabricLoaderUtil;

public interface MultiLoaderUtil {
    MultiLoaderUtil INSTANCE = /*$ loader_util_inst*/ new FabricLoaderUtil();

    boolean isModLoaded(String modId);
    boolean isDevEnv();
}
