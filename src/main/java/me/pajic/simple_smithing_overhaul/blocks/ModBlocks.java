package me.pajic.simple_smithing_overhaul.blocks;

import me.pajic.simple_smithing_overhaul.Main;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
//? if > 1.21.1 {
/*import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
*///?}

public class ModBlocks {

    public static final Block BROKEN_ANVIL = Registry.register(
            BuiltInRegistries.BLOCK,
            Main.withModNamespace("broken_anvil"),
            new AnvilBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DAMAGED_ANVIL)
                    //? if > 1.21.1
                    /*.setId(ResourceKey.create(Registries.BLOCK, Main.withModNamespace("broken_anvil")))*/
            )
    );

    public static void init() {}
}
