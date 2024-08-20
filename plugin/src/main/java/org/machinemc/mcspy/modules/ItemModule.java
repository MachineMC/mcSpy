package org.machinemc.mcspy.modules;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.machinemc.mcspy.RegistryDataModule;
import org.machinemc.mcspy.util.SerializationUtils;
import org.machinemc.mcspy.util.CommonPropertiesUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Module for items.
 */
@Slf4j(topic = "mcSpy")
public class ItemModule extends RegistryDataModule<Item> {

    public ItemModule() {
        super("item", BuiltInRegistries.ITEM);
    }

    @Override
    public Map<String, ?> getProperties(Item element) {
        Map<String, Object> properties = new LinkedHashMap<>();

        ItemStack itemStack = element.getDefaultInstance();

        if (element instanceof BlockItem blockItem) {
            ResourceLocation block = BuiltInRegistries.BLOCK.getKey(blockItem.getBlock());
            properties.put("block", block.toString());
        }

        Map<String, ?> components = SerializationUtils.createMap(element.components(), map -> DataComponentMap.CODEC);
        if (components != null && !components.isEmpty())
            properties.put("components", components);

        properties.put("description", element.getDescriptionId());

        if (element.hasCraftingRemainingItem())
            properties.put("craftingRemainingItem", getKey(element.getCraftingRemainingItem()).toString());

        if (element.isComplex())
            properties.put("complex", element.isComplex());

        if (element.getUseAnimation(itemStack) != UseAnim.NONE)
            properties.put("useAnimation", element.getUseAnimation(itemStack).name().toLowerCase());

        if (element.isFoil(itemStack))
            properties.put("enchanted", element.isFoil(itemStack));

        if (element.isEnchantable(itemStack))
            properties.put("enchantable", element.isEnchantable(itemStack));

        if (element.getEnchantmentValue() != 0)
            properties.put("enchantmentValue", element.getEnchantmentValue());

        if (!element.getDrinkingSound().getLocation().toString().equals("minecraft:entity.generic.drink"))
            properties.put("drinkingSound", element.getDrinkingSound().getLocation().toString());

        if (!element.getEatingSound().getLocation().toString().equals("minecraft:entity.generic.eat"))
            properties.put("eatingSound", element.getEatingSound().getLocation().toString());

        if (!element.getBreakingSound().getLocation().toString().equals("minecraft:entity.item.break"))
            properties.put("breakingSound", element.getBreakingSound().getLocation().toString());

        CommonPropertiesUtil.writeFeatureFlags(element.requiredFeatures(), properties);

        return properties;
    }

}
