package me.pajic.simple_smithing_overhaul.event;

import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
//? if <= 1.21.1
import net.minecraft.world.InteractionResultHolder;

public class ModEvents {
    public static void init() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) ->
                ModUtil.isBroken(player.getItemInHand(hand)) ? InteractionResult.FAIL : InteractionResult.PASS
        );
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) ->
                ModUtil.isBroken(player.getItemInHand(hand)) ? InteractionResult.FAIL : InteractionResult.PASS
        );
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) ->
                ModUtil.isBroken(player.getItemInHand(hand)) ? InteractionResult.FAIL : InteractionResult.PASS
        );
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) ->
                ModUtil.isBroken(player.getItemInHand(hand)) ? InteractionResult.FAIL : InteractionResult.PASS
        );
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getMainHandItem();
            //? if <= 1.21.1
            return ModUtil.isBroken(stack) ? InteractionResultHolder.fail(stack) : InteractionResultHolder.pass(stack);
            //? if > 1.21.1
            /*return ModUtil.isBroken(stack) ? InteractionResult.FAIL : InteractionResult.PASS;*/
        });
    }
}
