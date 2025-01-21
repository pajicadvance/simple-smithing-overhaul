package me.pajic.simple_smithing_overhaul.compat;

import dev.emi.emi.EmiPort;
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
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

// EMI compatibility plugin for Simple Smithing Overhaul

public class EmiCompat implements EmiPlugin {
    @Override
    public void register(EmiRegistry emiRegistry) {
        //? if <= 1.21.1 {

        // Add anvil repair recipe for whetstones
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
        ModUtil.additionalRepairables.forEach(repair -> {
            emiRegistry.addRecipe(new EmiAnvilRecipe(
                    EmiStack.of(repair.left()),
                    EmiIngredient.of(repair.right()),
                    EmiPort.id(
                            "emi",
                            "/" + "anvil/repairing/material" +
                                    "/" + EmiUtil.subId(repair.left()) +
                                    "/" + counter[0]
                    )
            ));
            counter[0]++;
        });

        // Hide the "base" enchantment upgrading smithing recipe.
        // The behavior of this recipe is completely changed in the mod code so it's irrelevant.
        emiRegistry.removeRecipes(ResourceLocation.fromNamespaceAndPath(
                "simple_smithing_overhaul", "enchantment_upgrade_smithing")
        );
        // Same for pinnacle enchantment smithing recipe
        emiRegistry.removeRecipes(ResourceLocation.fromNamespaceAndPath(
                "simple_smithing_overhaul", "pinnacle_enchantment_smithing")
        );

        // Add enchantment upgrade smithing recipes for every enchantable item.
        // See the enchantment_upgradable tag for the list of included items.
        EmiIngredient input = EmiIngredient.of(Ingredient.of(TagKey.create(
                Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(
                        "simple_smithing_overhaul",
                        "enchantment_upgradeable"
                )
        )));
        counter[0] = 1;
        input.getEmiStacks().forEach(emiStack -> {
            emiRegistry.addRecipe(new EmiEnchantmentUpgradeSmithingRecipe(
                    ResourceLocation.fromNamespaceAndPath("simple_smithing_overhaul", "/enchantment_upgrade_" + counter[0]),
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
                    ResourceLocation.fromNamespaceAndPath("simple_smithing_overhaul", "/pinnacle_enchantment_" + counter[0]),
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
            } else ModUtil.additionalRepairables.forEach(repair -> {
                if (item.equals(repair.left())) {
                    addWhetstoneRepairRecipe(emiRegistry, repair.left(), repair.right(), counter[0]);
                    counter[0]++;
                }
            });
        }
        //?}
    }

    //? if <= 1.21.1 {
    private void addWhetstoneRepairRecipe(EmiRegistry emiRegistry, Item item, Ingredient repairIngredient, int counter) {
        emiRegistry.addRecipe(new EmiWhetstoneRepairRecipe(
                ResourceLocation.fromNamespaceAndPath("simple_smithing_overhaul", "/whetstone_repair_" + counter),
                EmiStack.of(item),
                EmiIngredient.of(repairIngredient)
        ));
    }

    private class EmiWhetstoneRepairRecipe implements EmiRecipe {
        protected final ResourceLocation id;
        protected final EmiStack input;
        protected final EmiStack whetstone;
        protected final EmiIngredient repairIngredient;
        private final int uniq1;
        private final int uniq2;
        private boolean enchanted;

        private EmiWhetstoneRepairRecipe(ResourceLocation id, EmiStack input, EmiIngredient repairIngredient) {
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
            return VanillaEmiRecipeCategories.CRAFTING;
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
            EmiPort.getEnchantmentRegistry().holders().forEach(enchantmentSet::add);
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
                    d -= stack.getMaxDamage() / ModUtil.determineUnitCost(stack);
                    if (d <= 0) {
                        return EmiStack.of(stack);
                    }
                }
                stack.setDamageValue(d);
            }
            return EmiStack.of(stack);
        }

        private EmiStack getWhetstone(Random r, List<EnchantmentInstance> enchantments) {
            ItemStack stack = this.whetstone.getItemStack().copy();
            if (!enchantments.isEmpty() && enchanted) {
                enchantments.forEach(enchantment -> stack.enchant(enchantment.enchantment, enchantment.level));
            }
            if (stack.getMaxDamage() > 0) {
                int d = r.nextInt(stack.getMaxDamage());
                stack.setDamageValue(d);
            }
            return EmiStack.of(stack);
        }
    }

    private class EmiEnchantmentUpgradeSmithingRecipe implements EmiRecipe {
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
            EmiPort.getEnchantmentRegistry().holders().forEach(enchantmentSet::add);
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

    private class EmiPinnacleEnchantmentSmithingRecipe implements EmiRecipe {
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
            ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            EmiPort.getEnchantmentRegistry().holders().forEach(ref -> {
                if (ref.value().isPrimaryItem(inputStack) && ref.value().exclusiveSet().size() == 0)
                    enchantments.set(ref, ref.value().getMaxLevel());
            });
            EmiPort.getEnchantmentRegistry().getTags().forEach(tag -> {
                if (tag.getFirst().equals(EnchantmentTags.CURSE)) {
                    tag.getSecond().forEach(e -> enchantments.removeIf(ie -> ie.is(e)));
                }
                else if (tag.getFirst().location().getPath().contains("exclusive_set")) {
                    List<Holder<Enchantment>> possibleEnchantments = tag.getSecond().stream().filter(e -> e.value().isPrimaryItem(inputStack)).toList();
                    if (!possibleEnchantments.isEmpty()){
                        Holder<Enchantment> e = possibleEnchantments.get(EmiUtil.RANDOM.nextInt(possibleEnchantments.size()));
                        enchantments.set(e, e.value().getMaxLevel());
                    }
                }
            });
            inputStack.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable());

            widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 62, 1);
            widgetHolder.addSlot(this.template, 0, 0);
            widgetHolder.addSlot(EmiStack.of(inputStack), 18, 0);
            widgetHolder.addSlot(this.addition, 36, 0);
            widgetHolder.addGeneratedSlot(r -> this.getOutput(inputStack, enchantments.toImmutable(), r), this.uniq, 94, 0).recipeContext(this);
        }

        private EmiStack getOutput(ItemStack stack, ItemEnchantments enchantments, Random r) {
            stack.set(DataComponents.CUSTOM_NAME, stack.getItem().getName(stack).copy().withStyle(ChatFormatting.LIGHT_PURPLE));
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
    //?}
}
