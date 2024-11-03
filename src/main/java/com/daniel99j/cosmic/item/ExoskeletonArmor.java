package com.daniel99j.cosmic.item;

import com.daniel99j.cosmic.CosmicEvolution;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerArmorModel;
import eu.pb4.polymer.resourcepack.api.PolymerModelData;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExoskeletonArmor extends ArmorItem implements PolymerItem {
    private final PolymerModelData itemModel;
    private final PolymerArmorModel armorModel;
    private final Item itemDefault;
    private final AttributeModifiersComponent modifiers;

    public ExoskeletonArmor(EquipmentSlot slot, Identifier model, Identifier armor, Settings settings) {
        super(ModItems.EXOSKELETON_MATERIAL, switch (slot) {
            case HEAD -> Type.HELMET;
            case CHEST -> Type.CHESTPLATE;
            case LEGS -> Type.LEGGINGS;
            default -> Type.BOOTS;
        }, settings);
        this.itemDefault = getItemFor(slot, false);
        this.itemModel = PolymerResourcePackUtils.requestModel(getItemFor(slot, true), model);
        this.armorModel = PolymerResourcePackUtils.requestArmor(armor);
        this.modifiers = new AttributeModifiersComponent(List.of(
                new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(Identifier.of(CosmicEvolution.MOD_ID, "exoskeleton_"+slot.getName().toLowerCase()), 5, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.forEquipmentSlot(slot)),
                new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(Identifier.of(CosmicEvolution.MOD_ID, "exoskeleton_"+slot.getName().toLowerCase()), 5, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.forEquipmentSlot(slot)),
                new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(Identifier.of(CosmicEvolution.MOD_ID, "exoskeleton_"+slot.getName().toLowerCase()), 5, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.forEquipmentSlot(slot)),
                new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_BURNING_TIME, new EntityAttributeModifier(Identifier.of(CosmicEvolution.MOD_ID, "exoskeleton_"+slot.getName().toLowerCase()), -1, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.forEquipmentSlot(slot)),
                new AttributeModifiersComponent.Entry(EntityAttributes.GENERIC_OXYGEN_BONUS, new EntityAttributeModifier(Identifier.of(CosmicEvolution.MOD_ID, "exoskeleton_"+slot.getName().toLowerCase()), 1, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.forEquipmentSlot(slot))
        ), false);
    }

    @Override
    public AttributeModifiersComponent getAttributeModifiers() {
        return this.modifiers;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return PolymerResourcePackUtils.hasMainPack(player) ? this.itemModel.item() : this.itemDefault;
    }

    @Override
    public int getPolymerArmorColor(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return PolymerResourcePackUtils.hasMainPack(player) ? this.armorModel.color() : -1;
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return PolymerResourcePackUtils.hasMainPack(player) ? this.itemModel.value() : -1;
    }

    private static Item getItemFor(EquipmentSlot slot, boolean bool) {
        if (bool) {
            return switch (slot) {
                case HEAD -> Items.LEATHER_HELMET;
                case CHEST -> Items.LEATHER_CHESTPLATE;
                case LEGS -> Items.LEATHER_LEGGINGS;
                case FEET -> Items.LEATHER_BOOTS;
                default -> Items.NETHERITE_SCRAP;
            };
        } else {
            return switch (slot) {
                case HEAD -> Items.NETHERITE_HELMET;
                case CHEST -> Items.NETHERITE_CHESTPLATE;
                case LEGS -> Items.NETHERITE_LEGGINGS;
                case FEET -> Items.NETHERITE_BOOTS;
                default -> Items.NETHERITE_SCRAP;
            };
        }
    }

    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!world.isClient && entity instanceof PlayerEntity player && slot < 4 && player.getInventory().getArmorStack(slot) == stack) {
            //purely for fx
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 19, -1, true, false));
            if(world.getTime() % 100 == 0)
                player.heal(1);
        }
    }

    public static final ComponentType<Data> DATA_TYPE = ComponentType.<ExoskeletonArmor.Data>builder().codec(ExoskeletonArmor.Data.CODEC).cache().build();
    public record Data(boolean upgraded) {
        public static  Codec<ExoskeletonArmor.Data> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.optionalFieldOf("upgraded", false).forGetter(ExoskeletonArmor.Data::upgraded)
        ).apply(instance, ExoskeletonArmor.Data::new));
        public static final ExoskeletonArmor.Data DEFAULT = new ExoskeletonArmor.Data(false);
    }
}