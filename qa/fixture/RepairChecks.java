package sso.qa;

import java.util.List;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.recipe.PortableItemRepairRecipe;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.enchantment.Repairable;

/** Test-only entrypoint. Never distribute this fixture with the mod. */
public final class RepairChecks implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> check(server, "startup"));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resources, success) -> {
            require(success, "datapack reload succeeded");
            check(server, "reload");
        });
    }

    private static void check(MinecraftServer server, String phase) {
        repairable(Items.BOW, Items.STRING, Items.DIAMOND);
        repairable(Items.CROSSBOW, Items.STRING, Items.DIAMOND);
        repairable(Items.FLINT_AND_STEEL, Items.IRON_INGOT, Items.STRING);
        repairable(Items.SHEARS, Items.IRON_INGOT, Items.STRING);
        repairable(Items.BRUSH, Items.FEATHER, Items.STRING);
        repairable(Items.CARROT_ON_A_STICK, Items.CARROT, Items.STRING);
        repairable(Items.WARPED_FUNGUS_ON_A_STICK, Items.WARPED_FUNGUS, Items.STRING);
        repairable(Items.TRIDENT, Items.PRISMARINE_SHARD, Items.STRING);
        if (Boolean.getBoolean("sso.qa.overrides")) {
            require("minecraft:stick".equals(SSO.CONFIG.streamlinedRepairs.modRepairableItems.get()
                .get("minecraft:fishing_rod")), "configured item override was loaded");
            repairable(Items.FISHING_ROD, Items.STICK, Items.STRING);
            repairable(Items.IRON_PICKAXE, Items.OAK_PLANKS, Items.IRON_INGOT);
            repairable(Items.DIAMOND_PICKAXE, Items.BIRCH_PLANKS, Items.DIAMOND);
        } else {
            repairable(Items.FISHING_ROD, Items.STRING, Items.DIAMOND);
        }

        ItemStack bow = new ItemStack(Items.BOW);
        int damage = bow.getMaxDamage() * 2 / 3;
        bow.setDamageValue(damage);
        CraftingInput input = CraftingInput.of(3, 1,
            List.of(bow, new ItemStack(Items.STRING), new ItemStack(Items.FLINT)));
        PortableItemRepairRecipe recipe = new PortableItemRepairRecipe();
        require(recipe.matches(input, server.overworld()), "bow/string/flint matches portable repair");
        ItemStack result = recipe.assemble(input);
        int repaired = (int) Math.ceil((float) bow.getMaxDamage() / ModUtil.determineUnitCost(bow));
        require(result.is(Items.BOW), "portable result is a bow");
        require(result.getDamageValue() == Math.max(0, damage - repaired), "portable repair amount");
        require(result.getOrDefault(ModDataComponents.REPAIR_COUNT, 0) == 1, "repair count incremented");
        require(bow.getDamageValue() == damage, "assembling preserves input damage");
        var remaining = recipe.getRemainingItems(input);
        require(remaining.stream().allMatch(ItemStack::isEmpty), "one string and flint consumed");
        CraftingInput wrong = CraftingInput.of(3, 1,
            List.of(bow, new ItemStack(Items.DIAMOND), new ItemStack(Items.FLINT)));
        require(!recipe.matches(wrong, server.overworld()), "wrong repair material rejected");
        System.out.println("SSO_QA_REPAIR_PASS " + phase);
    }

    private static void repairable(Item item, Item accepted, Item rejected) {
        ItemStack stack = new ItemStack(item);
        Repairable repairable = stack.get(DataComponents.REPAIRABLE);
        require(repairable != null, item + " repairable component exists");
        require(repairable.isValidRepairItem(new ItemStack(accepted)), item + " accepts " + accepted);
        require(!repairable.isValidRepairItem(new ItemStack(rejected)), item + " rejects " + rejected);
        require(ModUtil.isValidRepairItem(stack, new ItemStack(accepted)), item + " mod repair integration");
    }

    private static void require(boolean condition, String description) {
        if (!condition) throw new AssertionError("SSO_QA_REPAIR_FAIL: " + description);
    }
}
