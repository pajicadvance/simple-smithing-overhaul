package me.pajic.simple_smithing_overhaul.datapacks;

import me.pajic.simple_smithing_overhaul.config.ModCommonConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

public class NetheriteRepairMaterial {

    @SubscribeEvent
    public static void registerDatapacks(AddPackFindersEvent event) {
        switch (ModCommonConfig.netheriteRepairMaterial) {
            case DIAMOND -> event.addPackFinders(
                    ResourceLocation.fromNamespaceAndPath("simple_smithing_overhaul", "netherite_repair_diamond"),
                    PackType.SERVER_DATA,
                    Component.literal("Netherite Repair Material Diamond"),
                    PackSource.BUILT_IN,
                    true,
                    Pack.Position.TOP
            );
            case NETHERITE_SCRAP -> event.addPackFinders(
                    ResourceLocation.fromNamespaceAndPath("simple_smithing_overhaul", "netherite_repair_scrap"),
                    PackType.SERVER_DATA,
                    Component.literal("Netherite Repair Material Scrap"),
                    PackSource.BUILT_IN,
                    true,
                    Pack.Position.TOP
            );
        }
    }
}
