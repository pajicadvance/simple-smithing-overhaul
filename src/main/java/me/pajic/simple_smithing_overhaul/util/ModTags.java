package me.pajic.simple_smithing_overhaul.util;

import me.pajic.simple_smithing_overhaul.SSO;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
	public static final TagKey<Item> REPAIRS_NETHERITE_EQUIPMENT = TagKey.create(Registries.ITEM, SSO.id("repairs_netherite_equipment"));
}
