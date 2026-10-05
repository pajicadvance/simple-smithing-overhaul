package me.pajic.simple_smithing_overhaul.assets;

import me.pajic.simple_smithing_overhaul.SSO;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ModAssetPacks {

    private static final Set<String> PACKS = new HashSet<>();

    public static void init() {
        if (SSO.CONFIG.improvedExperienceBottle.renameToExperienceBottle.get()) PACKS.add("rename_xp_bottle");
        if (SSO.CONFIG.mendingRework.enabled.get()) PACKS.add("rename_mending_description");
        //? <26.1
        //PACKS.add("required");
    }

    public static Stream<String> getPacks() {
        return PACKS.stream();
    }
}
