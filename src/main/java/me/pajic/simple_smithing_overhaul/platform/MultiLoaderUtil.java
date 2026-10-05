package me.pajic.simple_smithing_overhaul.platform;

//$ loader_util_import
import me.pajic.simple_smithing_overhaul.platform.fabric.FabricLoaderUtil;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface MultiLoaderUtil {
    MultiLoaderUtil INSTANCE = /*$ loader_util_inst*/ new FabricLoaderUtil();

    boolean isModLoaded(String modId);
    boolean isDevEnv();
    void s2c(ServerPlayer player, CustomPacketPayload payload);
}
