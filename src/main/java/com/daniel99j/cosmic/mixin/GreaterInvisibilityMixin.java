package com.daniel99j.cosmic.mixin;

import eu.pb4.polymer.core.api.other.PolymerStatusEffect;
import net.borisshoes.arcananovum.effects.GreaterInvisibilityEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GreaterInvisibilityEffect.class)
public abstract class GreaterInvisibilityMixin implements PolymerStatusEffect {
    @Override
    public @Nullable StatusEffect getPolymerReplacement(ServerPlayerEntity player) {
        return StatusEffects.INVISIBILITY.value();
    }

}