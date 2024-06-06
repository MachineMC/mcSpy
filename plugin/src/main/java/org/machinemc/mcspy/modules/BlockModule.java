package org.machinemc.mcspy.modules;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import org.machinemc.mcspy.RegistryDataModule;
import org.machinemc.mcspy.util.CommonPropertiesUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Module for blocks
 */
public class BlockModule extends RegistryDataModule<Block> {

    public BlockModule() {
        super("block", BuiltInRegistries.BLOCK);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public Map<String, ?> getProperties(Block element) {
        Map<String, Object> properties = new LinkedHashMap<>();

        properties.put("item", BuiltInRegistries.ITEM.getKey(element.asItem()).toString());

        properties.put("description", element.getDescriptionId());

        if (!element.hasCollision)
            properties.put("hasCollision", element.hasCollision);

        properties.put("explosionResistance", element.getExplosionResistance());

        if (element.getFriction() != 0.6F)
            properties.put("friction", element.getFriction());

        if (element.getSpeedFactor() != 1)
            properties.put("speedFactor", element.getSpeedFactor());

        if (element.getJumpFactor() != 1)
            properties.put("jumpFactor", element.getJumpFactor());

        CommonPropertiesUtil.writeFeatureFlags(element.requiredFeatures(), properties);

        return properties;
    }

}
