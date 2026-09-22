package me.pajic.simple_smithing_overhaul.backport;

import net.minecraft.world.item.ItemStack;

public record ItemStackWithSlot(int slot, ItemStack stack) {}
