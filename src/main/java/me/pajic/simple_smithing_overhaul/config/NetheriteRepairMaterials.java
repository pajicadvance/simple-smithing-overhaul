package me.pajic.simple_smithing_overhaul.config;

import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import org.jetbrains.annotations.NotNull;

public enum NetheriteRepairMaterials implements EnumTranslatable {
    NETHERITE_INGOT, NETHERITE_SCRAP, DIAMOND;

    @Override
    @NotNull public String prefix() {
        return "simple_smithing_overhaul.netheriteRepairMaterials";
    }
}
