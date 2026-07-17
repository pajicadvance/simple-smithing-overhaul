package me.pajic.simple_smithing_overhaul.defaulted;

import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.util.CompatFlags;
import net.atlas.defaulted.Defaulted;
import net.atlas.defaulted.component.ItemPatches;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Repairable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepairablePatchEvent {

	private static final Map<String, String> vanillaRepairables = Map.ofEntries(
			Map.entry("minecraft:bow", "minecraft:string"),
			Map.entry("minecraft:crossbow", "minecraft:string"),
			Map.entry("minecraft:fishing_rod", "minecraft:string"),
			Map.entry("minecraft:flint_and_steel", "minecraft:iron_ingot"),
			Map.entry("minecraft:shears", "minecraft:iron_ingot"),
			Map.entry("minecraft:brush", "minecraft:feather"),
			Map.entry("minecraft:carrot_on_a_stick", "minecraft:carrot"),
			Map.entry("minecraft:warped_fungus_on_a_stick", "minecraft:warped_fungus")
	);

	public static void register() {
		Defaulted.builtinPatchCreator((registry, patchApplier) -> {
			HolderLookup<Item> lookup = registry.lookupOrThrow(Registries.ITEM);
			Map<String, PatchData> patches = new HashMap<>();
			Map<String, String> repairables = new HashMap<>();
			if (SSO.CONFIG.streamlinedRepairs.vanillaRepairables.get()) {
				repairables.putAll(vanillaRepairables);
				if (!CompatFlags.BETTER_TRIDENTS_LOADED) {
					repairables.put("minecraft:trident", "minecraft:prismarine_shard");
				}
			}
			repairables.putAll(SSO.CONFIG.streamlinedRepairs.modRepairableItems.get());
			repairables.forEach((repairItem, repairMaterial) -> {
				PatchData data = patches.getOrDefault(repairMaterial, new PatchData(new ArrayList<>(), new ArrayList<>()));
				if (repairItem.startsWith("#")) lookup.get(TagKey.create(Registries.ITEM, Identifier.parse(repairItem.substring(1)))).ifPresent(data.tags::add);
				else lookup.get(ResourceKey.create(Registries.ITEM, Identifier.parse(repairItem))).ifPresent(data.items::add);
				if (!patches.containsKey(repairMaterial)) patches.put(repairMaterial, data);
			});
			patches.forEach((repairMaterial, data) -> {
				if (!data.items.isEmpty() || !data.tags.isEmpty()) {
					String repairMaterialForId = repairMaterial.substring(repairMaterial.indexOf(':') + 1);
					List<HolderSet<Item>> elements = new ArrayList<>();
					elements.add(HolderSet.direct(data.items));
					elements.addAll(data.tags);
					try {
						patchApplier.put(SSO.id(repairMaterialForId), new ItemPatches(
								elements, List.of(),
								DataComponentPatch.builder().set(
										DataComponents.REPAIRABLE,
										new Repairable(HolderSet.direct(lookup.getOrThrow(ResourceKey.create(
												Registries.ITEM,
												Identifier.parse(repairMaterial.startsWith("#") ?
														repairMaterial.substring(1) :
														repairMaterial
												)
										))))
								).build(), 1000
						));
						log(repairMaterial, data);
					} catch (Throwable t) {
						SSO.LOGGER.warn("Unable to apply repairable patch {}, skipping: {}", repairMaterialForId, t.getMessage());
					}
				}
			});
		});
	}

	private static void log(String repairMaterial, PatchData data) {
		SSO.debugLog("-----------------------------------------------------------");
		SSO.debugLog("Repair material: {}", repairMaterial);
		SSO.debugLog("Items:");
		data.items.forEach(itemHolder -> SSO.debugLog("- {}", itemHolder.getRegisteredName()));
		SSO.debugLog("Tags:");
		data.tags.forEach(tag -> SSO.debugLog("- {}", tag.key().location().toString()));
	}

	private record PatchData(List<Holder<Item>> items, List<HolderSet.Named<Item>> tags) {}
}
