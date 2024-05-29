package org.machinemc.mcspy.util;

import io.papermc.paper.adventure.PaperAdventure;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Util for operations with item components.
 */
@Slf4j(topic = "mcSpy")
public final class ItemComponentUtil {

    private ItemComponentUtil() {
        throw new UnsupportedOperationException();
    }

    public static @Nullable Map<String, ?> deconstructValue(TypedDataComponent<?> componentData) {
        DataComponentType<?> component = componentData.type();
        Object o = componentData.value();

        if (component == DataComponents.MAX_STACK_SIZE) {
            Integer value = (Integer) o;
            if (value == 64) return null;
            return Map.of("value", value);
        }

        if (component == DataComponents.REPAIR_COST) {
            Integer value = (Integer) o;
            if (value == 0) return null;
            return Map.of("value", value);
        }

        if (component == DataComponents.LORE) {
            return deconstructLore((ItemLore) o);
        }

        if (component == DataComponents.ENCHANTMENTS) {
            return deconstructEnchantments((ItemEnchantments) o);
        }

        if (component == DataComponents.ATTRIBUTE_MODIFIERS) {
            return deconstructModifiers((ItemAttributeModifiers) o);
        }

        if (component == DataComponents.RARITY) {
            Rarity rarity = ((Rarity) o);
            if (o == Rarity.COMMON) return null;
            return Map.of("value", rarity.getSerializedName());
        }

        if (component == DataComponents.FOOD) {
            return deconstructFood((FoodProperties) o);
        }

        if (o instanceof Unit) {
            return Collections.emptyMap();
        }

        if (o instanceof Integer i) {
            if (i == 0) return null; // probably default value
            return Map.of("value", i);
        }

        if (o instanceof ResourceLocation) {
            return Map.of("value", o.toString());
        }

        return null;
    }

    public static Map<String, ?> deconstructLore(ItemLore value) {
        List<String> lines = new ArrayList<>();
        value.lines().forEach(line -> {
            net.kyori.adventure.text.Component text = PaperAdventure.asAdventure(line);
            lines.add(JSONComponentSerializer.json().serialize(text));
        });

        if (lines.isEmpty()) return null;
        return Map.of("value", lines);
    }

    public static Map<String, ?> deconstructEnchantments(ItemEnchantments value) {
        List<Enchantment> enchantments = value.keySet().stream().map(Holder::value).toList();
        Map<String, Integer> enchantmentMap = new LinkedHashMap<>();
        enchantments.forEach(enchantment -> {
            ResourceLocation location = BuiltInRegistries.ENCHANTMENT.getKey(enchantment);
            if (location == null) {
                log.warn("Missing enchantment {}", enchantment);
                return;
            }
            int level = value.getLevel(enchantment);
            enchantmentMap.put(location.toString(), level);
        });

        if (enchantmentMap.isEmpty()) return null;
        return Map.of("shown", value.showInTooltip, "enchantments", enchantmentMap);
    }

    public static Map<String, ?> deconstructModifiers(ItemAttributeModifiers value) {
        List<ItemAttributeModifiers.Entry> entries = value.modifiers();
        Map<String, Object> modifiers = new LinkedHashMap<>();
        for (ItemAttributeModifiers.Entry entry : entries) {
            Attribute attribute = entry.attribute().value();
            ResourceLocation location = BuiltInRegistries.ATTRIBUTE.getKey(attribute);
            if (location == null) {
                log.warn("Missing attribute {}", attribute);
                continue;
            }

            AttributeModifier modifier = entry.modifier();
            Map<String, Object> modifierMap = new LinkedHashMap<>();
            modifierMap.put("id", modifier.id().toString());
            modifierMap.put("name", modifier.name());
            modifierMap.put("amount", modifier.amount());
            modifierMap.put("operation", modifier.operation().getSerializedName());

            String slot = entry.slot().getSerializedName();

            Map<String, Object> entryMap = new LinkedHashMap<>();
            entryMap.put("modifier", modifierMap);
            entryMap.put("slot", slot);
            modifiers.put(location.toString(), entryMap);
        }

        if (modifiers.isEmpty()) return null;
        return Map.of("shown", value.showInTooltip(), "modifiers", modifiers);
    }

    public static Map<String, ?> deconstructFood(FoodProperties value) {
        if (value.eatSeconds() == 0) return null;

        Map<String, Object> effectMap = new LinkedHashMap<>();
        for (FoodProperties.PossibleEffect effect : value.effects()) {
            MobEffectInstance mobEffect = effect.effect();
            ResourceLocation location = BuiltInRegistries.MOB_EFFECT.getKey(mobEffect.getEffect().value());
            if (location == null) {
                log.warn("Missing mob effect {}", mobEffect.getEffect().value());
                continue;
            }
            Map<String, Object> properties = deconstructMobEffect(mobEffect, false);
            effectMap.put(location.toString(), properties);
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("nutrition", value.nutrition());
        map.put("saturation", value.saturation());
        map.put("canAlwaysEat", value.canAlwaysEat());
        map.put("eatSeconds", value.eatSeconds());
        if (!effectMap.isEmpty())
            map.put("effects", effectMap);

        return map;
    }

    private static Map<String, Object> deconstructMobEffect(MobEffectInstance value, boolean includeName) {
        Map<String, Object> effectProperties = new LinkedHashMap<>();

        if (includeName) {
            ResourceLocation location = BuiltInRegistries.MOB_EFFECT.getKey(value.getEffect().value());
            if (location == null) {
                log.warn("Missing mob effect {}", value.getEffect().value());
            } else {
                effectProperties.put("value", location.toString());
            }
        }

        effectProperties.put("duration", value.getDuration());
        effectProperties.put("amplifier", value.getAmplifier());
        effectProperties.put("ambient", value.isAmbient());
        effectProperties.put("showParticles", value.isVisible());
        effectProperties.put("showIcon", value.showIcon());
        if (value.hiddenEffect != null) {
            effectProperties.put("hidden", deconstructMobEffect(value.hiddenEffect, true));
        }

        return effectProperties;
    }

}
