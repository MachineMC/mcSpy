package org.machinemc.mcspy.modules;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import org.machinemc.mcspy.RegistryDataModule;
import org.machinemc.mcspy.util.SerializationUtils;

import java.util.Map;

public class BiomeModule extends RegistryDataModule<Biome> {

    public BiomeModule() {
        super("biome", MinecraftServer.getServer()
                .registryAccess()
                .registry(Registries.BIOME)
                .orElseThrow(() -> new RuntimeException("Missing biome registry"))
        );
    }

    @Override
    public Map<String, ?> getProperties(Biome element) {
        return SerializationUtils.createMap(element, biome -> Biome.NETWORK_CODEC);
    }

}
