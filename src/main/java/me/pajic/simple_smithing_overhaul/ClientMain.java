package me.pajic.simple_smithing_overhaul;

import me.pajic.simple_smithing_overhaul.config.ModClientConfig;
import me.pajic.simple_smithing_overhaul.mixson.ResourceModifications;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = "simple_smithing_overhaul", dist = Dist.CLIENT)
public class ClientMain {
    public ClientMain(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ModClientConfig.CLIENT_SPEC);
        modEventBus.addListener(this::onInitialize);
    }

    public void onInitialize(FMLCommonSetupEvent event) {
        ResourceModifications.clientInit();
    }
}
