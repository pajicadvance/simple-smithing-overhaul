package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilBlock.class)
public abstract class AnvilBlockMixin extends FallingBlock {

    public AnvilBlockMixin(Properties properties) {
        super(properties);
    }

    @Shadow @Final public static EnumProperty<Direction> FACING;

    @ModifyReturnValue(
            method = "damage",
            at = @At("RETURN")
    )
    private static BlockState brokenState(BlockState original, @Local(argsOnly = true, name = "blockState") BlockState blockState) {
        return original == null ? ModBlocks.BROKEN_ANVIL.defaultBlockState().setValue(FACING, blockState.getValue(FACING)) : original;
    }

    @WrapMethod(method = "useWithoutItem")
    private InteractionResult noMenuIfBroken(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, Operation<InteractionResult> original) {
        return state.is(ModBlocks.BROKEN_ANVIL) ? InteractionResult.FAIL : original.call(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(
            ItemStack stack, @NotNull BlockState state, @NotNull  Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult
    ) {
        if (stack.is(Items.IRON_BLOCK) && !state.is(Blocks.ANVIL)) {
            BlockState updatedState;
            if (state.is(ModBlocks.BROKEN_ANVIL)) {
                updatedState = Blocks.DAMAGED_ANVIL.defaultBlockState().setValue(FACING, state.getValue(FACING));
            } else updatedState = state.is(Blocks.DAMAGED_ANVIL) ?
                    Blocks.CHIPPED_ANVIL.defaultBlockState().setValue(FACING, state.getValue(FACING)) :
                    Blocks.ANVIL.defaultBlockState().setValue(FACING, state.getValue(FACING));
            level.setBlock(pos, updatedState, 2);
            level.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
            stack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
