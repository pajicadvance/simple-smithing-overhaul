package me.pajic.simple_smithing_overhaul.util;

import me.pajic.simple_smithing_overhaul.SSO;

public class CompatFlags {
    public static final boolean ED_LOADED = SSO.xplat().isModLoaded("enchantmentdisabler");
    public static final boolean TAX_FREE_LEVELS_LOADED = SSO.xplat().isModLoaded("taxfreelevels");
    public static final boolean BETTER_TRIDENTS_LOADED = SSO.xplat().isModLoaded("bettertridents");
	public static final boolean PENCHANT_LOADED = SSO.xplat().isModLoaded("penchant") && SSO.CONFIG.modIntegration.penchant.get();
    public static final boolean ENCHIRIDION_LOADED = SSO.xplat().isModLoaded("enchiridion");
}
