package me.pajic.simple_smithing_overhaul.criterion;

import net.minecraft.advancements.CriteriaTriggers;

public class ModCriteria {
    public static final ModCriterion REPAIR_ITEM = CriteriaTriggers.register("simple_smithing_overhaul:repair_item", new ModCriterion());
    public static final ModCriterion REPAIR_ITEM_WHETSTONE = CriteriaTriggers.register("simple_smithing_overhaul:repair_item_whetstone", new ModCriterion());
    public static final ModCriterion REPAIR_ITEM_WHETSTONE_ENCHANTED = CriteriaTriggers.register("simple_smithing_overhaul:repair_item_whetstone_enchanted", new ModCriterion());
    public static final ModCriterion MAX_WHETSTONE = CriteriaTriggers.register("simple_smithing_overhaul:max_whetstone", new ModCriterion());
    public static final ModCriterion ITEM_REPAIR_COUNT = CriteriaTriggers.register("simple_smithing_overhaul:item_repair_count", new ModCriterion());
    public static final ModCriterion ITEM_REPAIR_COUNT_BIG = CriteriaTriggers.register("simple_smithing_overhaul:item_repair_count_big", new ModCriterion());
    public static final ModCriterion DISENCHANT_ITEM = CriteriaTriggers.register("simple_smithing_overhaul:disenchant_item", new ModCriterion());
    public static final ModCriterion REDUCE_REPAIR_COST = CriteriaTriggers.register("simple_smithing_overhaul:reduce_repair_cost", new ModCriterion());
    public static final ModCriterion APPLY_ENCHANTMENT_UPGRADE = CriteriaTriggers.register("simple_smithing_overhaul:apply_enchantment_upgrade", new ModCriterion());
    public static final ModCriterion APPLY_PINNACLE_ENCHANTMENT = CriteriaTriggers.register("simple_smithing_overhaul:apply_pinnacle_enchantment", new ModCriterion());
    public static final ModCriterion BAD_RNG = CriteriaTriggers.register("simple_smithing_overhaul:bad_rng", new ModCriterion());
    public static final ModCriterion MAXED_OUT = CriteriaTriggers.register("simple_smithing_overhaul:maxed_out", new ModCriterion());
    public static final ModCriterion ANVIL_ENCHANT_COMBINE = CriteriaTriggers.register("simple_smithing_overhaul:anvil_enchant_combine", new ModCriterion());

    public static void init() {}
}
