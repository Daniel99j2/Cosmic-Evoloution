package com.daniel99j.cosmic.item;

import com.daniel99j.cosmic.CosmicEvolution;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import eu.pb4.polymer.core.api.other.PolymerComponent;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems {
    public static final RegistryEntry<ArmorMaterial> EXOSKELETON_MATERIAL = registerMaterial("exoskeleton_material",
            Map.of(
                    ArmorItem.Type.HELMET, 1184,
                    ArmorItem.Type.CHESTPLATE, 1184,
                    ArmorItem.Type.LEGGINGS, 1184,
                    ArmorItem.Type.BOOTS, 1184
            ),
            1,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.ofItems(ModItems.NANOCITE_ALLOY),
            8.0F,
            1.0F,
            false);

    public static final ConcentratedDragonsBreath CONCENTRATED_DRAGONS_BREATH = register("concentrated_dragons_breath", new ConcentratedDragonsBreath(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final NanobotBox NANOBOT_BOX = register("nanobot_box", new NanobotBox(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final NanociteAlloy NANOCITE_ALLOY = register("nanocite_alloy", new NanociteAlloy(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final NanociteUpgrade NANOCITE_UPGRADE = register("nanocite_upgrade_smithing_template", new NanociteUpgrade(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final PhantomBlade PHANTOM_BLADE = register("phantom_blade", new PhantomBlade(new Item.Settings().maxCount(1).maxDamage(4062).fireproof().attributeModifiers(PhantomBlade.createAttributeModifiers(ToolMaterials.NETHERITE, 5, -0.5f)).rarity(Rarity.UNCOMMON)));
    public static final AMPE AMPE = register("ampe", new AMPE(new Item.Settings().maxCount(1).fireproof().rarity(Rarity.UNCOMMON)));
    public static final ExoskeletonArmor EXOSKELETON_HELMET = createExoskeleton(EquipmentSlot.HEAD, new Item.Settings().maxCount(1).maxDamage(1184).fireproof().rarity(Rarity.UNCOMMON));
    public static final ExoskeletonArmor EXOSKELETON_CHEST = createExoskeleton(EquipmentSlot.CHEST, new Item.Settings().maxCount(1).maxDamage(1184).fireproof().rarity(Rarity.UNCOMMON));
    public static final ExoskeletonArmor EXOSKELETON_LEGS = createExoskeleton(EquipmentSlot.LEGS, new Item.Settings().maxCount(1).maxDamage(1184).fireproof().rarity(Rarity.UNCOMMON));
    public static final ExoskeletonArmor EXOSKELETON_BOOTS = createExoskeleton(EquipmentSlot.FEET, new Item.Settings().maxCount(1).maxDamage(1184).fireproof().rarity(Rarity.UNCOMMON));

    public static <T extends Item> T register(String path, T item) {
        Registry.register(Registries.ITEM, Identifier.of(CosmicEvolution.MOD_ID, path), item);
        return item;
    }

    public static void registerModItems() {
        CosmicEvolution.debug("Loading items");

        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(CosmicEvolution.MOD_ID, "exoskeleton_upgrade"), ExoskeletonArmor.DATA_TYPE);
        PolymerComponent.registerDataComponent(ExoskeletonArmor.DATA_TYPE);
        if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
            PolymerItemGroupUtils.registerPolymerItemGroup(Identifier.of(CosmicEvolution.MOD_ID, "item_group"), ItemGroup.create(ItemGroup.Row.BOTTOM, -1)
                    .icon(PHANTOM_BLADE::getDefaultStack)
                    .displayName(Text.translatable("itemgroup." + CosmicEvolution.MOD_ID))
                    .entries(((context, entries) -> {
                        entries.add(PHANTOM_BLADE);
                        entries.add(AMPE);
                        entries.add(EXOSKELETON_HELMET);
                        entries.add(EXOSKELETON_CHEST);
                        entries.add(EXOSKELETON_LEGS);
                        entries.add(EXOSKELETON_BOOTS);
                        entries.add(CONCENTRATED_DRAGONS_BREATH);
                        entries.add(NANOBOT_BOX);
                        entries.add(NANOCITE_ALLOY);
                        entries.add(NANOCITE_UPGRADE);
                    })).build()
            );
        }
    }

    private static <T extends Item> ExoskeletonArmor createExoskeleton(EquipmentSlot slot, Item.Settings settings) {
        return register("exoskeleton_"+slot.getName().toLowerCase(), new ExoskeletonArmor(slot, Identifier.of(CosmicEvolution.MOD_ID, "exoskeleton_"+slot.getName().toLowerCase()), Identifier.of(CosmicEvolution.MOD_ID, "exoskeleton_"+slot.getName().toLowerCase()), settings));
    }

    public static RegistryEntry<ArmorMaterial> registerMaterial(String id, Map<ArmorItem.Type, Integer> defensePoints, int enchantability, RegistryEntry<SoundEvent> equipSound, Supplier<Ingredient> repairIngredientSupplier, float toughness, float knockbackResistance, boolean dyeable) {
        List<ArmorMaterial.Layer> layers = List.of(
                new ArmorMaterial.Layer(Identifier.of(CosmicEvolution.MOD_ID, id), "", dyeable)
        );

        ArmorMaterial material = new ArmorMaterial(defensePoints, enchantability, equipSound, repairIngredientSupplier, layers, toughness, knockbackResistance);
        material = Registry.register(Registries.ARMOR_MATERIAL, Identifier.of(CosmicEvolution.MOD_ID, id), material);

        return RegistryEntry.of(material);
    }
}
