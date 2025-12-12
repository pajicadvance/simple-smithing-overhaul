package me.pajic.simple_smithing_overhaul.compat;

//? if 1.21.1 {

/*import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.recipe.EmiAnvilRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;
//? if neoforge
//import dev.emi.emi.api.EmiEntrypoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

// EMI compatibility plugin for Simple Smithing Overhaul

//? if neoforge
//@EmiEntrypoint
public class EmiCompat implements EmiPlugin {

    private static final EmiRecipeCategory PORTABLE_REPAIR = new EmiRecipeCategory(
            SSO.id("portable_repair"),
            EmiStack.of(ModItems.WHETSTONE)
    );

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register(EmiRegistry emiRegistry) {
        // Add anvil repair recipe for whetstones
        emiRegistry.addCategory(PORTABLE_REPAIR);

        emiRegistry.addRecipe(new EmiAnvilRecipe(
                EmiStack.of(ModItems.WHETSTONE),
                EmiStack.of(Items.QUARTZ),
                EmiPort.id(
                        "emi",
                        "/" + "anvil/repairing/material" +
                                "/" + EmiUtil.subId(ModItems.WHETSTONE) +
                                "/" + EmiUtil.subId(Items.QUARTZ)
                )
        ));

        // Add anvil repair recipes for every item made repairable by the mod
        final int[] counter = {1};
        ModUtil.additionalRepairables.forEach((key, value) -> {
            for (ItemStack stack : key.getItems()) {
                emiRegistry.addRecipe(new EmiAnvilRecipe(
                        EmiStack.of(stack),
                        EmiIngredient.of(value),
                        EmiPort.id(
                                "emi",
                                "/" + "anvil/repairing/material" +
                                        "/" + EmiUtil.subId(stack.getItem()) +
                                        "/" + counter[0]
                        )
                ));
                counter[0]++;
            }
        });

        // Hide the "base" enchantment upgrading smithing recipe.
        // The behavior of this recipe is completely changed in the mod code so it's irrelevant.
        emiRegistry.removeRecipes(SSO.id("enchantment_upgrade_smithing"));
        // Same for pinnacle enchantment smithing recipe
        emiRegistry.removeRecipes(SSO.id("pinnacle_enchantment_smithing"));

        // Add enchantment upgrade smithing recipes for every enchantable item.
        // See the enchantment_upgradable tag for the list of included items.
        EmiIngredient input = EmiIngredient.of(Ingredient.of(TagKey.create(
                Registries.ITEM,
                SSO.id("enchantment_upgradeable")
        )));
        counter[0] = 1;
        input.getEmiStacks().forEach(emiStack -> {
            emiRegistry.addRecipe(new EmiEnchantmentUpgradeSmithingRecipe(
                    SSO.id("/enchantment_upgrade_" + counter[0]),
                    emiStack.getItemStack()
            ));
            counter[0]++;
        });
        // Same for pinnacle enchantment smithing recipes
        EmiIngredient input1 = EmiIngredient.of(Ingredient.of(TagKey.create(
                Registries.ITEM,
                ResourceLocation.withDefaultNamespace("enchantable/durability")
        )));
        counter[0] = 1;
        input1.getEmiStacks().forEach(emiStack -> {
            emiRegistry.addRecipe(new EmiPinnacleEnchantmentSmithingRecipe(
                    SSO.id("/pinnacle_enchantment_" + counter[0]),
                    emiStack.getItemStack()
            ));
            counter[0]++;
        });

        // Add whetstone repair crafting recipes for every repairable item
        counter[0] = 1;
        for (Item item : EmiPort.getItemRegistry()) {
            // Copypasta from EMI's VanillaPlugin
            if (item instanceof ArmorItem ai) {
                if (ai.getMaterial() != null && ai.getMaterial().value().repairIngredient().get() != null && !ai.getMaterial().value().repairIngredient().get().isEmpty()) {
                    addWhetstoneRepairRecipe(emiRegistry, ai, ai.getMaterial().value().repairIngredient().get(), counter[0]);
                    counter[0]++;
                }
            } else if (item instanceof TieredItem ti) {
                if (ti.getTier().getRepairIngredient() != null && !ti.getTier().getRepairIngredient().isEmpty()) {
                    addWhetstoneRepairRecipe(emiRegistry, ti, ti.getTier().getRepairIngredient(), counter[0]);
                    counter[0]++;
                }
            // Mod repairables
            } else {
                ModUtil.additionalRepairables.forEach((key, value) -> {
                    for (ItemStack stack : key.getItems()) {
                        if (item.equals(stack.getItem())) {
                            addWhetstoneRepairRecipe(emiRegistry, stack.getItem(), value, counter[0]);
                            counter[0]++;
                        }
                    }
                });
            }
        }
    }

    private void addWhetstoneRepairRecipe(EmiRegistry emiRegistry, Item item, Ingredient repairIngredient, int counter) {
        emiRegistry.addRecipe(new EmiPortableRepairRecipe(
                SSO.id("/portable_repair_" + counter),
                EmiStack.of(item),
                EmiIngredient.of(repairIngredient)
        ));
    }

    private static class EmiPortableRepairRecipe implements EmiRecipe {
        protected final ResourceLocation id;
        protected final EmiStack input;
        protected final EmiStack whetstone;
        protected final EmiIngredient repairIngredient;
        private final int uniq1;
        private final int uniq2;
        private boolean enchanted;

        private EmiPortableRepairRecipe(ResourceLocation id, EmiStack input, EmiIngredient repairIngredient) {
            this.id = id;
            this.input = input;
            this.whetstone = EmiStack.of(ModItems.WHETSTONE);
            this.repairIngredient = repairIngredient;
            this.uniq1 = EmiUtil.RANDOM.nextInt();
            this.uniq2 = EmiUtil.RANDOM.nextInt();
            this.enchanted = false;
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return PORTABLE_REPAIR;
        }

        @Override
        public @Nullable ResourceLocation getId() {
            return this.id;
        }

        @Override
        public List<EmiIngredient> getInputs() {
            return List.of(this.input, this.whetstone, this.repairIngredient);
        }

        @Override
        public List<EmiStack> getOutputs() {
            return List.of(this.input);
        }

        @Override
        public int getDisplayWidth() {
            return 118;
        }

        @Override
        public int getDisplayHeight() {
            return 54;
        }

        @Override
        public void addWidgets(WidgetHolder widgetHolder) {
            widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 60, 18);
            widgetHolder.addTexture(EmiTexture.SHAPELESS, 97, 0);

            List<EnchantmentInstance> enchantmentInstances;
            Set<Holder<Enchantment>> enchantmentSet = new HashSet<>();
            EmiPort.getEnchantmentRegistry().holders().forEach(e -> {
                if (e.value().canEnchant(this.input.getItemStack()) && ModUtil.enchantmentEligible(e)) enchantmentSet.add(e);
            });
            enchantmentInstances = EnchantmentHelper.selectEnchantment(RandomSource.create(), input.getItemStack(), 30, enchantmentSet.stream());

            widgetHolder.addGeneratedSlot(r -> this.getInput(r, false, enchantmentInstances), this.uniq1, 0, 0);
            widgetHolder.addGeneratedSlot(r -> this.getWhetstone(r, enchantmentInstances), this.uniq2, 18, 0);
            widgetHolder.addSlot(this.repairIngredient, 36 ,0);
            for(int i = 3; i < 9; ++i) {
                widgetHolder.addSlot(EmiStack.of(ItemStack.EMPTY), i % 3 * 18, i / 3 * 18);
            }

            widgetHolder.addGeneratedSlot(r -> this.getInput(r, true, enchantmentInstances), this.uniq1, 92, 14).large(true).recipeContext(this);
        }

        private EmiStack getInput(Random r, boolean repaired, List<EnchantmentInstance> enchantments) {
            enchanted = r.nextBoolean();
            ItemStack stack = this.input.getItemStack().copy();
            if (!enchantments.isEmpty() && enchanted) {
                enchantments.forEach(enchantment -> stack.enchant(enchantment.enchantment, enchantment.level));
            }
            if (stack.getMaxDamage() > 0) {
                int d = r.nextInt(stack.getMaxDamage());
                if (repaired) {
                    d -= Math.round((float) stack.getMaxDamage() / ModUtil.determineUnitCost(stack));
                    if (d <= 0) {
                        return EmiStack.of(stack);
                    }
                }
                stack.setDamageValue(d);
            }
            return EmiStack.of(stack);
        }

        private EmiIngredient getWhetstone(Random r, List<EnchantmentInstance> enchantments) {
            ItemStack stack = this.whetstone.getItemStack().copy();
            if (!enchantments.isEmpty() && enchanted) {
                enchantments.forEach(enchantment -> stack.enchant(enchantment.enchantment, enchantment.level));
            }
            if (stack.getMaxDamage() > 0) {
                int d = r.nextInt(stack.getMaxDamage());
                stack.setDamageValue(d);
            }
            return enchanted ? EmiStack.of(stack) : EmiIngredient.of(List.of(EmiStack.of(stack), EmiStack.of(Items.FLINT)));
        }
    }

    private static class EmiEnchantmentUpgradeSmithingRecipe implements EmiRecipe {
        protected final ResourceLocation id;
        protected final EmiStack template;
        protected final EmiStack input;
        protected final EmiIngredient addition;
        private final int uniq;

        private EmiEnchantmentUpgradeSmithingRecipe(ResourceLocation id, ItemStack input) {
            this.id = id;
            this.template = EmiStack.of(ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE);
            this.input = EmiStack.of(input);
            this.addition = EmiStack.of(Items.LAPIS_LAZULI);
            this.uniq = EmiUtil.RANDOM.nextInt();
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return VanillaEmiRecipeCategories.SMITHING;
        }

        @Override
        public @Nullable ResourceLocation getId() {
            return this.id;
        }

        @Override
        public List<EmiIngredient> getInputs() {
            return List.of(this.template, this.input, this.addition);
        }

        @Override
        public List<EmiStack> getOutputs() {
            return List.of(this.input);
        }

        @Override
        public int getDisplayWidth() {
            return 112;
        }

        @Override
        public int getDisplayHeight() {
            return 18;
        }

        @Override
        public void addWidgets(WidgetHolder widgetHolder) {
            widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 62, 1);

            boolean fallback = false;
            ItemStack inputStack = this.input.getItemStack().copy();
            List<EnchantmentInstance> enchantmentInstances;
            Set<Holder<Enchantment>> enchantmentSet = new HashSet<>();
            EmiPort.getEnchantmentRegistry().holders().forEach(e -> {
                if (((inputStack.is(Items.ENCHANTED_BOOK) || inputStack.is(ModItems.WHETSTONE)) || e.value().canEnchant(inputStack)) && ModUtil.enchantmentEligible(e)) enchantmentSet.add(e);
            });
            enchantmentInstances = EnchantmentHelper.selectEnchantment(RandomSource.create(), inputStack, 30, enchantmentSet.stream());
            if (enchantmentInstances.isEmpty()) {
                enchantmentInstances = EnchantmentHelper.selectEnchantment(RandomSource.create(), new ItemStack(Items.BOOK), 30, enchantmentSet.stream());
                fallback = true;
            }
            if (!enchantmentInstances.isEmpty()) {
                if (fallback){
                    ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
                    if (inputStack.is(Items.ENCHANTED_BOOK) || inputStack.is(ModItems.WHETSTONE)) {
                        enchantmentInstances.forEach(enchantment -> {
                            int maxLevel = enchantment.enchantment.value().getMaxLevel();
                            if (maxLevel > 1) {
                                if (enchantment.level == maxLevel)
                                    enchantments.set(enchantment.enchantment, enchantment.level - 1);
                                else
                                    enchantments.set(enchantment.enchantment, enchantment.level);
                            }
                        });
                        if (enchantments.toImmutable().isEmpty()) {
                            enchantments.set(EmiPort.getEnchantmentRegistry().getHolderOrThrow(Enchantments.UNBREAKING), 2);
                            inputStack.set(DataComponents.STORED_ENCHANTMENTS, enchantments.toImmutable());
                        }
                        inputStack.set(DataComponents.STORED_ENCHANTMENTS, enchantments.toImmutable());
                    } else {
                        enchantments.set(EmiPort.getEnchantmentRegistry().getHolderOrThrow(Enchantments.UNBREAKING), 2);
                        inputStack.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable());
                    }
                } else {
                    enchantmentInstances.forEach(enchantment -> {
                        int maxLevel = enchantment.enchantment.value().getMaxLevel();
                        if (maxLevel > 1) {
                            if (enchantment.level == maxLevel)
                                inputStack.enchant(enchantment.enchantment, enchantment.level - 1);
                            else
                                inputStack.enchant(enchantment.enchantment, enchantment.level);
                        }
                    });
                    if (!inputStack.isEnchanted()) {
                        inputStack.enchant(EmiPort.getEnchantmentRegistry().getHolderOrThrow(Enchantments.UNBREAKING), 2);
                    }
                }
            }

            ItemEnchantments enchantments;
            if (inputStack.is(Items.ENCHANTED_BOOK) || inputStack.is(ModItems.WHETSTONE)) {
                enchantments = inputStack.get(DataComponents.STORED_ENCHANTMENTS);
            } else {
                enchantments = inputStack.get(DataComponents.ENCHANTMENTS);
            }
            ItemEnchantments finalEnchantments = enchantments;
            if (finalEnchantments != null) {
                int count = EmiUtil.RANDOM.nextInt(finalEnchantments.size()) + 1;
                this.addition.setAmount(count);
                widgetHolder.addSlot(this.template, 0, 0);
                widgetHolder.addSlot(EmiStack.of(inputStack), 18, 0);
                widgetHolder.addSlot(this.addition, 36, 0);
                widgetHolder.addGeneratedSlot(r -> this.getOutput(inputStack, finalEnchantments, count), this.uniq, 94, 0).recipeContext(this);
            }
        }

        private EmiStack getOutput(ItemStack stack, ItemEnchantments enchantments, int index) {
            Minecraft mc = Minecraft.getInstance();
            List<Component> enchantmentNames = new ArrayList<>();
            Consumer<Component> consumer = enchantmentNames::add;
            enchantments.addToTooltip(Item.TooltipContext.of(mc.level), consumer, TooltipFlag.NORMAL);
            for (int i = 0; i < enchantmentNames.size(); i++) {
                if (i + 1 == index) {
                    Component enchantmentName = enchantmentNames.get(i);
                    for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
                        if (Enchantment.getFullname(entry.getKey(), entry.getIntValue()).equals(enchantmentName)) {
                            EnchantmentHelper.updateEnchantments(stack, mutable ->
                                    mutable.upgrade(entry.getKey(), entry.getIntValue() + 1)
                            );
                            break;
                        }
                    }
                }
            }
            return EmiStack.of(stack);
        }
    }

    private static class EmiPinnacleEnchantmentSmithingRecipe implements EmiRecipe {
        protected final ResourceLocation id;
        protected final EmiStack template;
        protected final EmiStack input;
        protected final EmiIngredient addition;
        private final int uniq;

        private EmiPinnacleEnchantmentSmithingRecipe(ResourceLocation id, ItemStack input) {
            this.id = id;
            this.template = EmiStack.of(ModItems.PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE);
            this.input = EmiStack.of(input);
            this.addition = EmiStack.of(Items.ECHO_SHARD);
            this.uniq = EmiUtil.RANDOM.nextInt();
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return VanillaEmiRecipeCategories.SMITHING;
        }

        @Override
        public @Nullable ResourceLocation getId() {
            return this.id;
        }

        @Override
        public List<EmiIngredient> getInputs() {
            return List.of(this.template, this.input, this.addition);
        }

        @Override
        public List<EmiStack> getOutputs() {
            return List.of(this.input);
        }

        @Override
        public int getDisplayWidth() {
            return 112;
        }

        @Override
        public int getDisplayHeight() {
            return 18;
        }

        @Override
        public void addWidgets(WidgetHolder widgetHolder) {
            ItemStack inputStack = this.input.getItemStack().copy();
            List<EnchantmentInstance> allEnchantments = new ArrayList<>();
            EmiPort.getEnchantmentRegistry().holders().forEach(ref -> {
                if (ref.value().canEnchant(inputStack) && ModUtil.enchantmentEligible(ref))
                    allEnchantments.add(new EnchantmentInstance(ref, ref.value().getMaxLevel()));
            });
            Collections.shuffle(allEnchantments);
            List<EnchantmentInstance> filteredEnchantments = new ArrayList<>();
            allEnchantments.forEach(ei -> {
                if (
                        !EmiPort.getEnchantmentRegistry().getTag(EnchantmentTags.CURSE).orElseThrow().contains(ei.enchantment) &&
                        filteredEnchantments.stream().allMatch(e1 -> Enchantment.areCompatible(ei.enchantment, e1.enchantment))
                ) {
                    filteredEnchantments.add(new EnchantmentInstance(ei.enchantment, ei.level));
                }
            });
            ItemEnchantments.Mutable finalEnchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            filteredEnchantments.forEach(ei -> finalEnchantments.set(ei.enchantment, ei.level));
            inputStack.set(DataComponents.ENCHANTMENTS, finalEnchantments.toImmutable());

            widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 62, 1);
            widgetHolder.addSlot(this.template, 0, 0);
            widgetHolder.addSlot(EmiStack.of(inputStack), 18, 0);
            widgetHolder.addSlot(this.addition, 36, 0);
            widgetHolder.addGeneratedSlot(r -> this.getOutput(inputStack, finalEnchantments.toImmutable(), r), this.uniq, 94, 0).recipeContext(this);
        }

        private EmiStack getOutput(ItemStack stack, ItemEnchantments enchantments, Random r) {
            stack.set(DataComponents.CUSTOM_NAME, stack.getItem().getName(stack).copy().withStyle(ChatFormatting.getByName(SSO.CONFIG.pinnacleEnchantment.pinnacleItemNameColor.get().replace(" ", "_").toUpperCase())));
            stack.set(DataComponents.ENCHANTMENTS, enchantments);
            List<EnchantmentInstance> possibleUpgrades = new ArrayList<>(enchantments.entrySet()
                    .stream().map(entry -> new EnchantmentInstance(entry.getKey(), entry.getIntValue())).toList());
            possibleUpgrades.removeIf(ei -> ei.enchantment.value().getMaxLevel() == 1);
            EnchantmentInstance toUpgrade = possibleUpgrades.get(r.nextInt(possibleUpgrades.size()));
            EnchantmentHelper.updateEnchantments(stack, mutable ->
                    mutable.upgrade(toUpgrade.enchantment, toUpgrade.level + 1)
            );
            return EmiStack.of(stack);
        }
    }
}
*///?}
