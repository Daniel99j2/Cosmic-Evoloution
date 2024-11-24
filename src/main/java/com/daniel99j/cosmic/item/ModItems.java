package com.daniel99j.cosmic.item;

import com.daniel99j.cosmic.CosmicEvolution;
import com.daniel99j.cosmic.block.blocks.CosmicForge;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import eu.pb4.polymer.core.api.other.PolymerComponent;
import net.borisshoes.arcananovum.utils.TextUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems {
    public static final BottleOfSouls BOTTLE_OF_SOULS = register("bottle_of_souls", new BottleOfSouls(new Item.Settings().rarity(Rarity.UNCOMMON).food(new FoodComponent.Builder().nutrition(1).saturationModifier(0.1F).build())));

    public static <T extends Item> T register(String path, T item) {
        Registry.register(Registries.ITEM, Identifier.of(CosmicEvolution.MOD_ID, path), item);
        return item;
    }

    public static void registerModItems() {
        CosmicEvolution.debug("Loading items");
        if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
            PolymerItemGroupUtils.registerPolymerItemGroup(Identifier.of(CosmicEvolution.MOD_ID, "item_group"), ItemGroup.create(ItemGroup.Row.BOTTOM, -1)
                    .icon(BOTTLE_OF_SOULS::getDefaultStack)
                    .displayName(Text.translatable("itemgroup." + CosmicEvolution.MOD_ID))
                    .entries(((context, entries) -> {
                        entries.add(BOTTLE_OF_SOULS);
                    })).build()
            );
        }
    }
}
