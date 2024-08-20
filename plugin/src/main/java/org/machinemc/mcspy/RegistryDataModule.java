package org.machinemc.mcspy;

import com.google.common.base.Preconditions;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

/**
 * Registry data module that is backed by a registry.
 *
 * @param <T> element type
 */
public abstract class RegistryDataModule<T> extends DataModule<T> {

    private final Registry<T> registry;

    protected RegistryDataModule(String name, Registry<T> registry) {
        super(name);
        this.registry = Preconditions.checkNotNull(registry, "Registry can not be null");
    }

    @Override
    public Stream<T> getAll() {
        return registry.stream();
    }

    @Override
    public ResourceLocation getKey(T element) {
        return registry.getKey(element);
    }

    @Override
    public int getIndex(T element) {
        return registry.getId(element);
    }

    @Override
    public boolean isIndexed() {
        return true;
    }

    @Override
    public @Nullable ResourceLocation getDefaultElement() {
        if (!isDefaulted()) return null;
        return ((DefaultedRegistry<T>) registry).getDefaultKey();
    }

    @Override
    public boolean isDefaulted() {
        return registry instanceof DefaultedRegistry<T>;
    }

}
