package me.pajic.simple_smithing_overhaul.event;

import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
//? if <= 1.21.1
import net.minecraft.world.InteractionResultHolder;

public class ModEvents {

    public static void init() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> canUse(player, hand));
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> canUse(player, hand));
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> canUse(player, hand));
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> canUse(player, hand));
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getMainHandItem();
            //? if <= 1.21.1
            return ModUtil.isBroken(stack) ? InteractionResultHolder.fail(stack) : InteractionResultHolder.pass(stack);
            //? if > 1.21.1
            /*return ModUtil.isBroken(stack) ? InteractionResult.FAIL : InteractionResult.PASS;*/
        });
    }

    private static InteractionResult canUse(Player player, InteractionHand hand) {
        return ModUtil.isBroken(player.getItemInHand(hand)) ? InteractionResult.FAIL : InteractionResult.PASS;
    }
}
