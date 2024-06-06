package org.machinemc.mcspy.modules;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;
import org.machinemc.mcspy.DataModule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Module for block states.
 */
@Slf4j(topic = "mcSpy")
public class BlockStateModule extends DataModule<List<BlockState>> {

    private final Multimap<ResourceLocation, BlockState> blockStates = Multimaps.newMultimap(new LinkedHashMap<>(), ArrayList::new);

    public BlockStateModule() {
        super("block_state");
        for (BlockState next : Block.BLOCK_STATE_REGISTRY) {
            ResourceLocation location = BuiltInRegistries.BLOCK.getKey(next.getBlock());
            blockStates.put(location, next);
        }
    }

    @Override
    public Stream<List<BlockState>> getAll() {
        return blockStates.keySet().stream().map(location -> new ArrayList<>(blockStates.get(location)));
    }

    @Override
    public ResourceLocation getKey(List<BlockState> element) {
        Preconditions.checkArgument(!element.isEmpty());
        return BuiltInRegistries.BLOCK.getKey(element.getFirst().getBlock());
    }

    @Override
    public Map<String, ?> getProperties(List<BlockState> element) {
        Preconditions.checkArgument(!element.isEmpty());

        BlockState defaultState = element.getFirst().getBlock().defaultBlockState();
        int defaultId = Block.BLOCK_STATE_REGISTRY.getId(defaultState);
        Map<String, Object> defaultExtra = getExtraProperties(defaultState);

        Map<String, Object> states = new LinkedHashMap<>();

        for (BlockState state : element) {
            int id = Block.BLOCK_STATE_REGISTRY.getId(state);
            boolean isDefault = id == defaultId;

            Map<String, Object> dataProperties = new LinkedHashMap<>();
            for (Property<?> property : state.getProperties()) {
                String name = property.getName();
                Object value = state.getValue(property);
                if (value instanceof Enum<?> e) value = e.name().toLowerCase();
                dataProperties.put(name, value);
            }

            Map<String, Object> filteredExtra = new LinkedHashMap<>();
            Map<String, Object> extraProperties = getExtraProperties(state);
            for (String key : extraProperties.keySet()) {
                if (extraProperties.get(key).equals(defaultExtra.get(key))) continue;
                filteredExtra.put(key, extraProperties.get(key));
            }

            Map<String, Object> properties = new LinkedHashMap<>();

            if (isDefault) properties.put("default", true);

            if (!dataProperties.isEmpty()) properties.put("data", dataProperties);

            if (!filteredExtra.isEmpty()) properties.put("extra", filteredExtra);

            states.put(String.valueOf(id), properties);
        }

        return Map.of("defaultExtra", defaultExtra, "default", defaultId, "states", states);
    }

    @Override
    public int getIndex(List<BlockState> element) {
        return -1;
    }

    @Override
    public boolean isIndexed() {
        return false;
    }

    @Override
    public @Nullable ResourceLocation getDefaultElement() {
        return null;
    }

    @Override
    public boolean isDefaulted() {
        return false;
    }

    private Map<String, Object> getExtraProperties(BlockState state) {
        Map<String, Object> properties = new LinkedHashMap<>();

        if (state.getLightEmission() != 0)
            properties.put("lightEmission", state.getLightEmission());

        if (state.isAir())
            properties.put("air", true);

        if (state.ignitedByLava())
            properties.put("ignitedByLava", state.ignitedByLava());


        properties.put("destroySpeed", state.destroySpeed);

        if (state.getPistonPushReaction() != PushReaction.NORMAL)
            properties.put("pushReaction", state.getPistonPushReaction().name().toLowerCase());

        properties.put("mapColor", "#" + Integer.toString(state.getMapColor(null, null).col, 16));

        if (state.requiresCorrectToolForDrops())
            properties.put("requiresCorrectToolForDrops", state.requiresCorrectToolForDrops());

        if (!state.canOcclude())
            properties.put("isOccluding", false);

        if (!state.shouldSpawnTerrainParticles())
            properties.put("spawnTerrainParticles", state.shouldSpawnTerrainParticles());

        properties.put("instrument", state.instrument().getSerializedName());

        if (state.canBeReplaced())
            properties.put("replaceable", state.canBeReplaced());

        if (state.isRandomlyTicking())
            properties.put("isRandomlyTicking", true);

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
        return properties;
    }

}
