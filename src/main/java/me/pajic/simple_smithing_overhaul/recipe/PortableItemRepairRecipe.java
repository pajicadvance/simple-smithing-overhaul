package me.pajic.simple_smithing_overhaul.recipe;

import com.mojang.serialization.MapCodec;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class PortableItemRepairRecipe extends CustomRecipe {

	private static final PortableItemRepairRecipe INSTANCE = new PortableItemRepairRecipe();
	public static final MapCodec<PortableItemRepairRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
	public static final StreamCodec<RegistryFriendlyByteBuf, PortableItemRepairRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private ItemStack itemToRepair;
    private List<ItemStack> repairMaterials;
    private List<ItemStack> repairableItems;
    private int unitCost;
    private int flintCount = 0;
    private RandomSource random;

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        random = level.getRandom();
        List<ItemStack> whetstones = input.items().stream().filter(itemStack -> itemStack.is(ModItems.WHETSTONE)).toList();
        List<ItemStack> flint = input.items().stream().filter(itemStack -> itemStack.is(Items.FLINT)).toList();
        if (whetstones.isEmpty() ^ flint.isEmpty()) {
            if (whetstones.size() == 1) {
                ItemStack whetstone = whetstones.getFirst();
                repairableItems = input.items().stream().filter(itemStack ->
                        itemStack.isDamageableItem() && !itemStack.is(ModItems.WHETSTONE)).toList();
                if (!repairableItems.isEmpty()) {
                    itemToRepair = repairableItems.getFirst();
                    if (itemToRepair.isDamaged() && whetstone.getDamageValue() < whetstone.getMaxDamage()) {
                        ItemEnchantments whetstoneEnchantments = whetstone.getOrDefault(
                                DataComponents.STORED_ENCHANTMENTS,
                                ItemEnchantments.EMPTY
                        );
                        if (itemToRepair.getEnchantments().entrySet().stream().allMatch(
                                entry -> whetstoneEnchantments.getLevel(entry.getKey()) >= Math.min(
                                        entry.getIntValue(),
                                        entry.getKey().value().getMaxLevel()
                                )
                        )) {
                            return processRepair(input);
                        }
                    }
                }
            } else if (flint.size() == 1) {
                flintCount = flint.getFirst().getCount();
                repairableItems = input.items().stream().filter(itemStack ->
                        itemStack.isDamageableItem() && !itemStack.is(Items.FLINT)).toList();
                if (!repairableItems.isEmpty()) {
                    itemToRepair = repairableItems.getFirst();
                    if (itemToRepair.isDamaged() && !itemToRepair.isEnchanted()) {
                        return processRepair(input);
                    }
                }
				flintCount = 0;
            }
        }
        return false;
    }

    @SuppressWarnings("DataFlowIssue")
    private boolean processRepair(CraftingInput input) {
        unitCost = ModUtil.determineUnitCost(itemToRepair);
        int damageRepairedPerUnit = Mth.ceil((float) itemToRepair.getMaxDamage() / unitCost);
        int unitsToMaxRepair = itemToRepair.getDamageValue() / damageRepairedPerUnit;
        repairMaterials = input.items().stream().filter(itemStack -> {
			if (itemToRepair.has(DataComponents.REPAIRABLE)) {
				return itemToRepair.get(DataComponents.REPAIRABLE).isValidRepairItem(itemStack);
			}
			return false;
		}).toList();
        boolean flintMaterialValid = false;
        if (flintCount > 0) flintMaterialValid = repairMaterials.stream().allMatch(i -> {
			for (String s : SSO.CONFIG.portableItemRepair.flintMaterialWhitelist.get()) {
				try {
					if (s.startsWith("#")) {
						if (i.is(TagKey.create(Registries.ITEM, Identifier.tryParse(s.substring(1))))) return true;
					} else {
						Optional<Item> opt = BuiltInRegistries.ITEM.getOptional(Identifier.tryParse(s));
						if (opt.isPresent() && i.is(opt.get())) return true;
					}
				} catch (Throwable t) {
					SSO.LOGGER.warn("Unable to load flint material whitelist entry {}, skipping: {}", s, t.getMessage());
				}
			}
			return false;
		});
		boolean bl = !repairMaterials.isEmpty() && repairMaterials.size() <= unitsToMaxRepair + 1 && (flintCount == 0 || (flintMaterialValid && flintCount >= repairMaterials.size()));
		flintCount = 0;
        return bl;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input) {
        ItemStack outputItem = itemToRepair.copy();
        outputItem.setDamageValue(
                outputItem.getDamageValue() - (Mth.ceil((float) outputItem.getMaxDamage() / unitCost) * repairMaterials.size())
        );
        outputItem.set(ModDataComponents.REPAIR_COUNT, outputItem.getOrDefault(ModDataComponents.REPAIR_COUNT, 0) + 1);
        return outputItem;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull CraftingInput input) {
        List<ItemStack> otherGear = repairableItems.subList(1, repairableItems.size());
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < remainingItems.size(); i++) {
            ItemStack itemStack = input.getItem(i);
            if (itemStack.is(ModItems.WHETSTONE)) {
                float degradationChance = SSO.CONFIG.anvilImprovements.modifyDegradationChance.get() ?
                        SSO.CONFIG.anvilImprovements.degradationChance.get() / 50 : 0.24F;
                for (int j = 0; j < repairMaterials.size(); j ++) if (random.nextFloat() < degradationChance) {
                    itemStack.setDamageValue(itemStack.getDamageValue() + 1);
                }
                remainingItems.set(i, itemStack.copy());
            } else if (itemStack.is(Items.FLINT)) {
                ItemStack updated = new ItemStack(Items.FLINT);
                updated.setCount(itemStack.getCount() - repairMaterials.size());
                itemStack.setCount(0);
                remainingItems.set(i, updated);
            } else if (otherGear.contains(itemStack)) {
                remainingItems.set(i, itemStack.copy());
            } else if (!itemStack.is(repairMaterials.getFirst().getItem()) && !itemStack.is(repairableItems.getFirst().getItem())) {
                remainingItems.set(i, new ItemStack(itemStack.getItem()));
            }
        }
        return remainingItems;
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializers.PORTABLE_ITEM_REPAIR;
    }

	public int getRepairMaterialsSize() {
		return repairMaterials.size();
	}
}
