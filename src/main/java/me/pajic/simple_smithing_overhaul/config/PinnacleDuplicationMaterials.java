package me.pajic.simple_smithing_overhaul.config;

import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import org.jetbrains.annotations.NotNull;

public enum PinnacleDuplicationMaterials implements EnumTranslatable {
	SCULK_CATALYST, SCULK, NETHER_STAR;

	@Override
	@NotNull public String prefix() {
		return "simple_smithing_overhaul.duplicationMaterials";
	}
}
