package me.pajic.simple_smithing_overhaul.datapacks;

import me.pajic.simple_smithing_overhaul.Main;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

public class NetheriteRepairMaterial {

    public static void init() {
        FabricLoader.getInstance().getModContainer("simple_smithing_overhaul").ifPresent(modContainer -> {
            switch (Main.CONFIG.streamlinedRepairs.netheriteRepairMaterial()) {
                case DIAMOND -> ResourceManagerHelper.registerBuiltinResourcePack(
                        ResourceLocation.parse("simple_smithing_overhaul:netherite_repair_diamond"),
                        modContainer,
                        ResourcePackActivationType.ALWAYS_ENABLED
                );
                case NETHERITE_SCRAP -> ResourceManagerHelper.registerBuiltinResourcePack(
                        ResourceLocation.parse("simple_smithing_overhaul:netherite_repair_scrap"),
                        modContainer,
                        ResourcePackActivationType.ALWAYS_ENABLED
                );
            }
        });
    }
}
