package me.pajic.simple_smithing_overhaul.config;

import me.fzzyhmstrs.fzzy_config.util.EnumTranslatable;
import org.jetbrains.annotations.NotNull;

public enum DestructionPreventMode implements EnumTranslatable {
    ALL, ENCHANTED, NONE;

    @Override
    @NotNull public String prefix() {
        return "simple_smithing_overhaul.destructionPreventMode";
    }
}
