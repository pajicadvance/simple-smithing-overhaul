package me.pajic.simple_smithing_overhaul;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.simple_smithing_overhaul.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class Main implements ModInitializer {
    public static final String MOD_ID = "simple_smithing_overhaul";
    public static final ResourceLocation CONFIG_RL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "config");
    public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);

    @Override
    public void onInitialize() {
        Initializer.init();
    }
}
