package com.daniel99j.cosmic.mixin;

import eu.pb4.polymer.core.api.other.PolymerStatusEffect;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.BlockDisplayElement;
import eu.pb4.polymer.virtualentity.api.elements.VirtualElement;
import net.borisshoes.arcananovum.ArcanaNovum;
import net.borisshoes.arcananovum.ArcanaRegistry;
import net.borisshoes.arcananovum.effects.GreaterBlindnessEffect;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Mixin(GreaterBlindnessEffect.class)
public abstract class GreaterBlindnessMixin extends StatusEffect implements PolymerStatusEffect {
    @Shadow public abstract boolean applyUpdateEffect(LivingEntity entity, int amplifier);

    protected GreaterBlindnessMixin(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public @Nullable StatusEffect getPolymerReplacement(ServerPlayerEntity player) {
        return StatusEffects.BLINDNESS.value();
    }

    @Inject(method = "applyUpdateEffect", at = @At("TAIL"), remap = false)
    public void applyUpdateEffect(LivingEntity entity, int amplifier, CallbackInfoReturnable<Boolean> cir) {
//        if(entity instanceof ServerPlayerEntity player) {
//            if(player.isAlive()) player.networkHandler.sendPacket(new HealthUpdateS2CPacket(0, 0, 0));
//        }
//        if(entity instanceof ServerPlayerEntity player && Objects.requireNonNull(player.getStatusEffect(ArcanaRegistry.GREATER_BLINDNESS_EFFECT)).getDuration() <= 1) {
//            if(player.isAlive()) player.networkHandler.sendPacket(new HealthUpdateS2CPacket(player.getHealth(), player.getHungerManager().getFoodLevel(), player.getHungerManager().getSaturationLevel()));
//        }
    }

    @Override
    public void onEntityRemoval(LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
        super.onEntityRemoval(entity, amplifier, reason);

        entity.sendMessage(Text.literal("The custom effect has been removed! Reason: " + reason.name()));
    }
}