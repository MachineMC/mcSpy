package org.machinemc.mcspy.util;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Util for common properties shared across multiple different parts of the game.
 */
public final class CommonPropertiesUtil {

    private CommonPropertiesUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Write necessary feature flags for the element.
     *
     * @param featureFlags feature flag set
     * @param properties properties
     */
    public static void writeFeatureFlags(FeatureFlagSet featureFlags, Map<String, Object> properties) {
        List<String> flags = new ArrayList<>();
        FeatureFlags.REGISTRY.names.forEach((location, flag) -> {
            if (flag == FeatureFlags.VANILLA) return;
            if (featureFlags.contains(flag)) flags.add(location.toString());
        });
        if (!flags.isEmpty())
            properties.put("experiments", flags);
    }

}
