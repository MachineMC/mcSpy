package org.machinemc.mcspy.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public final class SerializationUtils {

    private SerializationUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> @Nullable JsonElement createJSON(T instance, Function<T, Codec<T>> codecSupplier) {
        Codec<T> codec = codecSupplier.apply(instance);
        return JsonOps.INSTANCE.withEncoder(codec).apply(instance).result().orElse(null);
    }

    public static <T> @Nullable Map<String, ?> createMap(T instance, Function<T, Codec<T>> codecSupplier) {
        JsonElement element = createJSON(instance, codecSupplier);
        if (element == null || !element.isJsonObject()) return null;
        return element.getAsJsonObject().asMap();
    }

}
