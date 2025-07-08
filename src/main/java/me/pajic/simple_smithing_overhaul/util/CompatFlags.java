package me.pajic.simple_smithing_overhaul.util;

import net.fabricmc.loader.api.FabricLoader;

public class CompatFlags {
    public static final boolean ED_LOADED = FabricLoader.getInstance().isModLoaded("enchantmentdisabler");
    public static final boolean TAX_FREE_LEVELS_LOADED = FabricLoader.getInstance().isModLoaded("taxfreelevels");
    public static final boolean CHALK_LOADED = FabricLoader.getInstance().isModLoaded("chalk");
    public static final boolean BETTER_TRIDENTS_LOADED = FabricLoader.getInstance().isModLoaded("bettertridents");
}
