package org.machinemc.mcspy;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Represents a module for data generation of single element type.
 *
 * @param <T> element type
 */
@Getter
public abstract class DataModule<T> {

    private final String name;

    protected DataModule(String name) {
        this.name = Preconditions.checkNotNull(name, "Name of data module can not be null");
    }

    /**
     * Returns stream of all elements for this module.
     *
     * @return all elements for this module
     */
    public abstract Stream<T> getAll();

    /**
     * Returns key for given element that will be used in the generated
     * output file.
     *
     * @param element element
     * @return key of the element
     */
    public abstract ResourceLocation getKey(T element);

    /**
     * Returns additional properties that will be generated alongside with the
     * element key.
     *
     * @param element element
     * @return additional properties
     */
    public abstract Map<String, ?> getProperties(T element);

    /**
     * Returns index for given element that will be used in the generated
     * output file.
     * <p>
     * If the element is not indexed, {@code -1} is expected.
     *
     * @param element element
     * @return index of the element in the registry
     * @see #isIndexed()
     */
    public abstract int getIndex(T element);

    /**
     * Returns whether the elements all have associated numerical ID with them.
     *
     * @return whether the data are indexed
     */
    public abstract boolean isIndexed();

    /**
     * Returns the default element if the module has one, else null.
     *
     * @return default element
     * @see #isDefaulted()
     */
    public abstract @Nullable ResourceLocation getDefaultElement();

    /**
     * Whether there is a default element for this module.
     *
     * @return if this module has default element
     */
    public abstract boolean isDefaulted();

}
