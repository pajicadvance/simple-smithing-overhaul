package me.pajic.simple_smithing_overhaul.util;

import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.platform.MultiLoaderUtil;

public class CompatFlags {
    public static final boolean ED_LOADED = MultiLoaderUtil.INSTANCE.isModLoaded("enchantmentdisabler");
    public static final boolean TAX_FREE_LEVELS_LOADED = MultiLoaderUtil.INSTANCE.isModLoaded("taxfreelevels");
    public static final boolean BETTER_TRIDENTS_LOADED = MultiLoaderUtil.INSTANCE.isModLoaded("bettertridents");
	public static final boolean PENCHANT_LOADED = MultiLoaderUtil.INSTANCE.isModLoaded("penchant") && SSO.CONFIG.modIntegration.penchant.get();
}
