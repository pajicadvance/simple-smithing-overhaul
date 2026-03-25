package me.pajic.simple_smithing_overhaul.blocks;

import me.pajic.simple_smithing_overhaul.SSO;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {

    public static final Block BROKEN_ANVIL = new AnvilBlock(
			BlockBehaviour.Properties.ofFullCopy(Blocks.DAMAGED_ANVIL)
			.setId(ResourceKey.create(Registries.BLOCK, SSO.id("broken_anvil")))
	);

    public static void init() {}
}
