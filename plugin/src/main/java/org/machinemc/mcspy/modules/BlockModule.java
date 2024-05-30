package org.machinemc.mcspy.modules;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
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

        BlockState state = element.defaultBlockState();

        if (state.getLightEmission() != 0)
            properties.put("lightEmission", state.getLightEmission());

        if (state.isAir())
            properties.put("air", state.isAir());

        if (state.ignitedByLava())
            properties.put("ignitedByLava", state.ignitedByLava());


        properties.put("destroySpeed", state.destroySpeed);

        if (state.getPistonPushReaction() != PushReaction.NORMAL)
            properties.put("pushReaction", state.getPistonPushReaction().name().toLowerCase());

        properties.put("mapColor", "#" + Integer.toString(state.getMapColor(null, null).col, 16));

        if (state.requiresCorrectToolForDrops())
            properties.put("requiresCorrectToolForDrops", state.requiresCorrectToolForDrops());

        if (!state.canOcclude())
            properties.put("isOccluding", state.canOcclude());

        if (!state.shouldSpawnTerrainParticles())
            properties.put("spawnTerrainParticles", state.shouldSpawnTerrainParticles());

        properties.put("instrument", state.instrument().getSerializedName());

        if (state.canBeReplaced())
            properties.put("replaceable", state.canBeReplaced());

        if (state.isRandomlyTicking())
            properties.put("isRandomlyTicking", state.isRandomlyTicking());

        Map<String, Object> soundGroupMap = new LinkedHashMap<>();
        SoundType soundGroup = state.getSoundType();
        if (soundGroup.getVolume() != 1)
            soundGroupMap.put("volume", soundGroup.getVolume());
        if (soundGroup.getPitch() != 1)
            soundGroupMap.put("pitch", soundGroup.getPitch());
        soundGroupMap.put("breakSound", soundGroup.getBreakSound().getLocation().toString());
        soundGroupMap.put("stepSound", soundGroup.getStepSound().getLocation().toString());
        soundGroupMap.put("placeSound", soundGroup.getPlaceSound().getLocation().toString());
        soundGroupMap.put("hitSound", soundGroup.getHitSound().getLocation().toString());
        soundGroupMap.put("fallSound", soundGroup.getFallSound().getLocation().toString());
        properties.put("soundGroup", soundGroupMap);

        CommonPropertiesUtil.writeFeatureFlags(element.requiredFeatures(), properties);

        return properties;
    }

}
