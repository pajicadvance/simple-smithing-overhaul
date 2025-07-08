package me.pajic.simple_smithing_overhaul.datapacks;

import me.pajic.simple_smithing_overhaul.Main;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

public class ChalkItemTags {
    public static void init() {
        FabricLoader.getInstance().getModContainer(Main.MOD_ID).ifPresent(modContainer ->
                ResourceManagerHelper.registerBuiltinResourcePack(
                        ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "chalk_item_tags"),
                        modContainer,
                        ResourcePackActivationType.ALWAYS_ENABLED
                )
        );
    }
}