package me.pajic.simple_smithing_overhaul.items;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import org.jetbrains.annotations.NotNull;
//? if <= 1.21.1
/*import net.minecraft.world.flag.FeatureFlag;*/

import java.util.List;

public class SmithingTemplateFoilItem extends SmithingTemplateItem {

    private final boolean enabled;

    //? if <= 1.21.1 {
    /*public SmithingTemplateFoilItem(Component component, Component component2, Component component3, Component component4, Component component5, List<ResourceLocation> list, List<ResourceLocation> list2, boolean enabled, FeatureFlag... featureFlags) {
        super(component, component2, component3, component4, component5, list, list2, featureFlags);
        this.enabled = enabled;
    }
    *///?} else {
    public SmithingTemplateFoilItem(Component appliesTo, Component ingredients, Component baseSlotDescription, Component additionsSlotDescription, List<ResourceLocation> baseSlotEmptyIcons, List<ResourceLocation> additionalSlotEmptyIcons, Properties properties, boolean enabled) {
        super(appliesTo, ingredients, baseSlotDescription, additionsSlotDescription, baseSlotEmptyIcons, additionalSlotEmptyIcons, properties);
        this.enabled = enabled;
    }
    //?}

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean isEnabled(@NotNull FeatureFlagSet enabledFeatures) {
        return enabled;
    }
}
