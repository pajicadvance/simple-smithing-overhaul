package me.pajic.simple_smithing_overhaul.datapacks;

import me.pajic.simple_smithing_overhaul.Main;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;

public class ChalkItemTags {
    public static void init() {
        FabricLoader.getInstance().getModContainer(Main.MOD_ID).ifPresent(modContainer -> {
            String path = "chalk_item_tags";
            //? if >= 1.21.4
            /*path = path.concat("_new");*/
            ResourceManagerHelper.registerBuiltinResourcePack(
                    Main.withModNamespace(path),
                    modContainer,
                    ResourcePackActivationType.ALWAYS_ENABLED
            );
        });
    }
}